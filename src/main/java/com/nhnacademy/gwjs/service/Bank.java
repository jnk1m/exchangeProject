package com.nhnacademy.gwjs.service;

import com.nhnacademy.gwjs.entity.Currency;
import com.nhnacademy.gwjs.entity.Money;
import com.nhnacademy.gwjs.exception.MismatchCurrencyException;
import com.nhnacademy.gwjs.exception.NegativeArithmeticException;

public class Bank {

    public Money mintMoney(double amount, Currency currency){
        return Money.of(amount,currency);
    }

    public Money add(Money money1, Money money2) {
        double calculated = money1.getAmount() + money2.getAmount();

        Currency currency = checkCurrency(money1, money2);

        if(currency == Currency.WON){
            return Money.of(Math.floor(calculated), money1.getCurrency());
        }

        double per = Double.parseDouble(String.format("%.2f",calculated));
        return Money.of(per, money1.getCurrency());
    }

    public Money subtraction(Money money1,Money money2) {
        checkCurrency(money1, money2);

        if(money1.getAmount() < money2.getAmount()){
            throw  new NegativeArithmeticException(money1.getAmount(), money2.getAmount());
        }

        return Money.of(money1.getAmount() - money2.getAmount(), money1.getCurrency());
    }
    
    public Money exchange(Money money) {
        double changedMoney = 0;

        if(money.getCurrency() == Currency.WON){ //원화에서 달러
            changedMoney = calculateExchangeFee(money) / Currency.DOLLAR.getRate();
            double roundMoney = Math.round(changedMoney * 100) / 100.0;
            return Money.of(roundMoney,Currency.DOLLAR);
        }

        changedMoney = calculateExchangeFee(money) * Currency.DOLLAR.getRate(); //달러에서 원화
        double roundMoney = Math.round(changedMoney / 10) * 10.0;
        return Money.of(roundMoney,Currency.WON);


    }
    public double calculateExchangeFee(Money money) {
        return money.getAmount() - money.getAmount() * 0.15;
    }





    private Currency checkCurrency(Money money1, Money money2) {
        if(money1.getCurrency() != money2.getCurrency()){
            throw new MismatchCurrencyException(money1.getCurrency(), money2.getCurrency());
        }

        return money1.getCurrency();
    }


}
