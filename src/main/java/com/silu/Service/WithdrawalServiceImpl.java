package com.silu.Service;

import com.silu.Domain.WithdrawalStatus;
import com.silu.Models.User;
import com.silu.Models.Withdrawal;
import com.silu.Repository.WithdrawalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    @Autowired
    private WithdrawalRepository withdrawalRepository;


    @Override
    public Withdrawal requestWithdrawal(Long amount, User user) {
        Withdrawal withdrawal = Withdrawal.builder()
                .amount(amount)
                .user(user)
                .withdrawalStatus(WithdrawalStatus.PENDING)
                .date(LocalDateTime.now())
                .build();
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public Withdrawal procedWithdrawal(Long withdrawalId, boolean accept) {

        Withdrawal withdrawal = withdrawalRepository.findById(withdrawalId)
                .orElseThrow(() -> new RuntimeException("Withdrawal not found"));
        withdrawal.setDate(LocalDateTime.now());
        if (accept) {
            withdrawal.setWithdrawalStatus(WithdrawalStatus.SUCCESS);
        } else {
            withdrawal.setWithdrawalStatus(WithdrawalStatus.PENDING);
        }
        return withdrawalRepository.save(withdrawal);
    }

    @Override
    public List<Withdrawal> getUsersWithdrawalHistory(User user) {
        return withdrawalRepository.findByUserId(user.getId());
    }

    @Override
    public List<Withdrawal> getAllWithdrawalRequest() {
        return withdrawalRepository.findAll();
    }
}
