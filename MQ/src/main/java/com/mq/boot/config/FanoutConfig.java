package com.mq.boot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Fanout 就是我们熟悉的广播模式或者订阅模式，给Fanout交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。
 */


@Configuration
public class FanoutConfig {

    /**
     * 定义交换机
     */
    private String EXCHANGE_SPRINGBOOT_NAME = "/mayikt_ex";


    /**
     * 短信队列
     */
    private String FANOUT_SMS_QUEUE = "fanout_sms_queue";
    /**
     * 邮件队列
     */
    private String FANOUT_EMAIL_QUEUE = "fanout_email_queue";

    /**
     * coupon队列
     */
    private String FANOUT_COUPON_QUEUE = "fanout_coupon_queue";
    private String EXCHANGE_COUPON = "exchange_coupon";

    @Bean
    public Queue couponQueue() {
        return new Queue(FANOUT_COUPON_QUEUE);
    }

    /**
     * 配置smsQueue
     *
     * @return
     */
    @Bean
    public Queue smsQueue() {
        return new Queue(FANOUT_SMS_QUEUE);
    }

    /**
     * 配置emailQueue
     *
     * @return
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(FANOUT_EMAIL_QUEUE);
    }

    /**
     * 配置fanoutExchange
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange(EXCHANGE_SPRINGBOOT_NAME);
    }

    /**
     * 配置fanoutExchange
     *
     * @return
     */
    @Bean
    public FanoutExchange fanoutCouponExchange() {
        return new FanoutExchange(EXCHANGE_COUPON);
    }
    // 绑定交换机 sms
    @Bean
    public Binding bindingSmsFanoutExchange(Queue smsQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(smsQueue).to(fanoutExchange);
    }

    // 绑定交换机 email
    @Bean
    public Binding bindingEmailFanoutExchange(Queue emailQueue, FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(emailQueue).to(fanoutExchange);
    }

    // 绑定交换机coupon
    @Bean
    public Binding bindingCouponFanoutExchange(Queue couponQueue, FanoutExchange fanoutCouponExchange) {
        return BindingBuilder.bind(couponQueue).to(fanoutCouponExchange);
    }
}
