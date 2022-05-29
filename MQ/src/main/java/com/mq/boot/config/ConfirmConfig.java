package com.mq.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 高级发布确认
 * 发布 不管交换机是或否接收消息都会会调用
 * spring.rabbitmq.publisher-confirm-type: correlated
 * 回退  不成功回退生产者
 * spring.rabbitmq.publisher-returns: true
 * <p>
 * 及
 * 备份交换机
 * <p>
 * 两者以备份为主
 *
 * 2022-04-12 14:23:57.671  INFO 39780 --- [io-8080-exec-10] com.mq.boot.producer.ConfirmProducer     : 发送消息内容:jj
 * 2022-04-12 14:23:57.687  INFO 39780 --- [nectionFactory1] com.mq.boot.callback.MyCallBack          : 交换机已经收到 id 为:1的消息
 * 2022-04-12 14:23:57.693  INFO 39780 --- [nectionFactory1] com.mq.boot.callback.MyCallBack          : 交换机已经收到 id 为:2的消息
 * 2022-04-12 14:23:57.700  INFO 39780 --- [ntContainer#1-1] com.mq.boot.consumer.ConfirmConsumer     : 接受到队列 confirm.queue 消息:jjkey1
 * 2022-04-12 14:23:57.700 ERROR 39780 --- [ntContainer#0-1] com.mq.boot.consumer.BackUpConsumer      : 备份不可路由的消息：jjkey2
 * 2022-04-12 14:23:57.702 ERROR 39780 --- [tContainer#12-1] com.mq.boot.consumer.WarnConsumer        : 报警发现不可路由消息：jjkey2
 */
@Configuration
public class ConfirmConfig {
    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";

    //备份交换机
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    //备份队列
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    //备份报警
    public static final String WARNING_QUEUE_NAME = "warning.queue";


    // 声明备份队列
    @Bean("backupQueue")
    public Queue backupQueue() {
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }

    // 声明报警队列
    @Bean("warningQueue")
    public Queue warningQueue() {
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }

    //声明备份 Exchange
    @Bean("backupExchange")
    public FanoutExchange backupExchange() {
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }

    //声明确认 Exchange 交换机的备份交换机   无法投递消息发送给备份交换机
    @Bean("confirmExchange")
    public DirectExchange confirmExchange() {
        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME)
                .durable(true)
                //设置该交换机的备份交换机
                .withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME)
                .build();
    }

    // 声明确认队列
    @Bean("confirmQueue")
    public Queue confirmQueue() {
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }

    // 声明确认队列绑定关系
    @Bean
    public Binding queueBinding(@Qualifier("confirmQueue") Queue queue,
                                @Qualifier("confirmExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("key1");
    }

    // 声明备份队列绑定关系
    @Bean
    public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue backupQueue,
                                @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }

    // 声明报警队列绑定关系
    @Bean
    public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue warningQueue,
                                @Qualifier("backupExchange") FanoutExchange backupExchange) {
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
