package com.silu.Controller;

import com.razorpay.RazorpayException;
import com.silu.Models.*;
import com.silu.Response.PaymentResponse;
import com.silu.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TransactionService transactionService;



    @GetMapping
    public ResponseEntity<Wallet> getUserWallet(
            @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);
        return ResponseEntity.ok(wallet);
    }

    @PutMapping("/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long walletId,
            @RequestBody WalletTransaction req
    ) {
        User senderUser = userService.findUserProfileByJwt(jwt);
        Wallet reciverWallet = walletService.findById(walletId);

        Wallet wallet = walletService
                .walletToWalletTransfer(
                        senderUser,
                        reciverWallet,
                        req.getAmount());
        transactionService.addTransaction(wallet,req);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }


    @PutMapping("/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId
    ) {
        User user = userService.findUserProfileByJwt(jwt);

        Order order = orderService.getOrderById(orderId);
        Wallet wallet = walletService.payOrderPayment(order, user);


        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/deposit")
    public ResponseEntity<Wallet> addMoneyToWallet(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(name = "order_id") Long orderId,
            @RequestParam(name ="payment_id") String paymentId

    ) throws RazorpayException {
        User user = userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        PaymentOrder paymentOrder = paymentService.getPaymentOrderById(orderId);

        Boolean status = paymentService.proceedPaymentOrder(paymentOrder,paymentId);
        if (wallet.getBalance() == null) {
            wallet.setBalance(BigDecimal.valueOf(0));
        }

        if (status) {
            wallet = walletService.addBalance(wallet, paymentOrder.getAmount());
        }

        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }


}
