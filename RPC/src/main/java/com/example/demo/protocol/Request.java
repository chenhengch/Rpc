package com.example.demo.protocol;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class Request implements Serializable {

    private String serviceName;

    private String method;

    private Map<String, String> headers = new HashMap<>();

    private Class<?>[] parameterTypes;

    private Object[] parameters;
}
