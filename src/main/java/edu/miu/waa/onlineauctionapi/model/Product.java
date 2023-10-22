package edu.miu.waa.onlineauctionapi.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Data
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  @Column(length = 2000)
  private String description;

  private double deposit;
  private double bidStartPrice;
  private String status; // draft/release/SOLD/EXPIRED
  private LocalDateTime bidDueDate;
  private LocalDateTime paymentDueDate;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "product_id")
  List<ProductImage> images;

  private List<String> categories;

  @Column(length = 2000)
  private String conditionOfSale;

  @Column(length = 2000)
  private String shippingInformation;

  private String owner;
  private Date created;

  @Transient private long bidCount;
}
