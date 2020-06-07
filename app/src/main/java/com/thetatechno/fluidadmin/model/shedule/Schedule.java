package com.thetatechno.fluidadmin.model.shedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Schedule implements Serializable {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("sexCode")
    @Expose
    private String sexCode;
    @SerializedName("imagePath")
    @Expose
    private String imagePath;
    @SerializedName("providerId")
    private String providerId;
    @SerializedName("providerName")
    @Expose
    private String providerName;
    @SerializedName("facilityId")
    @Expose
    private String facilityId;
    @SerializedName("facilitDescription")
    @Expose
    private String facilitDescription;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("endDate")
    @Expose
    private String endDate;
    @SerializedName("endTime")
    @Expose
    private String endTime;
    @SerializedName("startTime")
    private String startTime;
    @SerializedName("workingDays")
    @Expose
    String workingDays;
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("langId")
    @Expose
    String langId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getFacilityId() {
        return facilityId;
    }

    public void setFacilityId(String facilityId) {
        this.facilityId = facilityId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWorkingDays() {
        return workingDays;
    }

    public void setWorkingDays(String workingDays) {
        this.workingDays = workingDays;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getFacilitDescription() {
        return facilitDescription;
    }

    public void setFacilitDescription(String facilitDescription) {
        this.facilitDescription = facilitDescription;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getSexCode() {
        return sexCode;
    }

    public void setSexCode(String sexCode) {
        this.sexCode = sexCode;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
