package com.silu.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

    private String payment_url;

}