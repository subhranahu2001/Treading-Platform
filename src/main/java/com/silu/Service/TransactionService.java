package com.silu.Service;

import com.silu.Domain.WalletTransactionType;
import com.silu.Models.Wallet;
import com.silu.Models.WalletTransaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    List<WalletTransaction> getTransactionsByWallet(Wallet wallet);

    void addTransaction(Wallet wallet ,WalletTransaction walletTransaction);

    WalletTransaction createTransaction(Wallet userWallet, WalletTransactionType walletTransactionType, String tId, String purpose, LocalDateTime date, Long amount);
}
