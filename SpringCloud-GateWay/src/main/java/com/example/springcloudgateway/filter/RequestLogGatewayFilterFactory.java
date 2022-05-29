package com.example.springcloudgateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author PC
 * 自定义过滤器
 */
@Component
@Slf4j
public class RequestLogGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {
    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return (ServerWebExchange exchange, GatewayFilterChain chain) -> {
            log.info("请求网关，{}， {}", config.getName(), config.getValue());
            MultiValueMap<String, String> valueMap = exchange.getRequest().getQueryParams();
            valueMap.forEach((k, v) -> {
                log.info("请求参数 {} ", k);

                v.forEach(s -> {
                    log.info("请求参数值 = {} ", s);
                });
            });
            return chain.filter(exchange);
        };

    }
}
