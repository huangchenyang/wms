package com.feiruirobots.jabil.p1.model;

import cn.hutool.core.util.StrUtil;

public enum FUNCTION {
    FINISHED_GOODS("FINISHED_GOODS", "Finished Goods"),
    SEMI_FG("SEMI_FG", "Semi FG"),
    RAW_MATERIAL("RAW_MATERIAL", "Raw Material"),
    RTV_RTC("RTV_RTC", "RTV RTC"),
    STAGING("STAGING", "Staging");
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
