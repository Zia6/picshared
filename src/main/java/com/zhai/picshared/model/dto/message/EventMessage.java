package com.zhai.picshared.model.dto.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventMessage {
    private Long messageId;     // 唯一 ID（来源于 message_outbox 表主键）
    private String eventType;  // picture_edit 等
    private Long refId;
    private String payload;
}
