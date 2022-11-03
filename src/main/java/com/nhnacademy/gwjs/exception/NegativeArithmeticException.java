package com.nhnacademy.gwjs.exception;

public class NegativeArithmeticException extends RuntimeException {
    public NegativeArithmeticException(double amount, double amount1) {
        super("The result of the operation is negative : " + (amount - amount1));
    }
}
