package edu.miu.waa.onlineauctionapi.repository;

import edu.miu.waa.onlineauctionapi.model.Product;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  Page<Product> findByStatusAndNameContainsAndBidDueDateAfterOrderByIdAsc(
          String status, String name, LocalDate bidDueDate, Pageable pageable);

  List<Product> findByOwner(String owner);
}
