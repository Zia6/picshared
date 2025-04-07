package com.zhai.picshared.manager.mq;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhai.picshared.mapper.MessageOutboxMapper;
import com.zhai.picshared.model.dto.message.EventMessage;
import com.zhai.picshared.model.entity.MessageOutbox;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageDispatcherTask {

    private final MessageOutboxMapper messageOutboxMapper;
    private final MqSender mqSender;
    // 每次最多重试 5 次
    private static final int MAX_RETRY = 5;
    @Scheduled(fixedDelay = 60000)
    public void warnIfTooManyFailures() {
        long failedCount = messageOutboxMapper.selectCount(
                new LambdaQueryWrapper<MessageOutbox>()
                        .eq(MessageOutbox::getStatus, "FAILED")
                        .ge(MessageOutbox::getRetry_count, MAX_RETRY)
        );
        if (failedCount > 0) {
            log.warn("当前有 {} 条消息重试失败达到上限，请检查处理机制！", failedCount);
        }
    }
    @Scheduled(fixedDelay = 2000) // 每2秒轮询一次
    public void dispatch() {
        List<MessageOutbox> pendingList = messageOutboxMapper.selectList(
                new LambdaQueryWrapper<MessageOutbox>()
                        .in(MessageOutbox::getStatus, "PENDING", "FAILED")
                        .lt(MessageOutbox::getRetry_count, MAX_RETRY)
                        .orderByAsc(MessageOutbox::getCreate_time)
                        .last("LIMIT 100")
        );

        for (MessageOutbox msg : pendingList) {
            try {
                EventMessage event = new EventMessage(
                        msg.getId(),             // 作为 messageId
                        msg.getEvent_type(),
                        msg.getRef_id(),
                        msg.getPayload()
                );

                mqSender.send(event);

                msg.setStatus("SENT");
            } catch (Exception e) {
                log.error("消息发送失败：{}", msg.getId(), e);
                msg.setStatus("FAILED");
                msg.setRetry_count(msg.getRetry_count() + 1);
            }
            messageOutboxMapper.updateById(msg);
        }
    }


}
