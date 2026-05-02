package com.kakaopaysec.ordersystem.external.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalKrxClient {

    private final RestClient krxRestClient;

    // DTOs for external exchange communication
    public record KrxRequest(String userId, String stockCode, int quantity) {}
    public record KrxResponse(boolean success, String krxOrderId, String message) {}

    @CircuitBreaker(name = "krxExchange", fallbackMethod = "fallbackOrder")
    public KrxResponse sendOrderToKrx(KrxRequest request) {
        log.info("[External] Sending order to KRX: {}", request.stockCode());
        
        return krxRestClient.post()
                .uri("/external/krx/order")
                .body(request)
                .retrieve()
                .body(KrxResponse.class);
    }

// 수정 후 (시니어 추천 방식)
public String fallbackOrder(Throwable t) {
    log.error("[Resilience] Fallback executed. Cause: {}", t.getMessage());
    // 아무것도 던지지 않거나, 원본 예외를 그대로 던져야 서킷 브레이커가 상태를 관리합니다.
    throw new RuntimeException(t); 
}
}