package com.silu.Service;

import com.silu.Domain.VerificationType;
import com.silu.Models.User;

public interface UserService {
    public User findUserProfileByJwt(String jwt);

    public User findUserByEmail(String email);

    public User findUserById(Long userId);

    public User enableTwoFactorAuthentication(
            VerificationType type,
            String sendTo,
            User user);

    User updatePassword(User user, String newPassword);

}
