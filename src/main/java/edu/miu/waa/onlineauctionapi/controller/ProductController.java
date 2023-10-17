package edu.miu.waa.onlineauctionapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.miu.waa.onlineauctionapi.common.Constants;

@RestController
@RequestMapping(Constants.PRODUCTS_URL_PREFIX)
public class ProductController {

    @GetMapping
    public String getProducts(){
        return "Product data";
    }
}
