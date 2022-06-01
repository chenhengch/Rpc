package com.example.demo.register;

public interface ServiceRegister {

    void register(ServiceObject serviceObject, String protocol, int port) throws Exception;

    ServiceObject getServiceObject(String name);
}
