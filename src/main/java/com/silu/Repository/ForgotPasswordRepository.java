package com.silu.Repository;

import com.silu.Models.ForgotPasswordToken;
import com.silu.Models.User;
import com.silu.Service.ForgotPasswordService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken,String> {

    ForgotPasswordToken findByUserId(Long user_id);
}
