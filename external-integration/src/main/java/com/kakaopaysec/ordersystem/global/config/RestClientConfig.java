package com.kakaopaysec.ordersystem.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient krxRestClient() {
        // 면접 방어: 2초 타임아웃 요구사항을 충족하기 위해 하위 수준의 RequestFactory에서 직접 타임아웃을 설정합니다.
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(1000); // 1초
        factory.setReadTimeout(2000);    // 2초 (과제 핵심 제약사항)

        return RestClient.builder()
                .requestFactory(factory)
                .baseUrl("http://localhost:9090") // 가짜 외부 거래소 서버 주소
                .build();
    }
}