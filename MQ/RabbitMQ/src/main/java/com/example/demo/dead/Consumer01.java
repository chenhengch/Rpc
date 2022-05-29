package com.example.demo.dead;

import com.example.demo.util.RabbitMQConnection;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Consumer01 {

    //普通交换机名称
    private static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机名称
    private static final String DEAD_EXCHANGE = "dead_exchange";

    public static void main(String[] argv) throws Exception {
        Channel channel = RabbitMQConnection.getConnection().createChannel();
        //设置参数
        Map<String, Object> params = new HashMap<>();
        //正常队列设置死信交换机 参数 key 是固定值
        params.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        //正常队列设置死信 routing-key 参数 key 是固定值
        params.put("x-dead-letter-routing-key", "dead");
        params.put("x-max-length", 1);

        channel.exchangeDeclare(DEAD_EXCHANGE,"direct");
        channel.queueDeclare("dead_queue",true,false,false,null);
        channel.queueBind("dead_queue",DEAD_EXCHANGE,"dead");

        channel.queueDeclare("normal_queue",true,false,false,params);
        channel.queueBind("normal_queue",NORMAL_EXCHANGE,"normal");

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                System.out.println("Consumer01接收的消息:"+new String(body));
            }
        };
        channel.basicConsume("normal_queue",true,defaultConsumer);
    }

}
