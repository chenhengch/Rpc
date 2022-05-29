package com.example.springcloudgateway.factory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author PC
 */
@Component
@Slf4j
public class AccessTimeRoutePredicateFactory extends AbstractRoutePredicateFactory<MyAccessTime> {

    public AccessTimeRoutePredicateFactory() {
        super(MyAccessTime.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("start", "end");
    }

    @Override
    public Predicate<ServerWebExchange> apply(MyAccessTime config) {
        LocalTime start = config.getStart();
        LocalTime end = config.getEnd();

        return serverWebExchange -> {
            LocalTime now = LocalTime.now();
            return now.isAfter(start) && now.isBefore(end);
        };
    }
}
