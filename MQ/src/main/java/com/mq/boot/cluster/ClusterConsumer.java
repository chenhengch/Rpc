package com.mq.boot.cluster;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component

public class ClusterConsumer implements RabbitTemplate.ReturnsCallback {
    /**
     * 通过设置 mandatory 参
     * 数可以在当消息传递过程中不可达目的地时将消息返回给生产者
     */
    @Override
    public void returnedMessage(ReturnedMessage returned) {
        log.info("消息:{}被服务器退回，退回原因:{}, 交换机是:{}, 路由 key:{}",
                new String(returned.getMessage().getBody()),returned.getReplyText(), returned.getExchange(), returned.getRoutingKey());
    }

    @RabbitHandler
    @RabbitListener(queues = "mirrior_common_queue")
    public void getMessage(Message message) {
        String msg = new String(message.getBody());
        log.info("接受到队列 confirm.queue 消息:{}", msg);
    }
}
