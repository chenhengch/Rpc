package com.mq.boot.producer;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PC
 */
@RestController
public class FanoutProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @return
     */
    @RequestMapping("/sendMsg")
    public String sendMsg(String msg) {
        /**
         * 1.交换机名称
         * 2.路由key名称
         * 3.发送内容
         */
        rabbitTemplate.convertAndSend("/mayikt_ex", "", msg);
        rabbitTemplate.convertAndSend("exchange_coupon","",msg);
        return "success";
    }
}
