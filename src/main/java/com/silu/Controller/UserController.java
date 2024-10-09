package com.silu.Controller;

import com.silu.Domain.VerificationType;
import com.silu.Request.ForgotPasswordTokenRequest;
import com.silu.Models.ForgotPasswordToken;
import com.silu.Models.User;
import com.silu.Models.VerificationCode;
import com.silu.Request.ResetPasswordRequest;
import com.silu.Response.ApiResponse;
import com.silu.Response.AuthResponse;
import com.silu.Service.EmailService;
import com.silu.Service.ForgotPasswordService;
import com.silu.Service.UserService;
import com.silu.Service.VerificationCodeService;
import com.silu.Utils.OtpUtils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
//@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    @Autowired
    private ForgotPasswordService forgotPasswordService;

    @GetMapping("/api/users/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) {

        User user = userService.findUserProfileByJwt(jwt);

        return ResponseEntity.ok(user);

    }
    @PostMapping("/api/users/verification/{verificationType}/send-otp")
    public ResponseEntity<String> sendVerificationOtp(
            @RequestHeader("Authorization") String jwt,
            @PathVariable VerificationType verificationType
    ) throws MessagingException {

        User user = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());
        if (verificationCode == null) {
            verificationCode = verificationCodeService
                    .sendVerificationCode(user,verificationType);
        }

        if (verificationType.equals(VerificationType.EMAIL)) {
            emailService.sentVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }

        return ResponseEntity.ok("verification otp successfully sent");

    }

    @PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
    public ResponseEntity<User> enableTwoFactorAuthentication(
            @RequestHeader("Authorization") String jwt,
            @PathVariable String otp) throws Exception {

        User user = userService.findUserProfileByJwt(jwt);
        VerificationCode verificationCode = verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ?
                verificationCode.getEmail() : verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

        if (isVerified) {
            User updateUser =
                    userService.enableTwoFactorAuthentication(
                            verificationCode.getVerificationType(),
                            sendTo,
                            user);
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return ResponseEntity.ok(updateUser);
        }


        throw new Exception("Wrong OTP");

    }

    @PostMapping("/auth/user/verification/reset-password/send-otp")
    public ResponseEntity<AuthResponse> sendForgotPasswordOTP(
            @RequestBody ForgotPasswordTokenRequest req
            ) throws MessagingException {
        User user = userService.findUserByEmail(req.getSendTo());
        String otp = OtpUtils.generateOTP();
        String id = UUID.randomUUID().toString();

        ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());

        if (token == null) {
            token = forgotPasswordService.createToken(user,id,otp,req.getVerificationType(),req.getSendTo());
        }

        if (req.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sentVerificationOtpEmail(user.getEmail(), token.getOtp());
        }

        AuthResponse response = AuthResponse.builder()
                .session(token.getId())
                .message("Password reset OTP sent successfully")
                .build();

        return ResponseEntity.ok(response);

    }


    @PatchMapping("/auth/users/reset-password/verify-otp")
    public ResponseEntity<ApiResponse> resetPasswordOTP(
            @RequestBody ResetPasswordRequest req,
            @RequestParam String id
            ) throws Exception {

        ForgotPasswordToken token = forgotPasswordService.findById(id);
        boolean isVerified = token.getOtp().equals(req.getOtp());

        if(isVerified) {
            userService.updatePassword(token.getUser(), req.getPassword());
            ApiResponse response = ApiResponse.builder()
                    .message("Password update successfully")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }


        throw new Exception("Wrong OTP");

    }


}
