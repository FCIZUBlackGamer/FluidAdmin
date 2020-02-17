package com.thetatecno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Facility implements Serializable {
    @SerializedName("WAITING_AREA_ID")
    @Expose
    private String waitingAreaID;

    @SerializedName("TYPE_CODE")
    @Expose
    private String type;

    @SerializedName("ID")
    @Expose
    private String code;

    @SerializedName("DESCRIPTION")
    @Expose
    private String description;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getWaitingAreaID() {
        return waitingAreaID;
    }

    public void setWaitingAreaID(String waitingAreaID) {
        this.waitingAreaID = waitingAreaID;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
