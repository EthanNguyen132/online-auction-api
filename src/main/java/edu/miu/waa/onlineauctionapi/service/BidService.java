package edu.miu.waa.onlineauctionapi.service;

import edu.miu.waa.onlineauctionapi.model.Bid;

public interface BidService {
  public Bid addBid(Bid bid);

  public int countTotalBidsByProductId(long productId);

  public boolean hasDeposit(long userId, long productId);

  public Bid getCurrentBidByProductId(long productId);
}
