package com.example.demo.server;

import com.example.demo.protocol.MessageProtocol;
import com.example.demo.protocol.Request;
import com.example.demo.protocol.Response;
import com.example.demo.protocol.Status;
import com.example.demo.register.ServiceObject;
import com.example.demo.register.ServiceRegister;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestHandler {

    private MessageProtocol messageProtocol;
    private ServiceRegister serviceRegister;

    public byte[] handlerRequest(byte[] data) throws Exception {
        //解组消息
        Request request = messageProtocol.unmarshallingRequest(data);
        ServiceObject serviceObject = serviceRegister.getServiceObject(request.getServiceName());

        Response response;
        if (serviceObject == null) {
            response = new Response();
            response.setStatus(Status.NOT_FOUND);
        } else {
            try {
                Method method = serviceObject.getInterf().getMethod(request.getMethod(), request.getParameterTypes());
                Object invoke = null;

                invoke = method.invoke(serviceObject.getObject(), request.getParameters());
                response = new Response();
                response.setStatus(Status.SUCCESS);
                response.setReturnValue(invoke);
            } catch (Exception e) {
                e.printStackTrace();
                response = new Response();
                response.setStatus(Status.ERROR);
                response.setException(e);
            }
        }
        return messageProtocol.marshallingResponse(response);
    }
}
