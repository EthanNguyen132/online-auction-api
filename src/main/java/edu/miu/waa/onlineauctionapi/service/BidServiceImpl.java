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

        return bidRepository.countByProductIdAndBidPriceGreaterThan(id, 0);
    }

    @Override
    public boolean hasDeposit(long userId, long productId) {
        return bidRepository.existsByUserIdAndProductIdAndDepositNotNull(userId, productId);
    }

    @Override
    public Bid getCurrentBidByProductId(long productId) {
        Bid bid = bidRepository.findTop1ByProductIdOrderByBidPriceDesc(productId);
        return bid;
    }
}
