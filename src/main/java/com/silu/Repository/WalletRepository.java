package com.silu.Repository;

import com.silu.Models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {

    Wallet findByUserId(long userId);

}
