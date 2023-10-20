package edu.miu.waa.onlineauctionapi.controller;

import edu.miu.waa.onlineauctionapi.common.Constants;
import edu.miu.waa.onlineauctionapi.model.Bid;
import edu.miu.waa.onlineauctionapi.service.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.BIDS_URL_PREFIX)
public class BidController {
    private final BidService bidService;

    @PostMapping
    public Bid addBid(@RequestBody Bid bid) {
        return bidService.addBid(bid);
    }

}
