package com.silu.Service;

import com.silu.Models.PaymentDetails;
import com.silu.Models.User;
import com.silu.Repository.PaymentDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PaymentDetailsServiceImpl implements PaymentDetailsService {


    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNo, String accountHolder, String ifsc, String bankName, User user) {
        PaymentDetails paymentDetails = PaymentDetails.builder()
                .user(user)
                .accountNo(accountNo)
                .bankName(bankName)
                .ifscCode(ifsc)
                .accountHolderName(accountHolder)
                .build();
        return paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return paymentDetailsRepository.findByUserId(user.getId());
    }

    @Override
    public void deleteUserPaymentDetails(User user) {
        PaymentDetails paymentDetails = paymentDetailsRepository.findByUserId(user.getId());
        paymentDetailsRepository.delete(paymentDetails);
    }
}
