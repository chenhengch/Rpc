package com.mq.boot.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BackUpConsumer {

    @RabbitListener(queues = "backup.queue")
    public void receiveWarningMsg(Message message) {

        log.error("备份不可路由的消息：{}", new String(message.getBody()));

    }
}
