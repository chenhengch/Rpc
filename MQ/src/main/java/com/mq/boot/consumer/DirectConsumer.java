package com.mq.boot.consumer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author PC
 */
@Slf4j
@Component
public class DirectConsumer {

    @RabbitHandler
    @RabbitListener(queues = "hello")
    public void helloMessage(String message) {

        System.out.println("helloMessage:" + message);
    }

    @RabbitListener(queues = "direct")
    @RabbitHandler
    public void sendMessage(String message) {
        System.out.println("directMessage:" + message);

    }
}
