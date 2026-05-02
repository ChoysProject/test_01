package com.kakaopaysec.ordersystem.order.controller;

import com.kakaopaysec.ordersystem.global.dto.ApiResponse;
import com.kakaopaysec.ordersystem.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderService.OrderResponseDto> createOrder(@RequestBody OrderService.OrderRequestDto request) {
        OrderService.OrderResponseDto response = orderService.processOrder(request);

        log.info("Order created: {}", response);

        return ApiResponse.success(response);
    }
}