package com.zhai.picshared.manager.mq.listener;

import cn.hutool.json.JSONUtil;
import com.zhai.picshared.config.RabbitMqConfig;
import com.zhai.picshared.manager.redis.CacheManagerHelper;
import com.zhai.picshared.model.dto.message.EventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class PictureEventListener {

    @Resource
    private CacheManagerHelper cacheManagerHelper;

    @RabbitListener(queues = RabbitMqConfig.QUEUE_PICTURE)
    public void onMessage(String messageJson) {
        EventMessage event = JSONUtil.toBean(messageJson, EventMessage.class);

        if (event.getMessageId() == null || !cacheManagerHelper.tryProcessMessage(event.getMessageId())) {
            log.warn("重复消费消息，跳过：{}", event.getMessageId());
            return;
        }

        try {
            handlePictureEvent(event);
            log.info("成功消费 picture 消息：{}", event);
        } catch (Exception e) {
            log.error("消费 picture 消息失败：{}", event, e);
            // 可选：记录失败日志、补偿机制等
        }
    }

    /**
     * 路由处理具体事件
     */
    private void handlePictureEvent(EventMessage event) {
        String type = event.getEventType();
        Long refId = event.getRefId();

        // 目前只处理清除分页缓存（可拓展）
        switch (type) {
            case "picture.edit":
                cacheManagerHelper.clearBySpace(refId);
            case "picture.update":
                cacheManagerHelper.clearBySpace(refId);
            case "picture.delete":
                cacheManagerHelper.clearBySpace(refId);
                break;
            case "picture.insert":
                cacheManagerHelper.clearBySpace(refId);
                // 可选：后续预热缓存
                break;
            default:
                log.warn("未识别的事件类型：{}", type);
        }
    }
}
