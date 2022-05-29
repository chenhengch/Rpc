package com.example.demo.direct;

import com.example.demo.util.RabbitMQConnection;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {

    private  final static String EXCHANGE_NAME="test_exchange_direct";

    private  final static String QUEUE_NAME="test.exchange.direct.sms";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel =connection.createChannel();
        /**
         * 参数明细
         * 1、queue 队列名称
         * 2、durable 是否持久化，如果持久化，mq重启后队列还在
         * 3、exclusive 是否独占连接，队列只允许在该连接中访问，如果connection连接关闭队列则自动删除,如果将此参数设置true可用于临时队列的创建
         * 4、autoDelete 自动删除，队列不再使用时是否自动删除此队列，如果将此参数和exclusive参数设置为true就可以实现临时队列（队列不用了就自动删除）
         * 5、arguments 参数，可以设置一个队列的扩展参数，比如：可设置存活时间
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        /**
         *  param1：destination，目的地，队列的名字
         *  param2：source，资源，交换机的名字
         *  param3：routingKey，路由键（目前没有用到routingKey，填 "" 即可）
         */
        // 绑定队列到交换机，同时指定需要订阅的routing key。可以指定多个
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"sms");
        //指定接收发送方指定routing key为sms的消息

        DefaultConsumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                // body 即消息体
                String msg = new String(body);
                System.out.println(" [短信服务] received : " + msg + "!");

            }
        };
        channel.basicConsume(QUEUE_NAME,true,consumer);
    }
}
