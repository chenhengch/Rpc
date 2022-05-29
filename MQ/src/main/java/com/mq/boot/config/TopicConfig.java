package com.mq.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * #表示0个或若干个关键字，*表示一个关键字
 */

@Configuration
public class TopicConfig {

    public static final String QUEUE_EMAIL = "topic.message";
    //email队列
    public static final String QUEUE_SMS = "topic.message.s";
    //sms队列
    public static final String EXCHANGE_NAME="topic.exchange";
    //topics类型交换机
    public static final String ROUTINGKEY_EMAIL="#.message.#";
    //topic.message
    public static final String ROUTINGKEY_SMS="topic.#";

    @Bean(QUEUE_EMAIL)
    public  Queue emailQueue(){
        return  new Queue(QUEUE_EMAIL);
    }

    @Bean(QUEUE_SMS)
    public  Queue smsQueue(){
        return  new Queue(QUEUE_SMS);
    }

    @Bean(EXCHANGE_NAME)
    public  TopicExchange topicExchange(){
        return  new TopicExchange(EXCHANGE_NAME);
    }

    //綁定队列 emailBind() 到 topicExchange 交换机,路由键只接受完全匹配 #.message.# 的队列接受者可以收到消息
    @Bean
    public Binding emailBind(@Qualifier(QUEUE_EMAIL) Queue emailQueue,@Qualifier(EXCHANGE_NAME) TopicExchange topicExchange){
        return  BindingBuilder.bind(emailQueue).to(topicExchange).with(ROUTINGKEY_EMAIL);
    }
    @Bean
    public Binding smsBind(@Qualifier(QUEUE_SMS) Queue smsQueue,@Qualifier(EXCHANGE_NAME) TopicExchange topicExchange){
        return  BindingBuilder.bind(smsQueue).to(topicExchange).with(ROUTINGKEY_SMS);
    }
}
