package com.nhnacademy.gwjs.entity;

public enum Currency {
    WON(1),
    DOLLAR(1000);

    private final double rate;

    Currency(double rate) {
        this.rate = rate;
    }

    public double getRate() {
        return rate;
    }
}
