package com.example.demo.protocol;

public interface MessageProtocol {

    byte[] marshallingRequest(Request request) throws Exception;

    Request unmarshallingRequest(byte[] bytes) throws Exception;

    byte[] marshallingResponse(Response request) throws Exception;

    Response unmarshallingResponse(byte[] bytes) throws Exception;

}
