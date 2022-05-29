package com.example.demo.topics;

import com.example.demo.util.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * topic
 * #：匹配一个或多个词
 *
 * *：匹配不多不少恰好1个词
 */
public class Send {

    private  final  static  String EXCHANGE_NAME="test_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMQConnection.getConnection();
        Channel channel =connection.createChannel();
        //交换机持久化 exchangeDeclare(EXCHANGE_NAME,"topic",true);
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        String message ="删除商品";

        /**
         * 参数明细：
         * 1、exchange，交换机，如果不指定将使用mq的默认交换机（设置为""）
         * 2、routingKey，路由key，交换机根据路由key来将消息转发到指定的队列，如果使用默认交换机，routingKey设置为队列的名称
         * 3、props，消息的属性  MessageProperties.PERSISTENT_TEXT_PLAIN  消息持久化
         * 4、body，消息内容
         */
        channel.basicPublish(EXCHANGE_NAME,"routekey.1", null,message.getBytes());

        System.out.println("sent:"+message);
        channel.close();
        connection.close();
    }
}
