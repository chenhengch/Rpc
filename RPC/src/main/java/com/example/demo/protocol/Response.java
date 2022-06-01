package com.example.demo.protocol;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
public class Response implements Serializable {

    private Status status;
    private Object returnValue;
    private Exception exception;
    private Map<String, String> map = new HashMap<>();

}
