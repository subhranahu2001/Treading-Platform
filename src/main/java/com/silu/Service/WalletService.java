package com.silu.Service;

import com.silu.Models.Order;
import com.silu.Models.User;
import com.silu.Models.Wallet;

public interface WalletService {

    Wallet getUserWallet(User user);

    Wallet addBalance(Wallet wallet,Long money);

    Wallet findById(Long id);

    Wallet walletToWalletTransfer(User sender,Wallet reciverWallet,Long amount);

    Wallet payOrderPayment(Order order,User user);
}
