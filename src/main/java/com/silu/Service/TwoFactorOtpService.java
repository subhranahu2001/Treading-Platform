package com.silu.Service;

import com.silu.Models.TwoFactorAuth;
import com.silu.Models.TwoFactorOTP;
import com.silu.Models.User;

public interface TwoFactorOtpService {

    TwoFactorOTP createTwoFactorOtp(User user,String otp,String jwt);

    TwoFactorOTP findByUser(Long userId);

    TwoFactorOTP findById(String id);

    boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp,String otp);

    void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp);
}
