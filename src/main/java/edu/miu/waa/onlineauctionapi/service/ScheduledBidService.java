package edu.miu.waa.onlineauctionapi.service;

import edu.miu.waa.onlineauctionapi.common.ProductStatus;
import edu.miu.waa.onlineauctionapi.exception.BidProcessingException;
import edu.miu.waa.onlineauctionapi.model.Product;
import edu.miu.waa.onlineauctionapi.repository.ProductRepository;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledBidService {
  private static final Logger LOG = LoggerFactory.getLogger(ScheduledBidService.class);
  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  private final ProductRepository productRepository;
  private final BidService bidService;

  @Scheduled(
      fixedRateString = "${app.scheduling.task.interval}",
      initialDelayString = "${app.scheduling.task.initialDelay}")
  public void settleBids() {
    LOG.info("Settling bids for all active products");
    List<Product> activeProducts = getActiveProducts();

    if (!activeProducts.isEmpty()) {
      activeProducts.forEach(this::settleBidsForProduct);
    } else {
      LOG.info("No active products found");
    }
  }

  private void settleBidsForProduct(Product product) {
    try {
      bidService.settleProductBids(product);
    } catch (BidProcessingException e) {
      LOG.error("Error while settling bids for product {}", product.getId());
      LOG.error(e.getMessage(), e);
    }
  }

  private List<Product> getActiveProducts() {
    Optional<List<Product>> products = productRepository.findByStatus(ProductStatus.RELEASE);
    return products.orElse(Collections.emptyList());
  }
}
