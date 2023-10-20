package edu.miu.waa.onlineauctionapi.service;

import edu.miu.waa.onlineauctionapi.model.Bid;
import edu.miu.waa.onlineauctionapi.repository.BidRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {
  private final BidRepository bidRepository;

  @Override
  public Bid addBid(Bid bid) {
    return bidRepository.save(bid);
  }

  @Override
  public int countTotalBidsByProductId(long id) {
    return bidRepository.countByProductId(id);
  }
}
