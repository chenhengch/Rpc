package com.example.demo.register;

import java.util.HashMap;
import java.util.Map;

public class DefaultServiceRegister implements ServiceRegister {
    private Map<String, ServiceObject> map = new HashMap<>();

    @Override
    public void register(ServiceObject serviceObject, String protocol, int port) throws Exception {
        if (serviceObject == null) {
            throw new IllegalArgumentException("参数不能空");
        }
        map.put(serviceObject.getName(), serviceObject);
    }

    @Override
    public ServiceObject getServiceObject(String name) {
        return map.get(name);
    }
}
