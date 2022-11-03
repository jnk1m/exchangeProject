package com.nhnacademy.gwjs.exception;


import com.nhnacademy.gwjs.entity.Currency;

public class MismatchCurrencyException extends RuntimeException {
    public MismatchCurrencyException(Currency currency, Currency currency2) {
        super("com.nhnacademy.gwjs.entity.Currency mismatch!"+currency+ ":" +currency2);
    }
}
