package edu.miu.waa.onlineauctionapi.repository;

import edu.miu.waa.onlineauctionapi.model.Bid;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
  public int countByProductIdAndBidPriceGreaterThan(long id, double price);

  boolean existsByUserIdAndProductIdAndDepositGreaterThan(
      long userId, long productId, double deposit);

  Bid findTop1ByProductIdOrderByBidPriceDesc(long productId);

  Bid findTop1ByProductIdAndByUserId(long productId, long userId);

  List<Bid> findAllByProductId(long productId);
}
