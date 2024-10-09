package com.silu.Request;

import com.silu.Domain.OrderType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderRequest {
    private String coinId;

    private double quantity;

    private OrderType orderType;
}
