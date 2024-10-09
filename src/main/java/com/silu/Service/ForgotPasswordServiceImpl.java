package com.silu.Service;

import com.silu.Domain.VerificationType;
import com.silu.Models.ForgotPasswordToken;
import com.silu.Models.User;
import com.silu.Repository.ForgotPasswordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;
    @Override
    public ForgotPasswordToken createToken(
                                                User user,
                                                String id,
                                                String otp,
                                                VerificationType verificationType,
                                                String sendTo) {
        ForgotPasswordToken token = new ForgotPasswordToken();
        token.setUser(user);
        token.setId(id);
        token.setOtp(otp);
        token.setVerificationType(verificationType);
        token.setSendTo(sendTo);

        return forgotPasswordRepository.save(token);
    }

    @Override
    public ForgotPasswordToken findById(String id) {
        return forgotPasswordRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }

    @Override
    public ForgotPasswordToken findByUser(Long userId) {
        return forgotPasswordRepository.findByUserId(userId);
    }

    @Override
    public void deleteToken(ForgotPasswordToken token) {
        forgotPasswordRepository.delete(token);
    }
}
