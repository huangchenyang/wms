package com.feiruirobots.jabil.p1.model;

import cn.hutool.core.util.StrUtil;

public enum FUNCTION {
    FINISHED_GOODS("FINISHED_GOODS", "FG"),
    SEMI_FG("SEMI_FG", "SFG"),
    RAW_MATERIAL("RAW_MATERIAL", "RM"),
    RTV_RTC("RTV_RTC", "RT"),
    STAGING("STAGING", "STG");
    public String msg;
    public String value;

    FUNCTION(String value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public static FUNCTION of(String value) {
        for (FUNCTION state : FUNCTION.values()) {
            if (StrUtil.equals(state.value, value))
                return state;
        }
        return null;
    }
}
