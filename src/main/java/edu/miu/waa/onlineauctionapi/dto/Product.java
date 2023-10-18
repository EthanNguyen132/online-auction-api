package edu.miu.waa.onlineauctionapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Product {

  private int id;
  private String name;
  private double bidStartPrice;
  private double deposit;
  private String[] images;
}
