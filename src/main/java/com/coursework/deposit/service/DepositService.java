package com.coursework.deposit.service;

import com.coursework.deposit.model.*;
import com.coursework.deposit.repository.DepositRepository;
import com.coursework.deposit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositService {

    private final DepositRepository repository;
    private final UserRepository userRepository;

    public DepositService(DepositRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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

    public void delete(Long id, String username) {
        DepositEntity d = getById(id);

        if (!d.getOwner().getUsername().equals(username)) {
            throw new SecurityException("Вы не можете удалить чужой депозит");
        }

        repository.deleteById(id);
    }

    public DepositEntity add(String bank, double rate, double minAmount,
                             int period, DepositType type, String username) {

        UserEntity owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));

        DepositEntity e = new DepositEntity(bank, rate, minAmount, period, type);
        e.setOwner(owner);

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

    public void deleteByUser(Long id, String username) {

        DepositEntity d = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Депозит не найден"));

        // Проверяем, что депозит принадлежит текущему пользователю
        if (!d.getOwner().getUsername().equals(username)) {
            throw new SecurityException("Вы не можете удалить чужой депозит");
        }

        repository.deleteById(id);
    }


    private Deposit convert(DepositEntity e) {
        return switch (e.getType()) {
            case SIMPLE -> new SimpleDeposit(e.getBankName(), e.getInterestRate(), e.getMinAmount(), e.getPeriodMonths());
            case COMPOUND -> new CompoundDeposit(e.getBankName(), e.getInterestRate(), e.getMinAmount(), e.getPeriodMonths());
            case PROGRESSIVE -> new ProgressiveDeposit(e.getBankName(), e.getInterestRate(), e.getMinAmount(), e.getPeriodMonths());
        };
    }
}
