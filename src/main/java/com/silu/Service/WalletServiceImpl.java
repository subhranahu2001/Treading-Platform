package com.silu.Service;

import com.silu.Domain.OrderType;
import com.silu.Models.Order;
import com.silu.Models.User;
import com.silu.Models.Wallet;
import com.silu.Repository.WalletRepository;
import com.silu.Repository.WalletTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;


    @Override
    public Wallet getUserWallet(User user) {
        Wallet wallet = walletRepository.findByUserId(user.getId());
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            walletRepository.save(wallet);
        }
        return wallet;
    }

    @Override
    public Wallet addBalance(Wallet wallet, Long money) {
        BigDecimal newBalance = wallet.getBalance().add(BigDecimal.valueOf(money));
        wallet.setBalance(newBalance);


        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findById(Long id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet reciverWallet, Long amount) {

        Wallet senderWallet = getUserWallet(sender);

        //check the amount is less than TOTAL amount or not.
        if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        BigDecimal senderBalance = senderWallet.getBalance()
                .subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(senderBalance);
        walletRepository.save(senderWallet);

        BigDecimal receiverBalance = reciverWallet.getBalance()
                .add(BigDecimal.valueOf(amount));
        reciverWallet.setBalance(receiverBalance);
        walletRepository.save(reciverWallet);


        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) {
        Wallet wallet = getUserWallet(user);

        BigDecimal newBalance;
        if (order.getOrderType().equals(OrderType.BUY)) {
            newBalance = wallet.getBalance()
                    .subtract(order.getPrice());
            if (newBalance.compareTo(order.getPrice()) < 0) {
                throw new RuntimeException("Insufficient funds");
            }
        }
        else {
            newBalance = wallet.getBalance()
                    .add(order.getPrice());
        }
        wallet.setBalance(newBalance);

        return walletRepository.save(wallet);
    }
}
