package edu.miu.waa.onlineauctionapi.service;

import edu.miu.waa.onlineauctionapi.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
  public Product saveProduct(Product product);

  public Page<Product> getActiveProducts(Pageable pageable);

  public Page<Product> findActiveProductByStatusAndName(String name, Pageable pageable);
}
