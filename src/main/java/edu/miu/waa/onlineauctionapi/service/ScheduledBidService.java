package edu.miu.waa.onlineauctionapi.service;

import edu.miu.waa.onlineauctionapi.common.ProductStatus;
import edu.miu.waa.onlineauctionapi.model.Product;
import edu.miu.waa.onlineauctionapi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
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

  @Scheduled(
      fixedRateString = "${app.scheduling.task.interval}",
      initialDelayString = "${app.scheduling.task.initialDelay}")
  public void settleBids() {
    LOG.info("Settling all expired bid at {}", dateFormat.format(new Date()));
  }

  @Scheduled(fixedRateString = "${app.scheduling.task.interval}")
  @Transactional
  public void markExpiredAndReadyForBidProducts() {
    LOG.info(
        "Checking if any product is expired ready for bid settlement at {}",
        dateFormat.format(new Date()));
    LocalDate currentDate = LocalDate.now();
    Optional<List<Product>> activeProducts =
        productRepository.findByStatus(ProductStatus.RELEASE.getName());
    activeProducts.ifPresent(
        products -> {
          products.forEach(
              product -> {
                if (product.getBidDueDate().isBefore(currentDate)) {
                  product.setStatus(ProductStatus.READY_FOR_SETTLEMENT.getName());
                  productRepository.save(product);
                }
              });
        });
  }
}
