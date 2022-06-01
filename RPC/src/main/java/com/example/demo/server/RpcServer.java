package com.example.demo.server;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class RpcServer {

    protected String protocol;
    protected int port;
    protected RequestHandler handler;

    public abstract void start();

    public abstract void stop();
}
