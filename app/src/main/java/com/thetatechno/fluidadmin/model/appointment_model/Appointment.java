package com.thetatechno.fluidadmin.model.appointment_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Appointment {
    @SerializedName("sltoId")
    @Expose
    private String sltoId;
    @SerializedName("scheduledTime")
    @Expose
    private String scheduledTime = "";
    @SerializedName("expectedTime")
    @Expose
    private String expectedTime = "";
    @SerializedName("arrivalTime")
    @Expose
    private String arrivalTime = "";
    @SerializedName("isFollowup")
    @Expose
    private String isFollowup;
    @SerializedName("ProviderImagePath")
    @Expose
    private String providerImagePath;
    @SerializedName("providerEnName")
    @Expose
    private String providerEnName;
    @SerializedName("providerArName")
    @Expose
    private String providerArName;
    @SerializedName("customerEnName")
    @Expose
    private String customerEnName;
    @SerializedName("customerArName")
    @Expose
    private String customerArName;
    @SerializedName("facilityEnName")
    @Expose
    private String facilityEnName;
    @SerializedName("facilityArName")
    @Expose
    private String facilityArName;

    public String getSltoId() {
        return sltoId;
    }

    public void setSltoId(String sltoId) {
        this.sltoId = sltoId;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(String scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public void setExpectedTime(String expectedTime) {
        this.expectedTime = expectedTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getIsFollowup() {
        return isFollowup;
    }

    public void setIsFollowup(String isFollowup) {
        this.isFollowup = isFollowup;
    }

    public String getProviderImagePath() {
        return providerImagePath;
    }

    public void setProviderImagePath(String providerImagePath) {
        this.providerImagePath = providerImagePath;
    }

    public String getProviderEnName() {
        return providerEnName;
    }

    public void setProviderEnName(String providerEnName) {
        this.providerEnName = providerEnName;
    }

    public String getProviderArName() {
        return providerArName;
    }

    public void setProviderArName(String providerArName) {
        this.providerArName = providerArName;
    }

    public String getCustomerEnName() {
        return customerEnName;
    }

    public void setCustomerEnName(String customerEnName) {
        this.customerEnName = customerEnName;
    }

    public String getCustomerArName() {
        return customerArName;
    }

    public void setCustomerArName(String customerArName) {
        this.customerArName = customerArName;
    }

    public String getFacilityEnName() {
        return facilityEnName;
    }

    public void setFacilityEnName(String facilityEnName) {
        this.facilityEnName = facilityEnName;
    }

    public String getFacilityArName() {
        return facilityArName;
    }

    public void setFacilityArName(String facilityArName) {
        this.facilityArName = facilityArName;
    }
}
