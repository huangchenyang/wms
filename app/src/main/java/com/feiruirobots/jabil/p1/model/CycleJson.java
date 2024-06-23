package com.feiruirobots.jabil.p1.model;

import com.alibaba.fastjson.JSONObject;

public class CycleJson {
    private String pallet_status;
    private String binId;
    private String terminal;
    private static String TAG="hcy--CycleJson";

    public static CycleJson parse(JSONObject jsonObject) {
        CycleJson cycleJson = new CycleJson();
        cycleJson.setBinId(jsonObject.getString("bin_id"));
        cycleJson.setTerminal(jsonObject.getString("terminal"));
        cycleJson.setPalletStatus(jsonObject.getString("pallet_status"));
        return cycleJson;
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

    public String getPalletStatus() {
        return pallet_status;
    }

    public void setPalletStatus(String pallet_status) {
        this.pallet_status = pallet_status;
    }
}
