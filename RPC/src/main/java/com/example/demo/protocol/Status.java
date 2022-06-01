package com.example.demo.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Status {

    SUCCESS(200, "SUCCESS"),
    ERROR(500, "ERROR"),
    NOT_FOUND(404, "NOT_FOUND");

    private int code;
    private String message;

}
