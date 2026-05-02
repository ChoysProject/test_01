package com.kakaopaysec.ordersystem.order.repository;

import com.kakaopaysec.ordersystem.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}