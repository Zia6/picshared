package com.zhai.picshared.model.enums;

import cn.hutool.core.util.ObjUtil;

public enum EventType {
    PICTURE_INSERT("picture.insert",0),
    PICTURE_UPDATE("picture.update",1),
    PICTURE_DELETE("picture.delete",2),
    PICTURE_EDIT("picture.edit",3);

    private final String text;
    private final int value;

    EventType(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     */
    public static EventType getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (EventType eventType : EventType.values()) {
            if (eventType.value == value) {
                return eventType;
            }
        }
        return null;
    }

    public String getText() {
        return text;
    }

    public int getValue() {
        return value;
    }

    public static EventType getEnumByText(String text) {
        if (ObjUtil.isEmpty(text)) {
            return null;
        }
        for (EventType eventType : EventType.values()) {
            if (eventType.text.equalsIgnoreCase(text)) {
                return eventType;
            }
        }
        return null;
    }
}
