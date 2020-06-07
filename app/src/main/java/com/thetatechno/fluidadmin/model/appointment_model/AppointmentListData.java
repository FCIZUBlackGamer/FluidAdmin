package com.thetatechno.fluidadmin.model.appointment_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thetatechno.fluidadmin.model.appointment_model.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListData {
    @SerializedName("items")
    @Expose
    private List<Appointment> appointments = new ArrayList<>();
    @SerializedName("status")
    @Expose
    private String status = "";

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
