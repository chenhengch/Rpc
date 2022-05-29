package com.example.springcloudgateway.publish;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * @author PC
 */
@Component
public class DynamicRouteServiceImplNacos {

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;
    private String dataId = "gateway-router";

    private String group = "DEFAULT_GROUP";

    @Autowired
    private DynamicRouteServiceImpl dynamicRouteService;

    public DynamicRouteServiceImplNacos() {
        dynamicRouteByNacosLister(dataId, group);
    }

    public void dynamicRouteByNacosLister(String dateId, String group) {
        try {
            ConfigService configService = NacosFactory.createConfigService("127.0.0.1:8848");
            String config = configService.getConfig(dateId, group, 5000);
            System.out.println(config);
            configService.addListener(dateId, group, new Listener() {
                @Override
                public Executor getExecutor() {
                    return null;
                }

                @Override
                public void receiveConfigInfo(String configInfo) {
                    RouteDefinition definition = JSON.parseObject(configInfo, RouteDefinition.class);
                    dynamicRouteService.update(definition);
                }
            });
        } catch (NacosException e) {
            e.printStackTrace();
        }
    }
}
