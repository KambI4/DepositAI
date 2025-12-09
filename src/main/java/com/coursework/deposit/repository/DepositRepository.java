package com.coursework.deposit.repository;

import com.coursework.deposit.model.DepositEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<DepositEntity, Long> {
}
