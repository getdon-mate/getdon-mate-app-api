package com.api.app.getdonapi.automaticpayment.repository;

import com.api.app.getdonapi.automaticpayment.domain.AutomaticPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutomaticPaymentRepository extends JpaRepository<AutomaticPayment, Long> {
}