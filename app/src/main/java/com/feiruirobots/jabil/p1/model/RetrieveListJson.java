package com.feiruirobots.jabil.p1.model;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class RetrieveListJson {
    private String bill_no;
    private int boxCount;
    private String binId;
    private String terminal;
    private String fgtf;
    private String pallet_status;
    private String task_status;
    private int billBoxCount;
    private static String TAG="hcy--RetrieveListJson";

    public static RetrieveListJson parse(JSONObject jsonObject) {
        RetrieveListJson retrieveListJson = new RetrieveListJson();
        Log.d(TAG,"RetrieveListJson---1");
        retrieveListJson.setBillNo(jsonObject.getString("bill_no"));
        retrieveListJson.setBoxCount(jsonObject.getInteger("box_count"));
        retrieveListJson.setBillBoxCount(jsonObject.getInteger("bill_box_count"));
        Log.d(TAG,"RetrieveListJson---2");
        retrieveListJson.setBinId(jsonObject.getString("bin_id"));
        retrieveListJson.setTerminal(jsonObject.getString("terminal"));
        Log.d(TAG,"RetrieveListJson---3");
        retrieveListJson.setTaskStatus(jsonObject.getString("task_status"));
        retrieveListJson.setPalletStatus(jsonObject.getString("pallet_status"));
        Log.d(TAG,"RetrieveListJson---4");
        return retrieveListJson;
    }

    public int getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(int boxCount) {
        this.boxCount = boxCount;
    }

    public int getBillBoxCount() {
        return billBoxCount;
    }

    public void setBillBoxCount(int billBoxCount) {
        this.billBoxCount = billBoxCount;
    }

    public String getBillNo() {
        return bill_no;
    }

    public void setBillNo(String bill_no) {
        this.bill_no = bill_no;
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }


    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    public String getFgtf() {
        return fgtf;
    }

    public void setFgtf(String fgtf) {
        this.fgtf = fgtf;
    }

    public String getPalletStatus() {
        return pallet_status;
    }

    public void setPalletStatus(String pallet_status) {
        this.pallet_status = pallet_status;
    }

    public String getTaskStatus() {
        return task_status;
    }

    public void setTaskStatus(String task_status) {
        this.task_status = task_status;
    }
}
