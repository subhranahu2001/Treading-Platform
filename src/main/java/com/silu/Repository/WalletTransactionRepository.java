package com.silu.Repository;

import com.silu.Models.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionRepository extends JpaRepository<WalletTransaction,Long> {


//    WalletTransaction findByWalletId(Long id);
}
