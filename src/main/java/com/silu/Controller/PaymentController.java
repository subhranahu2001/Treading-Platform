package com.silu.Controller;

import com.razorpay.RazorpayException;
import com.silu.Domain.PaymentMethod;
import com.silu.Models.PaymentOrder;
import com.silu.Models.User;
import com.silu.Response.PaymentResponse;
import com.silu.Service.PaymentService;
import com.silu.Service.UserService;
import com.stripe.exception.StripeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment/{paymentMethod}/amount/{amount}")
    public ResponseEntity<PaymentResponse> paymentHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long amount,
            @PathVariable PaymentMethod paymentMethod
    ) throws RazorpayException, StripeException {
        User user = userService.findUserProfileByJwt(jwt);
        PaymentResponse paymentResponse;

        PaymentOrder order = paymentService.createOrder(user,amount,paymentMethod);

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            paymentResponse = paymentService.createRazorpayPaymentLink(user,amount, order.getId());
        } else {
            paymentResponse = paymentService.createStripPaymentLink(user,amount,order.getId());
        }

        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }


}
