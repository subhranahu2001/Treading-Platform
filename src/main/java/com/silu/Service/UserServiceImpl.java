package com.silu.Service;

import com.silu.Config.JwtProvider;
import com.silu.Domain.VerificationType;
import com.silu.Exceptions.UserNotFoundException;
import com.silu.Models.TwoFactorAuth;
import com.silu.Models.User;
import com.silu.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserProfileByJwt(String jwt) {
        String email = JwtProvider.getEmailFromJwtToken(jwt);
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found");
        }
        return user;
    }

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("User not found from email");
        }
        return user;
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + userId));
    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType type, String sendTo, User user) {
        TwoFactorAuth  twoFactorAuth =
                TwoFactorAuth.builder()
                        .isEnabled(true)
                        .sendTo(type)
                        .build();
        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }


    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
