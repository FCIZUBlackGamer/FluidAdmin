package com.thetatechno.fluidadmin.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Facility implements Serializable {
    @SerializedName("waitingAreaDescription")
    @Expose
    private String waitingAreaDescription;
    @SerializedName("waitingAreaId")
    private String waitingAreaId = "";
    @SerializedName("deviceDescription")
    private String deviceDescription;
    @SerializedName("typeCode")
    @Expose
    private String type;

    @SerializedName("id")
    @Expose
    private String code = "";

    @SerializedName("description")
    @Expose
    private String description = "";

    @SerializedName("deviceId")
    @Expose
    private String deviceId;

    @SerializedName("langId")
    @Expose
    private String langId;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getWaitingAreaDescription() {
        return waitingAreaDescription;
    }

    public void setWaitingAreaDescription(String waitingAreaDescription) {
        this.waitingAreaDescription = waitingAreaDescription;
    }

    public String getWaitingAreaId() {
        return waitingAreaId;
    }

    public void setWaitingAreaId(String waitingAreaId) {
        this.waitingAreaId = waitingAreaId;
    }

    public String getDeviceDescription() {
        return deviceDescription;
    }

    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

    @NonNull
    @Override
    public String toString() {
        return getDescription();
    }
}
