package com.mq.boot.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

/**
 * @author PC
 */
@Slf4j
@Component
public class OrderDlxConsumer {
    /**
     * 死信队列监听队列回调的方法
     *
     */
    @RabbitListener(queues = "my_dlx_queue")
    public void orderConsumer(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到死信队列信息{}", new Date().toString(), msg);
    }
}
