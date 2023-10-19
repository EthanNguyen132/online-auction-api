package edu.miu.waa.onlineauctionapi.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String categories;

    @Column(nullable = false)
    private BigDecimal startingPrice;

    @Column(nullable = false)
    private BigDecimal deposit;

    @Column(nullable = false)
    private LocalDate bidDueDate;

    @Column(nullable = false)
    private LocalDate paymentDueDate;

    @Column(nullable = false)
    private boolean released;
}