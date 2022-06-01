package com.example.demo.main;

public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String param) {
        return param + " world";
    }
}
