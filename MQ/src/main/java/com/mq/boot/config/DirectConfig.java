package com.mq.boot.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Direct Exchange 是RabbitMQ默认的交换机模式，也是最简单的模式，根据key全文匹配去寻找队列。
 */
@Configuration
public class DirectConfig {


    private final  static String QUEUE_HELLO="hello";
    private final  static String QUEUE_SEND="direct";
    @Bean
    public Queue helloQueue(){

        return new Queue(QUEUE_HELLO);
    }

    @Bean
    public Queue sendQueue(){

        return new Queue(QUEUE_SEND);
    }

}