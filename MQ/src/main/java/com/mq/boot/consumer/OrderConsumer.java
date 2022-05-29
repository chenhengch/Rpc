package com.mq.boot.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class OrderConsumer {

    /**
     * 监听队列回调的方法
     *消息被(basic.reject() or basic.nack()) and requeue = false，即消息被消费者拒绝或者nack，并且重新入队为false。
     * nack()与reject()的区别是：reject()不支持批量拒绝，而nack()可以
     */
    @RabbitHandler
    @RabbitListener(queues = "my_order_queue")
    public void orderConsumer(Message message, Channel channel) throws IOException {
        String msg = new String(message.getBody());
        log.info("当前时间：{},收到正常队列{}", new Date().toString(), msg);



    }
}
