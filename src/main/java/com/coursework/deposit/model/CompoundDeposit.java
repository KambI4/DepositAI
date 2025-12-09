package com.coursework.deposit.model;

/**
 * Депозит с ежемесячной капитализацией процентов.
 */
public class CompoundDeposit extends Deposit {

    public CompoundDeposit(String bankName, double interestRate, double minAmount, int periodMonths) {
        super(bankName, interestRate, minAmount, periodMonths, DepositType.COMPOUND);
    }

    @Override
    public double calculateIncome(double amount) {
        double monthlyRate = (interestRate / 100.0) / 12.0;
        double finalAmount = amount * Math.pow(1.0 + monthlyRate, periodMonths);
        return finalAmount - amount;
    }
}
