package edu.miu.waa.onlineauctionapi.model;

import jakarta.persistence.*;
import java.util.Date;

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
  private Date transactionDate;

  @ManyToOne private User user;
}
