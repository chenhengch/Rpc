package com.example.demo.fanout;

import com.example.demo.util.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author PC
 */
public class Send {
    private static String EXCHANGE_NAME = "ex_fanout";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        String msg = "direct";
        channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());

        channel.close();
        connection.close();
    }
}
