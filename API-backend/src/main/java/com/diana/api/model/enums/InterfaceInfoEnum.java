package com.diana.api.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 接口状态枚举
 *
 * @author diana
 * 
 */
public enum InterfaceInfoEnum {
    /**
     * 接口关闭
     */
    OFFLINE("关闭", 0),
    /**
     * 接口上线
     */
    ONLINE("上线", 1);

    private final String text;

    private final int value;

    InterfaceInfoEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static InterfaceInfoEnum getEnumByValue(int value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (InterfaceInfoEnum anEnum : InterfaceInfoEnum.values()) {
            if (anEnum.value == value) {
                return anEnum;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
