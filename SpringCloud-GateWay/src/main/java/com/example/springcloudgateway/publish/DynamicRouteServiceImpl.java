package com.example.springcloudgateway.publish;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    //添加路由
    public String add(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition));
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }

    //更新路由
    public String update(RouteDefinition definition) {
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
        } catch (Exception e) {
            return "update fail , not find route routeId:" + definition.getId();
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        } catch (Exception e) {
            return "update route fail";
        }
    }

    public Mono<ResponseEntity<Object>> delete(String id) {
        return this.routeDefinitionWriter.delete(
                Mono.just(id)).
                then(Mono.defer(() ->Mono.just(ResponseEntity.ok().build())))
                .onErrorResume(t -> t instanceof NotFoundException ,
                        t -> Mono.just(ResponseEntity.notFound().build()));
    }

    @Data
    class GatewayRouteDefinition {

        private String id;
        private String uri;
        private int order = 0;
        private List<GatewayPredicateDefinition> priest = new ArrayList<>();
        private List<GatewayFilterDefinition> filters = new ArrayList<>();
    }

    //创建路由断言 定义，模型
    @Data
    class GatewayPredicateDefinition {

        private String name;
        private Map<String, String> args = new LinkedHashMap<>();
    }

    //创建过滤器定义模型
    @Data
    class GatewayFilterDefinition {

        private String name;
        private Map<String, String> args = new LinkedHashMap<>();
    }

}
