package com.example.demo.fanout;

import com.example.demo.util.RabbitMQConnection;
import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv2 {
    private static String EXCHANGE_NAME = "ex_fanout";
    private static String QUEUE_NAME = "recv2";

    public static void main(String[] args) throws Exception {
        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //消息id，mq在channel中用来标识消息的id，可用于确认消息已接收
                long id = envelope.getDeliveryTag();
                System.out.println("CustomerOne:" + new String(body) + ",id:" + id);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, defaultConsumer);
    }
}
