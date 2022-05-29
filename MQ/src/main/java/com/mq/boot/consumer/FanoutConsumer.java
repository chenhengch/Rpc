package com.mq.boot.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class FanoutConsumer {

    @RabbitHandler
    @RabbitListener(queues = "fanout_sms_queue")
    public void processSms(String msg) {
        log.info(">>短信消费者消息msg:{}<<", msg);
    }

    @RabbitHandler
    @RabbitListener(queues = "fanout_email_queue")
    public void processEmail(String msg) {
        log.info(">>邮件消费者消息msg:{}<<", msg);
    }
    @RabbitHandler
    @RabbitListener(queues = "fanout_coupon_queue")
    public void processCoupon(String msg) {
        log.info(">>coupon消息msg:{}<<", msg);
    }
}
