package com.silu.Service;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.silu.Domain.PaymentMethod;
import com.silu.Domain.PaymentOrderStatus;
import com.silu.Models.PaymentOrder;
import com.silu.Models.User;
import com.silu.Repository.PaymentOrderRepository;
import com.silu.Response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;

    @Value("${strip.api.key}")
    private String stripSecreteKey;

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecreteKey;

    @Override
    public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {

        PaymentOrder paymentOrder = PaymentOrder.builder()
                .user(user)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .status(PaymentOrderStatus.PENDING)
                .build();


        return paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) {

        return paymentOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment Order Not Found"));
    }

    @Override
    public Boolean proceedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {

        if (paymentOrder.getStatus() == null) {
            paymentOrder.setStatus(PaymentOrderStatus.PENDING);
        }

        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)){
                RazorpayClient razorpayClient = new RazorpayClient(apiKey,apiSecreteKey);
                Payment payment = razorpayClient.payments.fetch(paymentId);
                Integer amount = payment.get("amount");
                String status = payment.get("status");
                if (status.equals("captured")) {
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    return true;
                }
                paymentOrder.setStatus(PaymentOrderStatus.FAILED);
                paymentOrderRepository.save(paymentOrder);
                return false;
            }
            paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
            paymentOrderRepository.save(paymentOrder);
            return true;
        }
        return false;
    }

    @Override
    public PaymentResponse createRazorpayPaymentLink(User user, Long amount,Long orderId) throws RazorpayException {

        Long Amount = amount * 100;

        try {
            // Instance a razorpay client with your key id and secret
            RazorpayClient razorpay = new RazorpayClient(apiKey,apiSecreteKey);

            //Create a JSON object with the payment link request parameters

            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount",Amount);
            paymentLinkRequest.put("currency","INR");

            //Create a Json Object with the customer details

            JSONObject customer = new JSONObject();
            customer.put("email",user.getEmail());

            paymentLinkRequest.put("customer",customer);

            //create a Json object with the notification setting
            JSONObject notify = new JSONObject();
            notify.put("email",true);
            paymentLinkRequest.put("notify",notify);

            //set the remainder setting
            paymentLinkRequest.put("reminder_enable",true);

            //set the callback url and method
            paymentLinkRequest.put("callback_url","http://localhost:9095/wallet?order_id="+orderId);
            paymentLinkRequest.put("callback_method","get");

            //create payment link using the paymentLink.create() method
            PaymentLink payment =razorpay.paymentLink.create(paymentLinkRequest);

            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentResponse res = PaymentResponse.builder()
                    .payment_url(paymentLinkUrl)
                    .build();

            return res;
        } catch (RazorpayException e) {

            log.error("Error creating payment link : {}",  e.getMessage());
            throw new RazorpayException(e.getMessage());
        }


    }

    @Override
    public PaymentResponse createStripPaymentLink(User user, Long amount, Long orderId) throws StripeException {

        Stripe.apiKey = stripSecreteKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:9095/wallet?order_id="+orderId)
                .setCancelUrl("http://localhost:9095/payment/cancel")
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(amount*100)
                                .setProductData(SessionCreateParams
                                        .LineItem
                                        .PriceData
                                        .ProductData
                                        .builder()
                                        .setName("Top up wallet")
                                        .build()
                                ).build()
                        ).build()
                ).build();

        Session session = Session.create(params);

        log.info("Session created: {}", session);

        PaymentResponse res = PaymentResponse.builder()
                .payment_url(session.getUrl())
                .build();

        return res;
    }
}
