package com.example.demo.dead;

import com.example.demo.util.RabbitMQConnection;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;


public class Producer {
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQConnection.getConnection().createChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE,"direct");


        for (int i = 1; i < 10; i++) {
            String msg="老六"+i;
            channel.basicPublish("NORMAL_EXCHANGE","normal",null,msg.getBytes());
        }

        //设置过期时间
        //AMQP.BasicProperties properties =new AMQP.BasicProperties().builder().expiration("10000").build();

    }
}
