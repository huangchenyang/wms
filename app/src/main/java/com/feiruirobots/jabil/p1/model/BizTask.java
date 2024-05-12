package com.feiruirobots.jabil.p1.model;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class BizTask {
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
    private static String TAG="hcy--BizTask";

    public static BizTask parse(JSONObject jsonObject) {
        BizTask bizTask = new BizTask();
        int box_count = jsonObject.getInteger("box_count");
        bizTask.setId(jsonObject.getInteger("id"));
        bizTask.setBoxCount(jsonObject.getInteger("box_count"));
        bizTask.setPartQty(jsonObject.getInteger("part_qty"));
        bizTask.setHeight(jsonObject.getDouble("height"));
        bizTask.setPull(jsonObject.getInteger("pull"));
        bizTask.setFunction(jsonObject.getString("function"));
        bizTask.setFirstFloor(jsonObject.getString("first_floor"));
        bizTask.setStatus(jsonObject.getString("status"));
        bizTask.setInTime(jsonObject.getDate("in_time"));
        bizTask.setCreateTime(jsonObject.getDate("create_time"));
        bizTask.setBinId(jsonObject.getString("bin_id"));
        bizTask.setRtvType(jsonObject.getString("rtv_type"));
        bizTask.setInventoryType(jsonObject.getString("inventory_type"));
        bizTask.setBinBarcodeUrlImg(jsonObject.getString("bin_barcode_url_img"));
        bizTask.setTerminal(jsonObject.getString("terminal"));
        return bizTask;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        if (fromId == null) fromId = "-";
        this.fromId = fromId;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public int getPartQty() {
        return partQty;
    }

    public void setPartQty(int partQty) {
        this.partQty = partQty;
    }

    public String getFirstFloor() {
        return firstFloor;
    }

    public void setFirstFloor(String firstFloor) {
        this.firstFloor = firstFloor;
    }

    public int getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(int boxCount) {
        this.boxCount = boxCount;
    }

    public int getReferenceId() {
        return pull;
    }

    public void setPull(int pull) {
        this.pull = pull;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date isInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        inTime = inTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }

    public String getRtvType() {
        return rtvType;
    }

    public void setRtvType(String rtvType) {
        this.rtvType = rtvType;
    }

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }


    public String getBinBarcodeUrlImg() {
        return binBarcodeUrlImg;
    }

    public void setBinBarcodeUrlImg(String binBarcodeUrlImg) {
        this.binBarcodeUrlImg = binBarcodeUrlImg;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
}
