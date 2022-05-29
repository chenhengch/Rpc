package com.mq.boot.producer;


import com.mq.boot.config.TopicConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author PC
 */
@RestController
public class TopicProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @RequestMapping("/topic")
    public void senMsgByTopics() {

        for (int i = 0; i < 5; i++) {

            String message = "恭喜您，注册成功！userid=" + i;

            rabbitTemplate.convertAndSend(TopicConfig.EXCHANGE_NAME, "m.topic.message", message);
           // System.out.println(" [x] Sent '" + message + "'" + rabbitTemplate.getRoutingKey());
        }
    }

}
