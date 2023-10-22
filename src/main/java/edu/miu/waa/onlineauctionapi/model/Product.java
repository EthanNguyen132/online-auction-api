package edu.miu.waa.onlineauctionapi.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
  private LocalDateTime bidDueDate;
  private LocalDateTime paymentDueDate;
  private String status; // draft/release

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
  
  @Transient
    private long bidCount;

}
