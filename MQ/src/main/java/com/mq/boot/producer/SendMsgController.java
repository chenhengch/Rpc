package com.mq.boot.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RequestMapping("ttl")
@RestController
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 队列设置TTL
     * 测试死信
     *
     * @param msg
     */
    @RequestMapping("/send/{msg}")
    public void sendMsg(@PathVariable String msg){
        log.info("测试消息的时间:{},消息为:{}",new Date().toString(),msg);
        rabbitTemplate.convertAndSend("X","XA","XA消息为:"+msg);
        rabbitTemplate.convertAndSend("X","XB","XB消息为:"+msg);
    }

    /**
     *
     * 消息设置TTL
     * 根据客服端 延迟多长时间
     *
     * 如果使用在消息属性上设置 TTL 的方式，消
     * 息可能并不会按时“死亡“，因为 RabbitMQ 只会检查第一个消息是否过期，如果过期则丢到死信队列，
     * 如果第一个消息的延时时长很长，而第二个消息的延时时长很短，第二个消息并不会优先得到执行。
     *
     * 当前时间：Mon Apr 11 21:58:16 CST 2022,发送一条时长20000毫秒 TTL 信息给队列 C:老六
     * 当前时间：Mon Apr 11 21:58:20 CST 2022,发送一条时长10000毫秒 TTL 信息给队列 C:老六
     * 当前时间：Mon Apr 11 21:58:36 CST 2022,收到死信队列信息XC消息为:老六
     * 当前时间：Mon Apr 11 21:58:36 CST 2022,收到死信队列信息XC消息为:老六
     * @param msg
     * @param ttl
     */
    @RequestMapping("/send/{msg}/{ttl}")
    public void sendMsgAndTtl(@PathVariable String msg,@PathVariable String ttl){
        log.info("当前时间：{},发送一条时长{}毫秒 TTL 信息给队列 C:{}", new Date(),ttl, msg);
        rabbitTemplate.convertAndSend("X","XC","XC消息为:"+msg,correlationData -> {
            correlationData.getMessageProperties().setExpiration(ttl);
            return correlationData;
        });
    }
}
