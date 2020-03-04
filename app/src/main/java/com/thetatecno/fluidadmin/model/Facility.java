package com.thetatecno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Facility implements Serializable {
    @SerializedName("waitingAreaId")
    @Expose
    private String waitingAreaID;

    @SerializedName("typeCode")
    @Expose
    private String type;

    @SerializedName("id")
    @Expose
    private String code;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("langId")
    @Expose
    private String langId;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected ;

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

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
