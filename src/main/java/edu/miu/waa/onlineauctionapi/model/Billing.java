package edu.miu.waa.onlineauctionapi.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data
public class Billing {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private double amount;
  private String type; // debit/credit
  private String details; // deposit/refund
  private double balance;
  private LocalDate transactionDate;

  @ManyToOne private User user;
}
