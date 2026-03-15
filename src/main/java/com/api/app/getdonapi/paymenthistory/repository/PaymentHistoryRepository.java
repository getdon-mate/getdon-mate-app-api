package com.api.app.getdonapi.paymenthistory.repository;

import com.api.app.getdonapi.paymenthistory.domain.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
}