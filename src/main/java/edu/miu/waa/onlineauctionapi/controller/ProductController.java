package edu.miu.waa.onlineauctionapi.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
//    @PreAuthorize("hasRole('" + Const.ADMIN + "')")
    @PreAuthorize("hasRole('ADMIN')")
    public String getProducts(){
        return "Hello World";
    }
}
