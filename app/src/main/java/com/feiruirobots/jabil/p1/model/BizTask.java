package com.feiruirobots.jabil.p1.model;

import com.alibaba.fastjson.JSONObject;

public class BizTask {
    private String id;
    private String fromId;
    private String destId;
    private int palletId;
    private int status;
    private String bufferId;
    private String referenceId;
    private String desc;
    private Boolean isPick;
    private String msg;
    private String name;

    public static BizTask parse(JSONObject jsonObject) {
        BizTask bizTask = new BizTask();
        bizTask.setBufferId(jsonObject.getString("buffer_id"));
        bizTask.setPalletId(jsonObject.getInteger("pallet_id"));
        bizTask.setFromId(jsonObject.getString("from_id"));
        bizTask.setDestId(jsonObject.getString("dest_id"));
        bizTask.setReferenceId(jsonObject.getString("reference_id"));
        bizTask.setDesc(jsonObject.getString("desc"));
        bizTask.setId(jsonObject.getString("id"));
        bizTask.setStatus(jsonObject.getInteger("status"));
        bizTask.setMsg(jsonObject.getString("msg"));
        bizTask.setPick(jsonObject.getBoolean("is_pick"));
        bizTask.setName(jsonObject.getString("name"));
        return bizTask;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        if (fromId == null) fromId = "-";
        this.fromId = fromId;
    }

    public String getDestId() {
        return destId;
    }

    public void setDestId(String destId) {
        if (destId == null) destId = "-";
        this.destId = destId;
    }

    public int getPalletId() {
        return palletId;
    }

    public void setPalletId(int palletId) {
        this.palletId = palletId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBufferId() {
        return bufferId;
    }

    public void setBufferId(String bufferId) {
        if (bufferId == null) bufferId = "-";
        this.bufferId = bufferId;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean isPick() {
        return isPick;
    }

    public void setPick(Boolean pick) {
        isPick = pick;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
