package com.example.demo.protocol;

import java.io.*;

/**
 * 基于jdk序列化协议 ，我们也可以基于json序列化实现消息协议 http协议
 * jdk系列化时，被序列化的对象必须实现序列化接口 其内的属性也必须实现
 */
public class JdkSerializeMessageProtocol implements MessageProtocol {
    @Override
    public byte[] marshallingRequest(Request request) throws Exception {
        return serializable(request);
    }

    @Override
    public Request unmarshallingRequest(byte[] bytes) throws Exception {
        return (Request) unserializable(bytes);
    }

    @Override
    public byte[] marshallingResponse(Response response) throws Exception {
        return serializable(response);
    }

    @Override
    public Response unmarshallingResponse(byte[] bytes) throws Exception {
        return (Response) unserializable(bytes);
    }

    /**
     * 序列化
     *
     * @return
     */
    public byte[] serializable(Object obj) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        return bos.toByteArray();
    }

    /**
     * 反序列化
     *
     * @return
     */
    public Object unserializable(byte[] bytes) throws ClassNotFoundException, IOException {
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
        return ois.readObject();
    }
}
