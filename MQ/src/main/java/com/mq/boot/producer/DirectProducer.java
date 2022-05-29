package com.mq.boot.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectProducer {

    private final  static String QUEUE_HELLO="hello";
    private final  static String QUEUE_Direct="direct";
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/hello")
    public void sendHello(){
        String context = "此消息在，默认的交换机模式队列下，有 helloReceiver 可以收到";
        rabbitTemplate.convertAndSend(QUEUE_HELLO,context);
    }

    @RequestMapping("/direct")
    public void sendDirect(){
        String context = "此消息在，默认的交换机模式队列下，有 directReceiver  可以收到";
        rabbitTemplate.convertAndSend(QUEUE_Direct,context);
    }
}
