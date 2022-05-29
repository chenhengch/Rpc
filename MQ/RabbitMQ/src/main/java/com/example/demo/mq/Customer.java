package com.example.demo.mq;

import com.example.demo.util.RabbitMQConnection;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Customer {

    private final  static  String QUEUE_NAME="Producer";

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {

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
        channel.queueDeclare(QUEUE_NAME ,false ,false,false,null);

        // 定义队列的消费者
        /**
         * 当接收到消息后此方法将被调用
         * @param consumerTag  消费者标签，用来标识消费者的，在监听队列时设置channel.basicConsume
         * @param envelope 信封，通过envelope
         * @param properties 消息属性
         * @param body 消息内容
         * @throws IOException
         */
        DefaultConsumer consumer =new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               String exchange = envelope.getExchange();

               String routKey= envelope.getRoutingKey();
                //消息id，mq在channel中用来标识消息的id，可用于确认消息已接收
               long deliverTag= envelope.getDeliveryTag();
               //exchange: routKey:q_test_01 deliverTag:1   body:[B@6d80c6f7
               System.out.println("exchange:"+exchange+",routKey:"+routKey+",deliverTag:"+deliverTag+",body:"+new String(body));
            }
        };
        // 监听队列
        channel.basicConsume(QUEUE_NAME ,true ,consumer);
    }
}
