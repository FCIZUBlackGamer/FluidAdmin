package com.thetatechno.fluidadmin.model.appointment_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AppointmentCalenderDaysListData {

    @SerializedName("items")
    @Expose
    private ArrayList<AppointmentDayDetails> dayDetailsList ;
    @SerializedName("error")
    private Error error;

    public ArrayList<AppointmentDayDetails> getDayDetailsList() {
        return dayDetailsList;
    }

    public void setDayDetailsList(ArrayList<AppointmentDayDetails> appointments) {
        this.dayDetailsList = appointments;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
