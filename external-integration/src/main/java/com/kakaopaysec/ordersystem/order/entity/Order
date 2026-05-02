package com.kakaopaysec.ordersystem.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders") // order는 SQL 예약어이므로 테이블명을 orders로 지정합니다.
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String stockCode;
    private int quantity;
    private long price;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status; // REQUESTED, COMPLETED, FAILED

    public Order(String userId, String stockCode, int quantity, long price) {
        this.userId = userId;
        this.stockCode = stockCode;
        this.quantity = quantity;
        this.price = price;
        this.status = OrderStatus.REQUESTED;
    }

    public void complete() { this.status = OrderStatus.COMPLETED; }
    public void fail() { this.status = OrderStatus.FAILED; }

    public enum OrderStatus { REQUESTED, COMPLETED, FAILED }
}