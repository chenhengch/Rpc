package com.example.springcloudgateway.factory;

import lombok.Data;

import java.time.LocalTime;

/**
 * @author PC
 */
@Data
public class MyAccessTime {

    private LocalTime start;
    private LocalTime end;
}
