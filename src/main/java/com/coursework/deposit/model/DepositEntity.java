package com.coursework.deposit.model;

import jakarta.persistence.*;

/**
 * JPA-сущность для хранения депозитов в базе данных.
 */
@Entity
@Table(name = "deposits")
public class DepositEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;
    private double interestRate;
    private double minAmount;
    private int periodMonths;

    @Enumerated(EnumType.STRING)
    private DepositType type;

    public DepositEntity() {
    }

    public DepositEntity(String bankName, double interestRate, double minAmount,
                         int periodMonths, DepositType type) {
        this.bankName = bankName;
        this.interestRate = interestRate;
        this.minAmount = minAmount;
        this.periodMonths = periodMonths;
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public int getPeriodMonths() {
        return periodMonths;
    }

    public void setPeriodMonths(int periodMonths) {
        this.periodMonths = periodMonths;
    }

    public DepositType getType() {
        return type;
    }

    public void setType(DepositType type) {
        this.type = type;
    }
}
