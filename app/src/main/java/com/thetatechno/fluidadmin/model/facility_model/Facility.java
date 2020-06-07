package com.thetatechno.fluidadmin.model.facility_model;

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
    private String id = "";

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

    @SerializedName("siteId")
    @Expose
    private String siteId;

    @SerializedName("siteDescription")
    private String siteDescription;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteDescription() {
        return siteDescription;
    }

    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }
}
