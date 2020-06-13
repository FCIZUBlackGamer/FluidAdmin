package com.thetatechno.fluidadmin.model.appointment_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thetatechno.fluidadmin.utils.Constants;

public class AppointmentBooked {
    @SerializedName("clientId")
    @Expose
    String clientId;

    @SerializedName("apptType")
    @Expose
    // slot id
    String apptType;
    @SerializedName("sessionId")
    @Expose
    String sessionId;
    @SerializedName("startTime")
    @Expose
    String startTime;
    @SerializedName("apptId")
    @Expose
    String apptId;
    @SerializedName(Constants.LANG_ID)
    @Expose
    String langId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getApptType() {
        return apptType;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getApptId() {
        return apptId;
    }

    public void setApptId(String apptId) {
        this.apptId = apptId;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

}
