package com.silu.Controller;


import com.silu.Domain.OrderType;
import com.silu.Models.Coin;
import com.silu.Models.Order;
import com.silu.Models.User;
import com.silu.Models.WalletTransaction;
import com.silu.Request.CreateOrderRequest;
import com.silu.Service.CoinService;
import com.silu.Service.OrderService;
import com.silu.Service.UserService;
import com.silu.Service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    @PostMapping("/pay")
    public ResponseEntity<Order> payOrderPayment(
            @RequestHeader("Authorization") String jwt,
            @RequestBody CreateOrderRequest req
    ) {
        User user = userService.findUserProfileByJwt(jwt);

        Coin coin = coinService.finById(req.getCoinId());
        Order order = orderService.processOrder(coin, req.getQuantity(), req.getOrderType(), user);

        return ResponseEntity.ok(order);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrderById(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId
    ) {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.getOrderById(orderId);

        if (order.getUser().getId().equals(user.getId())) {
            return ResponseEntity.ok(order);
        } else {
            throw new RuntimeException("Invalid order id");
        }
    }

    @GetMapping()
    public ResponseEntity<List<Order>> getAllOrdersForUser(
            @RequestHeader("Authorization") String jwt,
            @RequestParam(required = false) OrderType order_type,
            @RequestParam(required = false) String asset_symbol
    ) {
        User user = userService.findUserProfileByJwt(jwt);
        Long userId = user.getId();

        List<Order> userOrder = orderService.getAllOrderOfUser(userId,order_type,asset_symbol);


        return ResponseEntity.ok(userOrder);
    }


}
