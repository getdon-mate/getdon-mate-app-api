package com.api.app.getdonapi.withdrawal.repository;

import com.api.app.getdonapi.withdrawal.domain.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {

}