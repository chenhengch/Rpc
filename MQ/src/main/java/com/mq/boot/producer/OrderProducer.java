package com.mq.boot.producer;

import com.mq.boot.config.DeadLetterMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
public class OrderProducer {
    @Autowired
    private RabbitTemplate rabbitTemplate;


    private final static String ORDER_EXCHANGE = "my_order_exchange";

    /**
     * 订单路由key
     */
    private final static String ORDER_ROUTING_KEY = "my.order";

    /**
     * 测试 过期时间
     * @param msg
     * @return
     */
    @RequestMapping("/sendOrder/{msg}")
    public String sendOrder(@PathVariable String msg) {
        log.info("当前时间：{},发送一条信息正常队列队列:{}", new Date(), msg);
        rabbitTemplate.convertAndSend(ORDER_EXCHANGE, ORDER_ROUTING_KEY, msg, message -> {

            //消息队列：消息即使过期，也不一定会被马上丢弃，因为消息是否过期是在即将投递到消费者之前判定的如果当前队列有严重的消息积压情况，则已过期的消息也许还能存活较长时间
            //message.getMessageProperties().setExpiration("10000");
            return message;
        });
        return "success";
    }

    /**
     * 测试 最大队列
     */
    @RequestMapping("/sendMaxLength")
    public void sendMaxLength() {

        for (int i = 1; i < 11; i++) {

            // i= 5 设置优先级为10 ,优先级也可以作为形参接受
            if (i == 5){
                rabbitTemplate.convertAndSend(ORDER_EXCHANGE,ORDER_ROUTING_KEY,"maxlength"+i,msg -> {
                    msg.getMessageProperties().setPriority(10);
                    return msg;
                });
            }else {
                rabbitTemplate.convertAndSend(ORDER_EXCHANGE,ORDER_ROUTING_KEY,"maxlength"+i,msg -> {
                    //msg.getMessageProperties().setPriority(5);
                    return msg;
                });
            }
        }

    }

}
