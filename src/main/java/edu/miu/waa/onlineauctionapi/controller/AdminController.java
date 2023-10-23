package edu.miu.waa.onlineauctionapi.controller;

import edu.miu.waa.onlineauctionapi.common.Constants;
import edu.miu.waa.onlineauctionapi.dto.ApiResponse;
import edu.miu.waa.onlineauctionapi.exception.BidProcessingException;
import edu.miu.waa.onlineauctionapi.model.Product;
import edu.miu.waa.onlineauctionapi.service.BidService;
import edu.miu.waa.onlineauctionapi.service.ProductService;
import edu.miu.waa.onlineauctionapi.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Constants.ADMIN_URL_PREFIX)
public class AdminController {

  private final BidService bidService;
  private final UserService userService;
  private final ProductService productService;

  @PreAuthorize("hasRole('ADMIN')")
  @PostMapping("/bids/settle/{productId}")
  public ApiResponse<?> settleProductBids(@PathVariable long productId)
      throws BidProcessingException {
    bidService.settleProductBidsById(productId);
    return ApiResponse.builder().success(true).build();
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/bids")
  public ApiResponse<?> findAllActiveProductsForSettlement() throws BidProcessingException {

    List<Product> products = productService.findAllActiveProductsForSettlement();

    return ApiResponse.builder().success(true).data(products).build();
  }
}
