package com.mq.boot.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarnConsumer {

    @RabbitListener(queues = "warning.queue")
    public void receiveWarningMsg(Message message){

        log.error("报警发现不可路由消息：{}", new String(message.getBody()));

    }
}
