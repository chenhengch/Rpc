package com.example.demo.dead;

import com.example.demo.util.RabbitMQConnection;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Consumer02 {

    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMQConnection.getConnection().createChannel();

        channel.queueDeclare("dead_queue", true, false, false, null);
        channel.exchangeDeclare(DEAD_EXCHANGE, "direct");
        channel.queueBind("dead_queue", DEAD_EXCHANGE, "dead");
        System.out.println("等待接收死信队列消息.....");
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("Consumer02死信接收的消息:" + new String(body));
            }
        };
        channel.basicConsume("dead_queue", true, defaultConsumer);
    }
}
