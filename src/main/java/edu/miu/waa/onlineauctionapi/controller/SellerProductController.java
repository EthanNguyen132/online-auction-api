package edu.miu.waa.onlineauctionapi.controller;

import edu.miu.waa.onlineauctionapi.common.Constants;
import edu.miu.waa.onlineauctionapi.model.SellerProduct;
import edu.miu.waa.onlineauctionapi.repository.SellerProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.SELLER_PRODUCTS_URL_PREFIX)
@CrossOrigin(origins = "http://localhost:3000")
public class SellerProductController {

  @Autowired private SellerProductRepository sellerProductRepository;

  @GetMapping
  public List<SellerProduct> getAllProducts() {
    return sellerProductRepository.findAll();
  }

  @PostMapping
  public SellerProduct createProduct(@RequestBody SellerProduct product) {
    return sellerProductRepository.save(product);
  }

  @PutMapping("/{id}")
  public ResponseEntity<SellerProduct> updateProduct(
      @PathVariable Long id, @RequestBody SellerProduct updatedProduct) {
    return sellerProductRepository
        .findById(id)
        .map(
            product -> {
              product.setTitle(updatedProduct.getTitle());
              product.setDescription(updatedProduct.getDescription());
              product.setCategories(updatedProduct.getCategories());
              product.setStartingPrice(updatedProduct.getStartingPrice());
              product.setDeposit(updatedProduct.getDeposit());
              product.setBidDueDate(updatedProduct.getBidDueDate());
              product.setPaymentDueDate(updatedProduct.getPaymentDueDate());
              product.setReleased(updatedProduct.isReleased());
              return new ResponseEntity<>(sellerProductRepository.save(product), HttpStatus.OK);
            })
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
    return sellerProductRepository
        .findById(id)
        .map(
            product -> {
              sellerProductRepository.delete(product);
              return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
            })
        .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }
}
