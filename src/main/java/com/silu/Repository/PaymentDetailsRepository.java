package com.silu.Repository;

import com.silu.Models.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Long> {

    PaymentDetails findByUserId(long userId);
}
