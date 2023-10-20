package edu.miu.waa.onlineauctionapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double deposit;
    private LocalDate depositDate;

    private LocalDate bidDate;
    private double bidPrice;
    private boolean winner;

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

}
