package com.example.demo.main;

import com.example.demo.protocol.JdkSerializeMessageProtocol;
import com.example.demo.register.ServiceObject;
import com.example.demo.register.ZookeeperExportServiceRegister;
import com.example.demo.server.NettyRpcServer;
import com.example.demo.server.RequestHandler;
import com.example.demo.server.RpcServer;
import com.example.demo.util.PropertiesUtil;

public class Provider {

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(PropertiesUtil.getValue("rpc.port"));
        String protocol = PropertiesUtil.getValue("rpc.protocol");
        ZookeeperExportServiceRegister zk = new ZookeeperExportServiceRegister();
        DemoService demoService = new DemoServiceImpl();
        ServiceObject serviceObject = new ServiceObject(DemoService.class.getName(), DemoService.class, demoService);
        zk.register(serviceObject, protocol, port);

        RequestHandler handler = new RequestHandler(new JdkSerializeMessageProtocol(), zk);
        RpcServer rpcServer = new NettyRpcServer(port, protocol, handler);
        rpcServer.start();
        System.in.read();
        rpcServer.stop();
    }
}
