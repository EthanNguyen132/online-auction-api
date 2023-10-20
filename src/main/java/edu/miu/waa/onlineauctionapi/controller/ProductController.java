package edu.miu.waa.onlineauctionapi.controller;

import edu.miu.waa.onlineauctionapi.common.Constants;
import edu.miu.waa.onlineauctionapi.dto.ProductResponse;
import edu.miu.waa.onlineauctionapi.dto.ProductSearchRequest;
import edu.miu.waa.onlineauctionapi.model.Bid;
import edu.miu.waa.onlineauctionapi.model.Product;
import edu.miu.waa.onlineauctionapi.service.BidService;
import edu.miu.waa.onlineauctionapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.PRODUCTS_URL_PREFIX)
@RequiredArgsConstructor
public class ProductController {
  private final ProductService productService;
  private final BidService bidService;

  @PostMapping("/search")
  public ProductResponse searchProduct(@RequestBody ProductSearchRequest searchRequest) {
    PageRequest pageRequest =
        PageRequest.of(searchRequest.getPageNumber() - 1, searchRequest.getPageSize());

    Page<Product> list =
        productService.findActiveProductByStatusAndName(searchRequest.getName(), pageRequest);
    ProductResponse res =
        ProductResponse.builder()
            .success(true)
            .data(list.getContent())
            .totalPages(list.getTotalPages())
            .totalElements(list.getTotalElements())
            .build();
    return res;
  }

  @GetMapping("/{id}")
  public ProductResponse getProductDetails(@PathVariable long id) {
    Product product = productService.getProduct(id);
    Bid currentBid = bidService.getCurrentBidByProductId(id);

    ProductResponse res =
        ProductResponse.builder()
            .success(true)
            .data(product)
            .totalBids(bidService.countTotalBidsByProductId(id))
            .currentBid(currentBid == null ? 0 : currentBid.getBidPrice())
            .build();
    return res;
  }
}
