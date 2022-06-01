package com.example.demo.client.net;

import com.example.demo.discovery.ServiceInfo;
import com.example.demo.discovery.ServiceInfoDiscoverer;
import com.example.demo.protocol.MessageProtocol;
import com.example.demo.protocol.Request;
import com.example.demo.protocol.Response;
import lombok.Data;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Data
public class ClientSubProxyFactory {
    private ServiceInfoDiscoverer serviceInfoDiscoverer;
    private Map<String, MessageProtocol> supportMessageProtocol;
    private NetClient netClient = null;
    private Map<Class<?>, Object> objectMap = new HashMap<>();

    public <T> T getProxy(Class<?> interf) {
        T o = (T) objectMap.get(interf);
        if (o == null) {
            o = (T) Proxy.newProxyInstance(interf.getClassLoader(),
                    new Class[]{interf},
                    new ClientStubInvocationHandler(interf));
            this.objectMap.put(interf, o);

        }
        return o;
    }

    private class ClientStubInvocationHandler implements InvocationHandler {
        private Class<?> interf;

        private Random random = new Random();

        public ClientStubInvocationHandler(Class<?> interf) {
            super();
            this.interf = interf;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("toString")) {
                return proxy.getClass().toString();
            }
            if (method.getName().equals("hashCode")) {
                return 0;
            }
            //得到服务信息
            String serviceName = interf.getName();
            List<ServiceInfo> serviceInfos = serviceInfoDiscoverer.getServiceInfo(serviceName);
            if (serviceInfos == null && serviceInfos.size() == 0) {
                throw new RuntimeException("服务不存在");
            }
            //随机选择一个服务
            ServiceInfo serviceInfo = serviceInfos.get(random.nextInt(serviceInfos.size()));
            //构造请求信息
            Request request = new Request();
            request.setServiceName(serviceName);
            request.setMethod(method.getName());
            request.setParameterTypes(method.getParameterTypes());
            request.setParameters(args);
            //消息编组
            MessageProtocol messageProtocol = supportMessageProtocol.get(serviceInfo.getProtocol());
            byte[] bytes = messageProtocol.marshallingRequest(request);
            byte[] reqBytes = netClient.sendRequest(bytes, serviceInfo);
            Response response = messageProtocol.unmarshallingResponse(reqBytes);
            if (response.getException() != null) {
                throw new RuntimeException();
            }
            return response.getReturnValue();
        }
    }
}
