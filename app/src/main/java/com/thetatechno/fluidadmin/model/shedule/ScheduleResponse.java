package com.thetatechno.fluidadmin.model.shedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thetatechno.fluidadmin.model.facility_model.Facility;

import java.util.List;

public class ScheduleResponse {
    @SerializedName("items")
    @Expose
    private List<Schedule> schedules = null;
    @SerializedName("error")
    @Expose
    private Error error;

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
