package com.silu.Repository;

import com.silu.Models.Order;
import com.silu.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

        List<Order> findByUserId(long userId);
}
