package com.nhnacademy.gwjs.entity;

public enum Currency {
    WON(1),
    DOLLAR(1000),
    EURO(1300);

    private final double rate;

    Currency(double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }
}
