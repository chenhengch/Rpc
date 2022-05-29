package com.example.demo.topics;

import com.example.demo.util.RabbitMQConnection;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {
    private  final  static  String EXCHANGE_NAME="test_exchange_topic";
    private  final  static  String QUEUE_NAME="test_queue_topic_1";

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {

        Connection connection = RabbitMQConnection.getConnection();
        Channel channel = connection.createChannel();

        //声明队列  队列持久化  b:true
        channel.queueDeclare(QUEUE_NAME ,false,false,false,null);
        //绑定队列到交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"routekey.*");
        // 定义队列的消费者
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message= new String(body);
                System.out.println("recv111:"+message);
            }
        };
        // 监听队列，手动返回完成
        channel.basicConsume(QUEUE_NAME,true,defaultConsumer);

    }
}
