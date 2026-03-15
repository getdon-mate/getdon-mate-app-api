package com.api.app.getdonapi.deposit.repository;

import com.api.app.getdonapi.deposit.domain.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
}