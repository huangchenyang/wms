package com.feiruirobots.jabil.p1.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class ListJson {
    private int id;
    private String fromId;
    private Double height;
    private int partQty;
    private String firstFloor;
    private int boxCount;
    private int pull;
    private String function;
    private Date inTime;
    private String status;
    private Date createTime;
    private String binId;
    private String rtvType;
    private String inventoryType;
    private String binBarcodeUrlImg;
    private String terminal;
    private String fgtf;
    private static String TAG="hcy--BizTask";

    public static ListJson parse(JSONObject jsonObject) {
        ListJson listJson = new ListJson();
        listJson.setId(jsonObject.getInteger("id"));
        listJson.setBoxCount(jsonObject.getInteger("box_count"));
        listJson.setBinId(jsonObject.getString("bin_id"));
        listJson.setTerminal(jsonObject.getString("terminal"));
        listJson.setFgtf(jsonObject.getString("fgtf"));
        return listJson;
    }

    public int getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(int boxCount) {
        this.boxCount = boxCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
