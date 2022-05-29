package com.mq.boot.cluster;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClusterConfig {

    //集群  镜像队列
    private static final String COMMON_QUEUE = "mirrior_common_queue";

    private static final String EXCHANGE_NAME = "exchange_name";

    @Bean("commonQueue")
    public Queue commonQueue() {
        return new Queue(COMMON_QUEUE);
    }

    @Bean("getDirectExchange")
    public DirectExchange getDirectExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding bindQueueToExchange(@Qualifier("commonQueue") Queue commonQueue,@Qualifier("getDirectExchange") DirectExchange getDirectExchange ) {
        return BindingBuilder.bind(commonQueue).to(getDirectExchange).with("common");
    }
}
