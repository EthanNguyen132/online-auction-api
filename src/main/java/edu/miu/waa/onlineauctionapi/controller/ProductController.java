package edu.miu.waa.onlineauctionapi.controller;

import edu.miu.waa.onlineauctionapi.common.Constants;
import edu.miu.waa.onlineauctionapi.dto.Product;
import java.util.Arrays;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.PRODUCTS_URL_PREFIX)
public class ProductController {

  @GetMapping
  public List<Product> getProducts() {
    String[] images = {"/logo192.png"};
    return Arrays.asList(
        Product.builder()
            .id(1)
            .name("iPhone 15 Pro Max")
            .bidStartPrice(1000)
            .deposit(100)
            .images(images)
            .build(),
        Product.builder()
            .id(2)
            .name("iPhone 14 Pro Max")
            .bidStartPrice(900)
            .deposit(90)
            .images(images)
            .build(),
        Product.builder()
            .id(3)
            .name("iPhone 13 Pro Max")
            .bidStartPrice(800)
            .deposit(80)
            .images(images)
            .build(),
        Product.builder()
            .id(4)
            .name("iPhone 12 Pro Max")
            .bidStartPrice(700)
            .deposit(70)
            .images(images)
            .build());
  }
}
