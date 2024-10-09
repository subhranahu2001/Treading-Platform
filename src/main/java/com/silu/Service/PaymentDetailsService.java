package com.silu.Service;

import com.silu.Models.PaymentDetails;
import com.silu.Models.User;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface PaymentDetailsService {

    PaymentDetails addPaymentDetails(String accountNo, String accountHolder, String ifsc, String bankName, User user);

    PaymentDetails getUserPaymentDetails(User user);

    void deleteUserPaymentDetails(User user);
}
