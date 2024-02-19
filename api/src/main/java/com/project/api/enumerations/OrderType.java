package com.project.api.enumerations;

public enum OrderType {
    DEFAULT("default"),
    MOST_RATED("most-rated"),
    BEST_SELLER("best-seller"),
    PRICE("price"),
    TOTAL_PRICE("total_price"),
    TOP("top"),
    SALE("sale"),
    NEWEST("newest");
    private final String value;

    OrderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
