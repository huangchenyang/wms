package com.feiruirobots.jabil.p1.model;

import com.alibaba.fastjson.JSONObject;

public class Carton {
    private String workCell;
    private Integer palletId;
    private String boxId;
    private String esr;
    private String grn;
    private int Id;
    private Integer qty;
    private String partNo;
    private String forward;
    private String ram;
    private String fgtf;
    private String type;
    private Integer cartonCount;
    private String referenceId;
    private String fromStation;
    private Integer status;
    private String function;

    //出库的
    private String billNo;

    public static Carton parse(JSONObject jsonObject) {
        Carton carton = new Carton();
        carton.setPalletId(jsonObject.getInteger("pallet_id"));
        carton.setId(jsonObject.getInteger("id"));
        carton.setBoxId(jsonObject.getString("box_id"));
        carton.setForward(jsonObject.getString("forward"));
        carton.setWorkCell(jsonObject.getString("work_cell"));
        carton.setPartNo(jsonObject.getString("part_no"));
        carton.setEsr(jsonObject.getString("esr"));
        carton.setGrn(jsonObject.getString("grn"));
        carton.setStatus(jsonObject.getInteger("status"));
        carton.setQty(jsonObject.getInteger("qty"));
        carton.setReferenceId(jsonObject.getString("reference_id"));
        carton.setForward(jsonObject.getString("forward"));
        carton.setCartonCount(jsonObject.getInteger("carton_count"));
        carton.setType(jsonObject.getString("type"));
        carton.setFunction(jsonObject.getString("function"));

        carton.setBillNo(jsonObject.getString("bill_no"));
        carton.setFromStation(jsonObject.getString("from_station"));
        return carton;
    }

    public String getWorkCell() {
        return workCell;
    }

    public void setWorkCell(String workCell) {
        this.workCell = workCell;
    }

    public Integer getPalletId() {
        return palletId;
    }

    public void setPalletId(Integer palletId) {
        this.palletId = palletId;
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId;
    }

    public String getEsr() {
        return esr;
    }

    public void setEsr(String esr) {
        this.esr = esr;
    }

    public String getGrn() {
        return grn;
    }

    public void setGrn(String grn) {
        this.grn = grn;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getForward() {
        return forward;
    }

    public void setForward(String forward) {
        this.forward = forward;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getFgtf() {
        return fgtf;
    }

    public void setFgtf(String fgtf) {
        this.fgtf = fgtf;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCartonCount() {
        return cartonCount;
    }

    public void setCartonCount(Integer cartonCount) {
        this.cartonCount = cartonCount;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
}
