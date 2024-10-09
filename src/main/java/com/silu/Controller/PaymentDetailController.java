package com.silu.Controller;

import com.silu.Models.PaymentDetails;
import com.silu.Models.User;
import com.silu.Service.PaymentDetailsService;
import com.silu.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentDetailController {
    @Autowired
    private UserService userService;

    @Autowired
    private PaymentDetailsService paymentDetailsService;

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(
            @RequestHeader("Authorization") String jwt,
            @RequestBody PaymentDetails paymentDetailsReq
    ) {

        User user = userService.findUserProfileByJwt(jwt);
        PaymentDetails paymentDetails = paymentDetailsService.addPaymentDetails(
                                            paymentDetailsReq.getAccountNo(),
                                            paymentDetailsReq.getAccountHolderName(),
                                            paymentDetailsReq.getIfscCode(),
                                            paymentDetailsReq.getBankName(),
                                            user
                                        );
        return ResponseEntity.ok(paymentDetails);
    }

    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails> getUserPaymentDetails(
            @RequestHeader("Authorization") String jwt

    ) {
        User user = userService.findUserProfileByJwt(jwt);
        return ResponseEntity.ok(paymentDetailsService.getUserPaymentDetails(user));
    }

    @DeleteMapping("/payment-details/remove")
    public ResponseEntity<PaymentDetails> removePaymentDetails(
            @RequestHeader("Authorization") String jwt
    ) {
        User user = userService.findUserProfileByJwt(jwt);
        paymentDetailsService.deleteUserPaymentDetails(user);

        return ResponseEntity.ok(paymentDetailsService.getUserPaymentDetails(user));
    }


}
