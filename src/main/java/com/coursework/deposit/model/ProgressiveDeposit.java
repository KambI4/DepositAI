package com.coursework.deposit.model;

/**
 * Прогрессивный депозит: ставка растёт к концу срока.
 */
public class ProgressiveDeposit extends Deposit {

    public ProgressiveDeposit(String bankName, double interestRate, double minAmount, int periodMonths) {
        super(bankName, interestRate, minAmount, periodMonths, DepositType.PROGRESSIVE);
    }

    @Override
    public double calculateIncome(double amount) {
        double maxRate = interestRate * 1.5;
        double avgRate = (interestRate + maxRate) / 2.0;
        double years = periodMonths / 12.0;
        return amount * (avgRate / 100.0) * years;
    }
}
