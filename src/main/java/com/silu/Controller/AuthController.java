package com.silu.Controller;

import com.silu.Config.JwtProvider;
import com.silu.Models.TwoFactorOTP;
import com.silu.Models.User;
import com.silu.Models.WatchList;
import com.silu.Repository.UserRepository;
import com.silu.Response.AuthResponse;
import com.silu.Service.CustomUserDetailService;
import com.silu.Service.EmailService;
import com.silu.Service.TwoFactorOtpService;
import com.silu.Service.WatchListService;
import com.silu.Utils.OtpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private WatchListService watchListService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {

        User isEmailExist = userRepository.findByEmail(user.getEmail());
        if (isEmailExist != null) {
            throw new RuntimeException("email is already in use");
        }
        User newUser = new User();
        newUser.setFullName(user.getFullName());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        User savedUser = userRepository.save(newUser);

        watchListService.createWatchList(savedUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = JwtProvider.generateToken(authentication);

        AuthResponse authResponse = AuthResponse.builder()
                .jwt(jwt)
                .status(true)
                .message("Register success")
                .build();

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        String username = user.getEmail();
        String password = user.getPassword();
        Authentication auth = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);

        User authUser = userRepository.findByEmail(user.getEmail());

        // This code is for two-factor authentication
        if (user.getTwoFactorAuth().isEnabled()) {
            AuthResponse res = AuthResponse.builder()
                    .message("Two factor auth is enabled")
                    .isTwoFactorAuthEnabled(true)
                    .build();
            String otp = OtpUtils.generateOTP();
            TwoFactorOTP oldTwoFactorService =
                    twoFactorOtpService.findByUser(authUser.getId());
            if (oldTwoFactorService != null) {
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorService);
            }

            TwoFactorOTP newTwofactorOtp = twoFactorOtpService.createTwoFactorOtp(authUser, otp, jwt);

            try {
                emailService.sentVerificationOtpEmail(username, otp);
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            res.setSession(newTwofactorOtp.getId());
            return new ResponseEntity<>(res, HttpStatus.ACCEPTED);

        }

        AuthResponse authResponse =
                AuthResponse.builder()
                        .jwt(jwt)
                        .status(true)
                        .message("Login success")
                        .build();

        return new ResponseEntity<>(authResponse, HttpStatus.OK);

    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customUserDetailService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!password.equals(userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySignInOTP(@PathVariable String otp, @RequestParam String id) throws Exception {
        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findById(id);
        if (twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP, otp)) {
            AuthResponse response = AuthResponse.builder()
                    .message("Two factor authentication verified")
                    .isTwoFactorAuthEnabled(true)
                    .jwt(twoFactorOTP.getJwt())
                    .build();

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        throw new Exception("Invalid OTP");
    }

}
