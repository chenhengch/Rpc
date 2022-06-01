package com.example.demo.discovery;

import com.alibaba.fastjson.JSON;
import com.example.demo.common.MyZkSerializer;
import com.example.demo.util.PropertiesUtil;
import org.I0Itec.zkclient.ZkClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ZkServiceInfoDiscoverer implements ServiceInfoDiscoverer {
    private ZkClient zkClient;

    private String rootPath = "/rpc";

    private String addressKey = "zk.address";

    public ZkServiceInfoDiscoverer() {
        String addr = PropertiesUtil.getValue(addressKey);
        zkClient = new ZkClient(addr);
        zkClient.setZkSerializer(new MyZkSerializer());
    }

    @Override
    public List<ServiceInfo> getServiceInfo(String name) {

        String path = rootPath + "/" + name + "/service";
        List<String> nodes = zkClient.getChildren(path);
        ArrayList<ServiceInfo> serviceInfos = new ArrayList<>();
        for (String node : nodes
        ) {
            try {
                String decode = URLDecoder.decode(node, "utf-8");
                ServiceInfo serviceInfo = JSON.parseObject(decode, ServiceInfo.class);
                serviceInfos.add(serviceInfo);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return serviceInfos;
    }
}
