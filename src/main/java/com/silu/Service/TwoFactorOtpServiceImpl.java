package com.silu.Service;

import com.silu.Models.TwoFactorOTP;
import com.silu.Models.User;
import com.silu.Repository.TwoFactorOTPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TwoFactorOtpServiceImpl implements TwoFactorOtpService {

    @Autowired
    private TwoFactorOTPRepository twoFactorOTPRepository;

    @Override
    public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt) {

        String id = UUID.randomUUID().toString();
        TwoFactorOTP twoFactorOTP =
                TwoFactorOTP.builder()
                        .jwt(jwt)
                        .user(user)
                        .otp(otp)
                        .id(id).build();
        return twoFactorOTPRepository.save(twoFactorOTP);
    }

    @Override
    public TwoFactorOTP findByUser(Long userId) {

        return twoFactorOTPRepository.findByUserId(userId);
    }

    @Override
    public TwoFactorOTP findById(String id) {
        return twoFactorOTPRepository.findById(id).orElse(null);
    }

    @Override
    public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp) {

        return twoFactorOtp.getOtp().equals(otp);
    }

    @Override
    public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOtp) {

        twoFactorOTPRepository.delete(twoFactorOtp);
    }
}
