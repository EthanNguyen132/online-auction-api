package edu.miu.waa.onlineauctionapi.common;

public enum ProductStatus {
    DRAFT("draft"),
    RELEASE("release");

    private String name;

    ProductStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
