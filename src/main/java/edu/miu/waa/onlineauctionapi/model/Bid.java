package edu.miu.waa.onlineauctionapi.model;

import jakarta.persistence.*;
import java.util.Date;
import lombok.Data;

@Entity
@Data
public class Bid {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private double deposit;
  private Date depositDate;

  private Date bidDate;
  private double bidPrice;
  private boolean winner;

  @ManyToOne private User user;

  @ManyToOne private Product product;
}
