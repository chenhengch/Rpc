package com.example.demo.main;

import com.example.demo.client.net.ClientSubProxyFactory;
import com.example.demo.client.net.NettyNetClient;
import com.example.demo.discovery.ZkServiceInfoDiscoverer;
import com.example.demo.protocol.JdkSerializeMessageProtocol;
import com.example.demo.protocol.MessageProtocol;

import java.util.HashMap;
import java.util.Map;

public class Consume {

    public static void main(String[] args) {
        ClientSubProxyFactory factory = new ClientSubProxyFactory();
        factory.setServiceInfoDiscoverer(new ZkServiceInfoDiscoverer());
        Map<String, MessageProtocol> map = new HashMap<>();
        map.put("jdks", new JdkSerializeMessageProtocol());
        factory.setSupportMessageProtocol(map);
        factory.setNetClient(new NettyNetClient());

        DemoService demoService = factory.getProxy(DemoService.class);
        String result = demoService.sayHello("hello");
        System.out.println("result:" + result);
    }
}
