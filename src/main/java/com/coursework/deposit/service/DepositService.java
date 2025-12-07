package com.coursework.deposit.service;

import com.coursework.deposit.model.*;
import com.coursework.deposit.repository.DepositRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositService {

    private final DepositRepository repository;

    public DepositService(DepositRepository repository) {
        this.repository = repository;
    }

    public List<DepositEntity> getAll() {
        return repository.findAll();
    }

    public DepositEntity getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Депозит не найден"));
    }

    public DepositEntity save(DepositEntity entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public DepositEntity add(String bank, double rate, double minAmount,
                             int period, DepositType type) {

        DepositEntity e = new DepositEntity(bank, rate, minAmount, period, type);
        return repository.save(e);
    }

    public double calculateIncome(Long depositId, double amount) {
        DepositEntity entity = getById(depositId);
        Deposit domain = convert(entity);
        return domain.calculateIncome(amount);
    }

    public DepositEntity findBest(double amount) {
        return repository.findAll().stream()
                .max((d1, d2) -> Double.compare(
                        convert(d1).calculateIncome(amount),
                        convert(d2).calculateIncome(amount)
                ))
                .orElse(null);
    }

    private Deposit convert(DepositEntity e) {
        return switch (e.getType()) {
            case SIMPLE -> new SimpleDeposit(e.getBankName(), e.getInterestRate(), e.getMinAmount(), e.getPeriodMonths());
            case COMPOUND -> new CompoundDeposit(e.getBankName(), e.getInterestRate(), e.getMinAmount(), e.getPeriodMonths());
            case PROGRESSIVE -> new ProgressiveDeposit(e.getBankName(), e.getInterestRate(), e.getMinAmount(), e.getPeriodMonths());
        };
    }
}
