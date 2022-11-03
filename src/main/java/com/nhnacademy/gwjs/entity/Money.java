package com.nhnacademy.gwjs.entity;

import com.nhnacademy.gwjs.exception.MintNegativeValueMoneyException;

import java.util.Objects;

public class Money {
    private final double amount;
    private final Currency currency;

    public Currency getCurrency() {
        return currency;
    }

    public double getAmount() {
        return this.amount;
    }

    private Money(double amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public static Money of(double amount, Currency currency) {
        isPositiveAmount(amount);
        return new Money(amount, currency);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Double.compare(money.amount, amount) == 0 && currency == money.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    private static void isPositiveAmount(double amount) {
        if (amount < 0) {
            throw new MintNegativeValueMoneyException(amount);
        }
    }
}
