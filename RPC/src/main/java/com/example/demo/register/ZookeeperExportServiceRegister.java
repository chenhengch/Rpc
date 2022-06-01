package com.example.demo.register;

import com.alibaba.fastjson.JSON;
import com.example.demo.common.MyZkSerializer;
import com.example.demo.discovery.ServiceInfo;
import com.example.demo.util.PropertiesUtil;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;

public class ZookeeperExportServiceRegister extends DefaultServiceRegister implements ServiceRegister {

    private ZkClient zkClient;
    public String rootPath = "/rpc";

    public ZookeeperExportServiceRegister() {
        String addr = PropertiesUtil.getValue("zk.address");
        zkClient = new ZkClient(addr);
        zkClient.setZkSerializer(new MyZkSerializer());
    }


    @Override
    public void register(ServiceObject serviceObject, String protocol, int port) throws Exception {
        super.register(serviceObject, protocol, port);
        ServiceInfo serviceInfo = new ServiceInfo();
        String hospIp = InetAddress.getLocalHost().getHostAddress();
        String addr = hospIp + ":" + port;
        serviceInfo.setAddress(addr);
        serviceInfo.setName(serviceObject.getInterf().getName());
        serviceInfo.setProtocol(protocol);
        exportService(serviceInfo);
    }

    public void exportService(ServiceInfo serviceInfo) {
        String serviceName = serviceInfo.getName();
        String uri = JSON.toJSONString(serviceInfo);
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
            String servicePath = rootPath + "/" + serviceName + "/service";
            if (!zkClient.exists(servicePath)) {
                zkClient.createPersistent(servicePath, true);
            }
            String uriPath = servicePath + "/" + uri;
            if (zkClient.exists(uriPath)) {
                zkClient.delete(uriPath);
            }
            zkClient.createEphemeral(uriPath);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
