package com.mq.boot.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * @author PC
 * <p>
 * 手动签收
 */
@Component
@Slf4j
public class ConfirmConsumer implements ChannelAwareMessageListener {

    @Override
    @RabbitListener(queues = "confirm.queue")
    public void onMessage(Message message,Channel channel) throws Exception {

        try {
            log.info("消费者已经接收到消息{}", new String(message.getBody()));
            //multiple：为了减少网络流量，手动确认可以被批处理，当该参数为 true 时，则可以一次性确认 delivery_tag 小于等于传入值的所有消息
            //channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        } catch (Exception e) {
            //      requeue：true  重回队列
            //channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }

    }
}
