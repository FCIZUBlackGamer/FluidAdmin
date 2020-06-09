package com.thetatechno.fluidadmin.model.session_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Session implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("providerId")
    @Expose
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
    @SerializedName("sessionDate")
    @Expose
    private String sessionDate;
    @SerializedName("scheduledStart")
    @Expose
    private String scheduledStart;
    @SerializedName("scheduledEnd")
    @Expose
    private String scheduledEnd;
    @SerializedName("siteId")
    @Expose
    private String siteId;
    @SerializedName("isRunning")
    @Expose
    private String isRunning;
    @SerializedName("langId")
    @Expose
    private String langId;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getScheduledStart() {
        return scheduledStart;
    }

    public void setScheduledStart(String scheduledStart) {
        this.scheduledStart = scheduledStart;
    }

    public String getScheduledEnd() {
        return scheduledEnd;
    }

    public void setScheduledEnd(String scheduledEnd) {
        this.scheduledEnd = scheduledEnd;
    }

    public String getIsRunning() {
        return isRunning;
    }

    public void setIsRunning(String isRunning) {
        this.isRunning = isRunning;
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

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }
}
