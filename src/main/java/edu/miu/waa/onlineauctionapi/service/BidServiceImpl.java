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
    LOG.info("Settling bids for product id {}", product.getId());
    // check product bid due date
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime bidDueDate = product.getBidDueDate();
    if (now.isBefore(bidDueDate)) {
      LOG.info(
          "Skipping this product since bid due date is not reached yet for product id {}",
          product.getId());
      return;
    }

    // get the highest bid
    Bid highestBid = bidRepository.findTop1ByProductIdOrderByBidPriceDesc(product.getId());
    if (highestBid == null) {
      // no bid for this product, mark as expired
      product.setStatus(ProductStatus.EXPIRED.getName());
      productRepository.save(product);
      LOG.info("No bid found for product id {}", product.getId());
      return;
    }
    List<Bid> allBid = bidRepository.findAllByProductId(product.getId());

    // check if buyer current balance is enough to pay for the product
    double buyerBalance = highestBid.getUser().getCurrentBalance();
    if (buyerBalance < highestBid.getBidPrice()) {
      LOG.info(
          "Buyer balance is not enough to pay for the product {} with name {}",
          product.getId(),
          product.getName());
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
      return;
    }

    User buyer = highestBid.getUser();
    // product sold price is the same as bid price, or we can have sold_price column in product tbl
    // product sold date is the same as bid date, or we can have sold_date column in product tbl
    LOG.info("marking product as SOLD");
    product.setStatus(ProductStatus.SOLD.getName());

    // update product winner for this bid, or we can have buyer column in product table
    LOG.info("updating highest bid & deposit record for winner");
    highestBid.setWinner(true);
    highestBid.setStatus(BidStatus.SETTLED);
    // mark the other bids for this product as expired
    Optional.ofNullable(allBid)
        .ifPresent(
            all -> {
              all.stream()
                  .filter(bid -> bid.getId() != highestBid.getId()) // exclude highest bid
                  .forEach(
                      bid -> {
                        bid.setStatus(BidStatus.EXPIRED);
                        bidRepository.save(bid);
                      });
            });

    // mark the deposit record for winner
    Bid winnerDeposit = bidRepository.findTop1ByProductIdAndUserId(product.getId(), buyer.getId());
    winnerDeposit.setWinner(true);

    LOG.info("crediting/debiting to seller/buyer & losers");
    double soldPrice = highestBid.getBidPrice();
    //    Billing latestBuyerTransaction =
    // billingRepository.findTop1ByUserIdOrderByTransactionDateDesc(buyer.getId());
    Date transactionDate = new Date();
    Billing buyerBilling =
        Billing.builder()
            .amount(
                soldPrice
                    - highestBid
                        .getDeposit()) //  debit to buyer soldPrice - deposit in transaction tbl
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
            .amount(soldPrice) // credit to seller full amount
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

    LOG.info("Refunding to losers...");
    List<Bid> allDeposits = bidRepository.findByProductIdAndDepositGreaterThan(product.getId(), 0);
    allDeposits.stream()
        .filter(
            deposit ->
                !deposit
                    .getUser()
                    .getId()
                    .equals(winnerDeposit.getUser().getId())) // exclude winner
        .forEach(
            bid -> {
              // only 1 deposit per user
              User user = bid.getUser();
              // refund deposit to each lose
              user.setCurrentBalance(user.getCurrentBalance() + bid.getDeposit());
              userRepository.save(user);

              // add new records in billing/transactions table
              Billing billing =
                  Billing.builder()
                      .amount(bid.getDeposit())
                      .user(user)
                      .balance(user.getCurrentBalance())
                      .type("Credit")
                      .transactionDate(transactionDate)
                      .details(
                          "Refund deposit for product "
                              + product.getId()
                              + ": "
                              + product.getName()
                              + " with bid price "
                              + soldPrice)
                      .build();
              billingRepository.save(billing);
            });

    LOG.info("Record billing/transactions for winner & seller...");
    billingRepository.save(buyerBilling);
    billingRepository.save(sellerBilling);

    LOG.info("Update winner's highest bid & deposit...");
    bidRepository.save(highestBid);
    bidRepository.save(winnerDeposit);
    productRepository.save(product);

    LOG.info("Update buyer & seller balance...");
    seller.setCurrentBalance(sellerBalance + soldPrice);
    buyer.setCurrentBalance(buyerBalance - soldPrice);
    userRepository.save(seller);
    userRepository.save(buyer);
    LOG.info("This product is settled successfully");
  }
}
