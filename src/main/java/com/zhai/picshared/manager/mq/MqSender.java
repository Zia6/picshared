package com.zhai.picshared.manager.mq;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.zhai.picshared.config.RabbitMqConfig;
import com.zhai.picshared.model.dto.message.EventMessage;
import com.zhai.picshared.model.entity.MessageOutbox;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MqSender {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 通用发送方法：传入 EventMessage 自动序列化发送
     */
    public void send(EventMessage message) {
        String routingKey = message.getEventType();
        String body = JSONUtil.toJsonStr(message);

        rabbitTemplate.convertAndSend(
                RabbitMqConfig.EXCHANGE,
                routingKey,
                body
        );
    }

    public EventMessage toEventMessage(MessageOutbox msg) {
        return new EventMessage(
                msg.getId(),
                msg.getEvent_type(),
                msg.getRef_id(),
                msg.getPayload()
        );
    }

    /**
     * 可选：更方便调用的方法（含 payload 序列化）
     */
    public void send(Long messageId,String eventType, Long refId, Object payloadObj) {
        String payload = payloadObj != null ? JSONUtil.toJsonStr(payloadObj) : null;
        send(new EventMessage(messageId,eventType, refId, payload));
    }
}
