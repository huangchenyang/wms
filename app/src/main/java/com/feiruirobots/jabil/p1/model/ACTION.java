package com.feiruirobots.jabil.p1.model;

import cn.hutool.core.util.StrUtil;

public enum ACTION {
    IN("IN", "In Stock"),
    OUT("OUT", "Retrive"),
    INV("INV", "Inventory");

    public String msg;
    public String value;

    ACTION(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public static ACTION of(String value) {
        for (ACTION state : ACTION.values()) {
            if (StrUtil.equals(state.value, value))
                return state;
        }
        return null;
    }
}
