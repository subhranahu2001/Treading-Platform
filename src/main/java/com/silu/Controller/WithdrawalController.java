package com.silu.Controller;


import com.silu.Domain.WalletTransactionType;
import com.silu.Models.User;
import com.silu.Models.Wallet;
import com.silu.Models.WalletTransaction;
import com.silu.Models.Withdrawal;
import com.silu.Service.TransactionService;
import com.silu.Service.UserService;
import com.silu.Service.WalletService;
import com.silu.Service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class WithdrawalController {

    @Autowired
    private WithdrawalService withdrawalService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawalRequest(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long amount
    ) {

        User user = userService.findUserProfileByJwt(jwt);
        Wallet userWallet = walletService.getUserWallet(user);
        Withdrawal withdrawal = withdrawalService.requestWithdrawal(amount,user);
        walletService.addBalance(userWallet,-withdrawal.getAmount());

        WalletTransaction transaction = transactionService.createTransaction(
                userWallet,
                WalletTransactionType.WITHDRAWAL,null,
                "bank account withdrawal",
                withdrawal.getDate(),
                withdrawal.getAmount()
        );

        return ResponseEntity.ok(withdrawal);
    }

    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(
            @RequestHeader("Authorization") String jwt,
            @PathVariable boolean accept,
            @PathVariable Long id) {

        User user = userService.findUserProfileByJwt(jwt);
        Wallet userWallet = walletService.getUserWallet(user);
        Withdrawal withdrawal = withdrawalService.procedWithdrawal(id,accept);
        if (!accept) {
            walletService.addBalance(userWallet,withdrawal.getAmount());
        }



        return ResponseEntity.ok(withdrawal);
    }

    @PostMapping("/api/withdrawal-history")
    public ResponseEntity<?> getWithdrawalHistory(
            @RequestHeader("Authorization") String jwt
    ) {

        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawal = withdrawalService.getUsersWithdrawalHistory(user);

        return ResponseEntity.ok(withdrawal);
    }

    @PostMapping("/api/admin/withdrawal")
    public ResponseEntity<?> getWithdrawalRequest(
            @RequestHeader("Authorization") String jwt
    ) {

        User user = userService.findUserProfileByJwt(jwt);
        List<Withdrawal> withdrawalList = withdrawalService.getAllWithdrawalRequest();

        return ResponseEntity.ok(withdrawalList);
    }







}
