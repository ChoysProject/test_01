package com.kakaopaysec.ordersystem.order.service;

import com.kakaopaysec.ordersystem.external.client.ExternalKrxClient;
import com.kakaopaysec.ordersystem.order.entity.Order;
import com.kakaopaysec.ordersystem.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ExternalKrxClient externalKrxClient;

    public record OrderRequestDto(String userId, String stockCode, int quantity, long price) {}
    public record OrderResponseDto(Long internalOrderId, String status) {}

    @Transactional
    public OrderResponseDto processOrder(OrderRequestDto requestDto) {
        // 1. 주문 초기 저장
        Order order = new Order(requestDto.userId(), requestDto.stockCode(), requestDto.quantity(), requestDto.price());
        orderRepository.save(order);

        try {
            // 2. 외부 연계 (Circuit Breaker 적용 구간)
            ExternalKrxClient.KrxRequest krxReq = new ExternalKrxClient.KrxRequest(
                    requestDto.userId(), requestDto.stockCode(), requestDto.quantity()
            );
            ExternalKrxClient.KrxResponse krxRes = externalKrxClient.sendOrderToKrx(krxReq);

            if (krxRes != null && krxRes.success()) {
                order.complete();
            } else {
                order.fail();
            }
        } catch (Exception e) {
            order.fail();
            throw e; 
        }

        return new OrderResponseDto(order.getId(), order.getStatus().name());
    }
}