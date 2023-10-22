package edu.miu.waa.onlineauctionapi.service;

import edu.miu.waa.onlineauctionapi.common.ProductStatus;
import edu.miu.waa.onlineauctionapi.common.TransactionType;
import edu.miu.waa.onlineauctionapi.dto.BidResponse;
import edu.miu.waa.onlineauctionapi.exception.BidProcessingException;
import edu.miu.waa.onlineauctionapi.model.Bid;
import edu.miu.waa.onlineauctionapi.model.BidStatus;
import edu.miu.waa.onlineauctionapi.model.Billing;
import edu.miu.waa.onlineauctionapi.model.Product;
import edu.miu.waa.onlineauctionapi.model.User;
import edu.miu.waa.onlineauctionapi.repository.BidRepository;
import edu.miu.waa.onlineauctionapi.repository.BillingRepository;
import edu.miu.waa.onlineauctionapi.repository.ProductRepository;
import edu.miu.waa.onlineauctionapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

  private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(BidServiceImpl.class);

  private final ProductRepository productRepository;
  private final BidRepository bidRepository;
  private final UserRepository userRepository;
  private final BillingRepository billingRepository;

  @Override
  public Bid addBid(Bid bid) {
    return bidRepository.save(bid);
  }

  @Override
  public int countTotalBidsByProductId(long id) {

    return bidRepository.countByProductIdAndBidPriceGreaterThan(id, 0);
  }

  @Override
  public boolean hasDeposit(long userId, long productId) {
    return bidRepository.existsByUserIdAndProductIdAndDepositGreaterThan(userId, productId, 0);
  }

  @Override
  public Bid getCurrentBidByProductId(long productId) {
    Bid bid = bidRepository.findTop1ByProductIdOrderByBidPriceDesc(productId);
    return bid;
  }

  @Override
  @Transactional
  public BidResponse makeDeposit(Bid bid) {
    String email = bid.getUser().getEmail();

    // check valid user
    User user = userRepository.findByEmail(email);
    if (user == null) {
      return BidResponse.builder().success(false).message("Invalid user").build();
    }
    // update balance in User table
    double currentBalance = user.getCurrentBalance() - bid.getDeposit();
    user.setCurrentBalance(currentBalance);

    // save bid
    bid.setUser(user);
    bidRepository.save(bid);

    // save billing
    Billing billing = new Billing();
    billing.setAmount(bid.getDeposit());
    billing.setType(TransactionType.DEBIT.getName());
    billing.setDetails("Deposit for product " + bid.getProduct().getId());
    billing.setTransactionDate(bid.getDepositDate());
    billing.setBalance(currentBalance);
    billing.setUser(user);
    billingRepository.save(billing);

    return BidResponse.builder().success(true).build();
  }

  @Override
  public List<Bid> findByUserIdOrderByProductIdAscBidDateDesc(String userId) {
    return bidRepository.findByUserEmailOrderByProductIdAscBidDateDesc(userId);
  }

  @Transactional
  public void settleProductBid(Product product) throws BidProcessingException {
    // check product bid due date
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime bidDueDate = product.getBidDueDate();
    if (now.isBefore(bidDueDate)) {
      LOG.error("Bid due date is not reached yet for product id {}", product.getId());
      throw new BidProcessingException("Bid due date is not reached yet");
    }

    // get the highest bid
    Bid highestBid = bidRepository.findTop1ByProductIdOrderByBidPriceDesc(product.getId());
    List<Bid> allBid = bidRepository.findAllByProductId(product.getId());

    // check if buyer current balance is enough to pay for the product
    double buyerBalance = highestBid.getUser().getCurrentBalance();
    if (buyerBalance > highestBid.getBidPrice()) {
      // cancelling all the bid for this product, mark product as cancelled too!
      product.setStatus(ProductStatus.CANCELLED.getName());
      Optional.ofNullable(allBid)
          .ifPresent(
              all -> {
                all.forEach(
                    bid -> {
                      bid.setStatus(BidStatus.CANCELLED);
                      bidRepository.save(bid);
                    });
              });
      LOG.error(
          "Buyer balance is not enough to pay for the product {} with name {}",
          product.getId(),
          product.getName());
      return;
    }

    User buyer = highestBid.getUser();
    // update product status to sold
    product.setStatus(ProductStatus.SOLD.getName());
    // update product winner to this user bid table, or we can have buyer column in product table
    highestBid.setWinner(true);
    // product sold price is the same as bid price, or we can have sold_price column in product tbl
    // product sold date is the same as bid date, or we can have sold_date column in product tbl
    // mark product winner deposit as final, i.e. deposit row of the same buyer to winner = 1
    Bid winnerDeposit = bidRepository.findTop1ByProductIdAndUserId(product.getId(), buyer.getId());
    winnerDeposit.setWinner(true);

    // credit to seller, debit to buyer in transaction tbl
    double soldPrice = highestBid.getBidPrice();
    //    Billing latestBuyerTransaction =
    // billingRepository.findTop1ByUserIdOrderByTransactionDateDesc(buyer.getId());
    Date transactionDate = new Date();
    Billing buyerBilling =
        Billing.builder()
            .amount(soldPrice)
            .user(buyer)
            .balance(buyerBalance - soldPrice)
            .type("Debit")
            .transactionDate(transactionDate)
            .details(
                "Payment for product "
                    + product.getId()
                    + ": "
                    + product.getName()
                    + " with bid price "
                    + soldPrice)
            .build();

    User seller = userRepository.findByEmail(product.getOwner());
    double sellerBalance = seller.getCurrentBalance();
    Billing sellerBilling =
        Billing.builder()
            .amount(soldPrice)
            .user(seller)
            .balance(sellerBalance + soldPrice)
            .type("Credit")
            .transactionDate(transactionDate)
            .details(
                "Receive payment for product "
                    + product.getId()
                    + ": "
                    + product.getName()
                    + " with bid price "
                    + soldPrice)
            .build();

    // save billing
    billingRepository.save(buyerBilling);
    billingRepository.save(sellerBilling);

    // save bid records
    // mark the other bids for this product as expired, mark product as SOLD
    Optional.ofNullable(allBid)
        .ifPresent(
            all -> {
              all.stream()
                  .filter(bid -> bid.getId() != highestBid.getId())
                  .forEach(
                      bid -> {
                        bid.setStatus(BidStatus.EXPIRED);
                        bidRepository.save(bid);
                      });
            });
    bidRepository.save(highestBid);
    bidRepository.save(winnerDeposit);
    productRepository.save(product);

    // update balance in User table
    seller.setCurrentBalance(sellerBalance + soldPrice);
    buyer.setCurrentBalance(buyerBalance - soldPrice);
    userRepository.save(seller);
    userRepository.save(buyer);
  }
}
