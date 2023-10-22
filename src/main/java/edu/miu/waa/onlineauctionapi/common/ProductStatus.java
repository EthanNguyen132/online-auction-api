package edu.miu.waa.onlineauctionapi.common;

public enum ProductStatus {
  DRAFT("draft"),
  RELEASE("release"),
  SOLD("sold"), // sold with bid
  EXPIRED("expired"), // expired without any bid
  CANCELLED("cancelled"); // buyer have not fully paid

  private String name;

  ProductStatus(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
