package com.example.demo.register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ServiceObject {

    private String name;
    private Class<?> interf;
    private Object object;

}
