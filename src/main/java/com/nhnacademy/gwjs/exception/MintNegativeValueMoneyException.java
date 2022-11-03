package com.nhnacademy.gwjs.exception;

public class MintNegativeValueMoneyException extends RuntimeException {
    public MintNegativeValueMoneyException(double amount) {
        super("Amount must be positive number. Current amount: " + amount);
    }
}
