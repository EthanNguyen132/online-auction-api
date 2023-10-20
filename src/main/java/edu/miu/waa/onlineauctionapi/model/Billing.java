package edu.miu.waa.onlineauctionapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

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

    @ManyToOne
    private User user;
}
