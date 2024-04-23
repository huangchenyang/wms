package com.feiruirobots.jabil.p1.model;

import cn.hutool.core.util.StrUtil;

public enum BIZ_TASK_STATUS {
    INIT(0, "Wait"),
    EXEC(1, "Running"),
    GROUP(2, "Group"),
    PICK(3, "Pick"),
    FINISH(6, "Finish"),
    CANCEL(4, "Cancel");

    public String msg;
    public int value;

    BIZ_TASK_STATUS(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public static BIZ_TASK_STATUS of(int value) {
        for (BIZ_TASK_STATUS state : BIZ_TASK_STATUS.values()) {
            if (state.value == value) return state;
        }
        return null;
    }
}
