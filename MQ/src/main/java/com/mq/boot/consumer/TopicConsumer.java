package com.mq.boot.consumer;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author PC
 */
@Slf4j
@Component
public class TopicConsumer {
    public static final String QUEUE_EMAIL = "topic.message";
    //email队列
    public static final String QUEUE_SMS = "topic.message.s";

    /**
     * @RabbitListener：方法上的注解，声明这个方法是一个消费者方法，需要指定下面的属性：
     *
     * bindings：指定绑定关系，可以有多个。值是@QueueBinding的数组。@QueueBinding包含下面属性：
     *
     * value：这个消费者关联的队列。值是@Queue，代表一个队列
     *
     * exchange：队列所绑定的交换机，值是@Exchange类型
     *
     * key：队列和交换机绑定的RoutingKey，可指定多个
     * ————————————————
     *
     */
   /* @RabbitHandler
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue_email", durable = "true"),
            exchange = @Exchange(
                    value = "topic.exchange",
                    ignoreDeclarationExceptions = "true",
                    type = ExchangeTypes.TOPIC
            ),
            key = {"topic.#.email.#","email.*"}))*/
    @RabbitHandler
    @RabbitListener(queues = QUEUE_SMS)
    public void queueSms(String message) {

        System.out.println("queueSms:" + message);
    }

    @RabbitListener(queues = QUEUE_EMAIL)
    @RabbitHandler
    public void queueEmail(String message) {
        System.out.println("queueEmail:" + message);

    }
}
