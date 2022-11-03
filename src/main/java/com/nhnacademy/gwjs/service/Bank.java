package com.nhnacademy.gwjs.service;

import com.nhnacademy.gwjs.entity.Currency;
import com.nhnacademy.gwjs.entity.Money;
import com.nhnacademy.gwjs.exception.MismatchCurrencyException;
import com.nhnacademy.gwjs.exception.NegativeArithmeticException;
import com.nhnacademy.gwjs.exception.SameCurrencyException;

public class Bank {

    public Money createMoney(double amount, Currency currency) {
        return Money.of(amount, currency);
    }

    public Money addMoney(Money money1, Money money2) {
        double calculated = money1.getAmount() + money2.getAmount();

        Currency currency = isDifferentCurrency(money1, money2);

        if (currency == Currency.WON) {
            return Money.of(Math.floor(calculated), money1.getCurrency());
        }

        double per = Double.parseDouble(String.format("%.2f", calculated));
        return Money.of(per, money1.getCurrency());
    }

    public Money subtractMoney(Money money1, Money money2) {
        isDifferentCurrency(money1, money2);

        if (money1.getAmount() < money2.getAmount()) {
            throw new NegativeArithmeticException(money1.getAmount(), money2.getAmount());
        }

        return Money.of(money1.getAmount() - money2.getAmount(), money1.getCurrency());
    }

    public Money exchangeWonToForeignCurrency(Money money, Currency currency) {
        if (currency == Currency.WON) {
            throw new SameCurrencyException(currency);
        }
        double changedMoney = 0;
        changedMoney = calculateExchangeFee(money) / currency.getRate();
        double roundMoney = Math.round(changedMoney * 100) / 100.0;
        return Money.of(roundMoney, Currency.DOLLAR);
    }

    public Money exchangeForeignCurrencyToWon(Money money, Currency currency) {
        double changedMoney = 0;
        changedMoney = calculateExchangeFee(money) * currency.getRate();
        double roundMoney = Math.round(changedMoney / 10) * 10.0;
        return Money.of(roundMoney, Currency.WON);
    }

    public double calculateExchangeFee(Money money) {
        return money.getAmount() - money.getAmount() * 0.15;
    }


    private Currency isDifferentCurrency(Money money1, Money money2) {
        if (money1.getCurrency() != money2.getCurrency()) {
            throw new MismatchCurrencyException(money1.getCurrency(), money2.getCurrency());
        }
        return money1.getCurrency();
    }


}
