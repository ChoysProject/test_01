package com.kakaopaysec.ordersystem.global.exception;

import com.kakaopaysec.ordersystem.global.dto.ApiResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.TimeoutException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 외부 API 연동 타임아웃 (2초 초과 시)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(TimeoutException.class)
    public ApiResponse<Void> handleTimeoutException(TimeoutException e) {
        log.error("외부 연동 타임아웃: {}", e.getMessage());
        return ApiResponse.error("EXTERNAL_TIMEOUT", "거래소 응답이 지연되고 있습니다.");
    }

    // 서킷 브레이커 작동 (차단 상태일 때)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(CallNotPermittedException.class)
    public ApiResponse<Void> handleCircuitBreakerException(CallNotPermittedException e) {
        log.error("서킷 브레이커 작동중: {}", e.getMessage());
        return ApiResponse.error("CIRCUIT_OPEN", "현재 시스템 보호를 위해 외부 연결이 일시 차단되었습니다.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleGeneralException(Exception e) {
        log.error("서버 내부 에러: {}", e.getMessage(), e);
        return ApiResponse.error("INTERNAL_ERROR", "시스템 오류가 발생했습니다.");
    }
}