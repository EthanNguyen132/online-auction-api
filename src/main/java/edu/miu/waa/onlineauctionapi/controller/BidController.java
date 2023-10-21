package edu.miu.waa.onlineauctionapi.controller;

import edu.miu.waa.onlineauctionapi.common.Constants;
import edu.miu.waa.onlineauctionapi.dto.BidResponse;
import edu.miu.waa.onlineauctionapi.model.Bid;
import edu.miu.waa.onlineauctionapi.model.User;
import edu.miu.waa.onlineauctionapi.service.BidService;
import edu.miu.waa.onlineauctionapi.service.ProductService;
import edu.miu.waa.onlineauctionapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.BIDS_URL_PREFIX)
public class BidController {
  private final BidService bidService;
  private final UserService userService;
  private final ProductService productService;

  @PostMapping
  public BidResponse addBid(@RequestBody Bid bid) {
    String email = bid.getUser().getEmail();

    // check valid user
    User user = userService.findUser(email);
    if (user == null) {
      return BidResponse.builder().success(false).message("Invalid user").build();
    }
    bid.setUser(user);

    // check if deposit
    boolean hasDeposit = bidService.hasDeposit(user.getId(), bid.getProduct().getId());
    if (!hasDeposit) {
      return BidResponse.builder()
          .success(false)
          .message("Required deposit")
          .requiredDeposit(true)
          .build();
    }
    // save bid
    bidService.addBid(bid);
    return BidResponse.builder().success(true).build();
  }

  @PostMapping("/deposit")
  public BidResponse makeDeposit(@RequestBody Bid bid) {
    return bidService.makeDeposit(bid);
  }
}
