package com.silu.Service;

import com.razorpay.RazorpayException;
import com.silu.Domain.PaymentMethod;
import com.silu.Models.PaymentOrder;
import com.silu.Models.User;
import com.silu.Response.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {

    PaymentOrder createOrder(User user,
                             Long amount,
                             PaymentMethod paymentMethod);

    PaymentOrder getPaymentOrderById(Long id);

    Boolean proceedPaymentOrder(PaymentOrder paymentOrder,String paymentId) throws RazorpayException;

    PaymentResponse createRazorpayPaymentLink(User user, Long amount,Long orderId) throws RazorpayException;

    PaymentResponse createStripPaymentLink(User user, Long amount,Long orderId) throws StripeException;

}
