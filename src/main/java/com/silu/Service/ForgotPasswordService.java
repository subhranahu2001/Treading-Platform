package com.silu.Service;

import com.silu.Domain.VerificationType;
import com.silu.Models.ForgotPasswordToken;
import com.silu.Models.User;

public interface ForgotPasswordService {

    ForgotPasswordToken createToken(
            User user,
            String id,
            String otp,
            VerificationType verificationType,
            String sendTo
    );

    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);

}
