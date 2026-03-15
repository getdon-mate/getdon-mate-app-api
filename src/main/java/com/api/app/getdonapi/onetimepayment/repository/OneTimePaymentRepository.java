package com.api.app.getdonapi.onetimepayment.repository;

import com.api.app.getdonapi.onetimepayment.domain.OneTimePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneTimePaymentRepository extends JpaRepository<OneTimePayment, Long> {
}