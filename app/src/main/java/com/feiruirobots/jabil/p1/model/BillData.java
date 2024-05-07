package com.feiruirobots.jabil.p1.model;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class BillData {
    private int id;
    private String name;
    private String billNo;
    private String boxId;
    private int qty;
    private String grn;
    private String esr;
    private String status;
    private Date createTime;
    private int palletId;
    private String fromStation;
    private String bizTaskId;
    private String function;
    private String workCell;
    private String partNo;
    private Date inTime;


    private static String TAG="hcy--BillData";



    public static BillData parse(JSONObject jsonObject) {
        BillData billData = new BillData();
        billData.setId(jsonObject.getInteger("id"));
        billData.setName(jsonObject.getString("name"));
        billData.setBillNo(jsonObject.getString("bill_no"));
        billData.setBoxId(jsonObject.getString("box_id"));
        billData.setGrn(jsonObject.getString("grn"));
        billData.setEsr(jsonObject.getString("esr"));
        billData.setQty(jsonObject.getInteger("qty"));
        billData.setStatus(jsonObject.getString("status"));
        billData.setInTime(jsonObject.getDate("in_time"));
        billData.setCreateTime(jsonObject.getDate("create_time"));
        billData.setFromStation(jsonObject.getString("from_station"));
        billData.setBizTaskId(jsonObject.getString("biz_task_id"));
        billData.setPalletId(jsonObject.getInteger("pallet_id"));
        billData.setPartNo(jsonObject.getString("part_no"));
        billData.setWorkCell(jsonObject.getString("work_cell"));
        billData.setFunction(jsonObject.getString("function"));
        return billData;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getGrn() {
        return grn;
    }

    public void setGrn(String grn) {
        this.grn = grn;
    }

    public String getEsr() {
        return esr;
    }

    public void setEsr(String esr) {
        this.esr = esr;
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

    public int getPalletId() {
        return palletId;
    }

    public void setPalletId(int palletId) {
        this.palletId = palletId;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public Date isInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getBizTaskId() {
        return bizTaskId;
    }

    public void setBizTaskId(String bizTaskId) {
        this.bizTaskId = bizTaskId;
    }

    public String getWorkCell() {
        return workCell;
    }

    public void setWorkCell(String workCell) {
        this.workCell = workCell;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }



}
