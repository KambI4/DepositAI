package com.coursework.deposit.model;

/**
 * Абстрактный депозит. Используется для расчётов (ООП-модель).
 */
public abstract class Deposit {

    protected String bankName;
    protected double interestRate;
    protected double minAmount;
    protected int periodMonths;
    protected DepositType type;

    protected Deposit(String bankName,
                      double interestRate,
                      double minAmount,
                      int periodMonths,
                      DepositType type) {
        this.bankName = bankName;
        this.interestRate = interestRate;
        this.minAmount = minAmount;
        this.periodMonths = periodMonths;
        this.type = type;
    }

    public String getBankName() {
        return bankName;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public int getPeriodMonths() {
        return periodMonths;
    }

    public DepositType getType() {
        return type;
    }

    /**
     * Расчёт дохода по депозиту при заданной сумме.
     */
    public abstract double calculateIncome(double amount);
}
