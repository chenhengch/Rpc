package com.example.springcloudgateway.factory;

import com.alibaba.nacos.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author PC
 */
@Component
@Slf4j
public class TokenRoutePredicateFactory extends AbstractRoutePredicateFactory<MyTokenConfig> {

    public TokenRoutePredicateFactory() {
        super(MyTokenConfig.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Collections.singletonList("token");
    }

    @Override
    public Predicate<ServerWebExchange> apply(MyTokenConfig config) {
        return exchange -> {
            MultiValueMap<String, String> valueMap = exchange.getRequest().getQueryParams();
            boolean flag = true;
            List<String> list = new ArrayList();
            valueMap.forEach((k, v) -> {
                list.add(k);
            });

            for (String s : list) {
                if (StringUtils.equalsIgnoreCase(s,config.getToken())){
                    flag=true;
                    break;
                }
            }
            return flag;
        };
    }
}
