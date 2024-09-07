package com.feiruirobots.jabil.p1.model;

import com.alibaba.fastjson.JSONObject;

public class ConJson {
    private String palletStatus;
    private String binId;
    private String taskType;
    private String boxId;
    private String status;
    private static String TAG="hcy--CycleJson";

    public static ConJson parse(JSONObject jsonObject) {
        ConJson conJson = new ConJson();
        conJson.setBinId(jsonObject.getString("bin_id"));
        conJson.setPalletStatus(jsonObject.getString("pallet_status_name"));
        conJson.setTaskType(jsonObject.getString("task_type_name"));
        conJson.setStatus(jsonObject.getString("status"));
        conJson.setBoxId(jsonObject.getString("box_id"));
        return conJson;
    }


    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }


    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getPalletStatus() {
        return palletStatus;
    }

    public void setPalletStatus(String palletStatus) {
        this.palletStatus = palletStatus;
    }

    public String getBoxId(){
        return boxId;
    }

    public void setBoxId(String boxId){
        this.boxId = boxId;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }
}
