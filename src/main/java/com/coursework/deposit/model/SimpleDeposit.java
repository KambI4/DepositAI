package com.coursework.deposit.model;

/**
 * Простой депозит без капитализации.
 */
public class SimpleDeposit extends Deposit {

    public SimpleDeposit(String bankName, double interestRate, double minAmount, int periodMonths) {
        super(bankName, interestRate, minAmount, periodMonths, DepositType.SIMPLE);
    }

    @Override
    public double calculateIncome(double amount) {
        double years = periodMonths / 12.0;
        return amount * (interestRate / 100.0) * years;
    }
}
