package com.example.demo.direct;

import com.example.demo.util.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {

    private  final static String EXCHANGE_NAME="test_exchange_direct";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();
        String message = "注册成功！请短信回复[T]退订";
        String email = "注册成功！请email回复[T]退订";
        channel.exchangeDeclare(EXCHANGE_NAME ,"direct");
        /**
         * 参数明细：
         * 1、exchange，交换机，如果不指定将使用mq的默认交换机（设置为""）
         * 2、routingKey，路由key，交换机根据路由key来将消息转发到指定的队列，如果使用默认交换机，routingKey设置为队列的名称
         * 3、props，消息的属性
         * 4、body，消息内容
         */
        channel.basicPublish(EXCHANGE_NAME,"sms",null,message.getBytes());
        channel.basicPublish(EXCHANGE_NAME,"email",null,email.getBytes());

        System.out.println("message:"+message);
        channel.close();
        connection.close();
    }
}
