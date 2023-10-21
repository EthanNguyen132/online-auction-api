package edu.miu.waa.onlineauctionapi.service;

import edu.miu.waa.onlineauctionapi.common.TransactionType;
import edu.miu.waa.onlineauctionapi.dto.BidResponse;
import edu.miu.waa.onlineauctionapi.model.Bid;
import edu.miu.waa.onlineauctionapi.model.Billing;
import edu.miu.waa.onlineauctionapi.model.User;
import edu.miu.waa.onlineauctionapi.repository.BidRepository;
import edu.miu.waa.onlineauctionapi.repository.BillingRepository;
import edu.miu.waa.onlineauctionapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
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


}
