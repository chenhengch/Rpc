package com.mq.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TtlQueueConfig {

    public static final String X_EXCHANGE = "X";
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    public static final String QUEUE_C = "QC";
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    public static final String DEAD_LETTER_QUEUE = "QD";


    @Bean
    public DirectExchange xExchange() {
        return new DirectExchange(X_EXCHANGE);
    }

    @Bean
    public Queue queueA() {
        return QueueBuilder.durable(QUEUE_A)
                .withArgument("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE)
                //声明该队列死信消息在交换机的 路由键
                .withArgument("x-dead-letter-routing-key", "YD")
                .withArgument("x-message-ttl", 10000)
                .build();
    }

    @Bean
    public Queue queueB() {
        return QueueBuilder.durable(QUEUE_B)
                .withArgument("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE)
                //声明该队列死信消息在交换机的 路由键
                .withArgument("x-dead-letter-routing-key", "YD")
                .withArgument("x-message-ttl", 40000)
                .build();
    }

    @Bean
    public Queue queueC() {
        return QueueBuilder.durable(QUEUE_C)
                .withArgument("x-dead-letter-exchange", Y_DEAD_LETTER_EXCHANGE)
                //声明该队列死信消息在交换机的 路由键
                .withArgument("x-dead-letter-routing-key", "YD")
                .build();
    }
    @Bean
    public DirectExchange yExchange() {
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }

    @Bean
    public Queue queueD() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    @Bean
    public Binding bindingAToX() {
        return BindingBuilder.bind(queueA()).to(xExchange()).with("XA");
    }

    @Bean
    public Binding bindingBToX() {
        return BindingBuilder.bind(queueB()).to(xExchange()).with("XB");
    }
    @Bean
    public Binding bindingCToX() {
        return BindingBuilder.bind(queueC()).to(xExchange()).with("XC");
    }

    @Bean
    public Binding bindingDToY() {
        return BindingBuilder.bind(queueD()).to(yExchange()).with("YD");
    }
}
