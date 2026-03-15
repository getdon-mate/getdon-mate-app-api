package com.api.app.getdonapi.onetimepaymentmember.repository;

import com.api.app.getdonapi.onetimepaymentmember.domain.OneTimePaymentMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OneTimePaymentMemberRepository extends JpaRepository<OneTimePaymentMember, Long> {
}