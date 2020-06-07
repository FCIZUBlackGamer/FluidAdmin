package com.thetatechno.fluidadmin.model.appointment_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentBooked {
    @SerializedName("clientId")
    @Expose
    String clientId;

    @SerializedName("apptId")
    @Expose
    // slot id
            String appointmentId;
    @SerializedName("apptType")
    @Expose
    String appointmentType;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }


}
