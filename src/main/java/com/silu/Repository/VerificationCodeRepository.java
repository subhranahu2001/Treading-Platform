package com.silu.Repository;

import com.silu.Models.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {

    public VerificationCode findByUserId(Long userId);

}
