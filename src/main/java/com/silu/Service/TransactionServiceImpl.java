package com.silu.Service;

import com.silu.Domain.WalletTransactionType;
import com.silu.Models.Wallet;
import com.silu.Models.WalletTransaction;
import com.silu.Repository.WalletTransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletTransactionRepository walletTransactionRepository;



    @Override
    public List<WalletTransaction> getTransactionsByWallet(Wallet wallet) {
        return walletTransactionRepository.findAll();
    }

    @Override
    public void addTransaction(Wallet wallet,WalletTransaction walletTransaction) {
        walletTransaction.setWallet(wallet);
        walletTransactionRepository.save(walletTransaction);
    }

    @Override
    public WalletTransaction createTransaction(Wallet userWallet, WalletTransactionType walletTransactionType, String tId, String purpose, LocalDateTime date, Long amount) {
        WalletTransaction walletTransaction = WalletTransaction.builder()
                .wallet(userWallet)
                .walletTRansactionType(walletTransactionType)
                .amount(amount)
                .purpose(purpose)
                .transferId(tId)
                .date(date)
                .build();

        return walletTransactionRepository.save(walletTransaction);
    }


//    @Override
//    public List<WalletTransaction> getTransactionsByWallet(Wallet wallet) {
//
//
//        return walletTransactionRepository.findByWalletId(wallet.getId());
//    }
}
