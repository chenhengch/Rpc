package com.mq.boot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 产生死信队列的原因
 * 1.消息投递到MQ中存放 消息已经过期  消费者没有及时的获取到我们消息，消息如果存放到mq服务器中过期之后，会转移到备胎死信队列存放。
 * 2.队列达到最大的长度 （队列容器已经满了）
 * 3.消费者消费多次消息失败，就会转移存放到死信队列中
 * <p>
 * <p>
 * <p>
 * 死信队列应用场景
 * 1.30分钟订单超时设计
 * A.Redis过期key ：
 * B.死信延迟队列实现：
 * 采用死信队列，创建一个普通队列没有对应的消费者消费消息，在30分钟过后
 * 就会将该消息转移到死信备胎消费者实现消费。
 * 备胎死信消费者会根据该订单号码查询是否已经支付过，如果没有支付的情况下
 * 则会开始回滚库存操作。
 */
@Configuration
public class DeadLetterMQConfig {
    /**
     * 订单交换机
     */
    private final static String ORDER_EXCHANGE = "my_order_exchange";

    /**
     * 订单队列
     */
    private final static String ORDER_QUEUE = "my_order_queue";


    /**
     * 订单路由key
     */
    private final static String ORDER_ROUTING_KEY = "my.order";
    /**
     * 死信交换机
     */
    private final static String DLX_EXCHANGE = "my_dlx_exchange";

    /**
     * 死信队列
     */
    private final static String DLX_QUEUE = "my_dlx_queue";
    /**
     * 死信路由
     */
    private final static String DLX_ROUTING_KEY = "dlx";

    /**
     * 声明死信交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE);
    }

    /**
     * 声明死信队列
     *
     * @return Queue
     */
    @Bean
    public Queue dlxQueue() {
        return new Queue(DLX_QUEUE);
    }

    /**
     * 声明订单业务交换机
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE);
    }

    /**
     * 声明订单队列
     *
     * @return Queue
     */
    @Bean
    public Queue orderQueue() {
        // 订单队列绑定我们的死信交换机
        return QueueBuilder.durable(ORDER_QUEUE)
                //声明该队列的死信消息发送到的 交换机 （队列添加了这个参数之后会自动与该交换机绑定，并设置路由键，不需要开发者手动设置)
                //.withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                //声明该队列死信消息在交换机的 路由键
                //.withArgument("x-dead-letter-routing-key", DLX_ROUTING_KEY)
                //.withArgument("x-max-length",2)
                //.withArgument("x-message-ttl", 200000)
                .withArgument("x-max-priority",10)
                .build();
    }

    /**
     * 绑定死信队列到死信交换机
     *
     * @return Binding
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with(DLX_ROUTING_KEY);
    }


    /**
     * 绑定订单队列到订单交换机
     *
     * @return Binding
     */
    @Bean
    public Binding orderBinding() {
        return BindingBuilder.bind(orderQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }
}