package edu.miu.waa.onlineauctionapi.service;

import edu.miu.waa.onlineauctionapi.common.ProductStatus;
import edu.miu.waa.onlineauctionapi.model.Product;
import edu.miu.waa.onlineauctionapi.repository.ProductRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;

  @Override
  public Product saveProduct(Product product) {
    return productRepository.save(product);
  }

  @Override
  public Page<Product> findActiveProductByStatusAndName(String name, Pageable pageable) {
    return productRepository.findByStatusAndNameContainsAndBidDueDateAfterOrderByIdAsc(
        ProductStatus.RELEASE.getName(), name, addDays(-1), pageable);
  }

  private static LocalDate addDays(int days)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(new Date());
    cal.add(Calendar.DATE, days); //minus number would decrement the days

    Date input = cal.getTime();
    LocalDate date = input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    System.out.println("LocalDate" + date);
    return date;
  }

  @Override
  public Product getProduct(long id) {
    return productRepository.findById(id).orElse(null);
  }

  @Override
  public Optional<Product> findById(long id) {
    return productRepository.findById(id);
  }

  @Override
  public void delete(Product product) {
    productRepository.delete(product);
  }

  @Override
  public List<Product> getSellerProducts(String owner) {
    return productRepository.findByOwner(owner);
  }
}
