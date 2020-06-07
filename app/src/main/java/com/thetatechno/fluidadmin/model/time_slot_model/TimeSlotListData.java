package com.thetatechno.fluidadmin.model.time_slot_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimeSlotListData {
    @SerializedName("items")
    @Expose
    private List<TimeSlot> timeSlots = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setStaffList(List<TimeSlot> data) {
        this.timeSlots = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
