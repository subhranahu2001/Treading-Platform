package com.silu.Service;

import com.silu.Domain.OrderType;
import com.silu.Models.Coin;
import com.silu.Models.Order;
import com.silu.Models.OrderItem;
import com.silu.Models.User;

import java.util.List;

public interface OrderService {

    Order createOrder(User user, OrderItem orderItem, OrderType orderType);

    Order getOrderById(Long orderId);

    List<Order> getAllOrderOfUser(Long userId,OrderType orderType,String assetSymbol);

    Order processOrder(
            Coin coin,
            double quantity,
            OrderType orderType,
            User user
    );


}
