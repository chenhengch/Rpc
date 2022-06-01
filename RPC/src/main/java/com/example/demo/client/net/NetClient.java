package com.example.demo.client.net;

import com.example.demo.discovery.ServiceInfo;

public interface NetClient {

    byte[] sendRequest(byte[] bytes, ServiceInfo serviceInfo) throws Throwable;
}