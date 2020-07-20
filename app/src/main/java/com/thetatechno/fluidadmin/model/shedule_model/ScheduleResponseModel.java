package com.thetatechno.fluidadmin.model.shedule_model;

public class ScheduleResponseModel {
    ScheduleResponse scheduleResponse;
    String errorMessage;

    public ScheduleResponseModel(ScheduleResponse scheduleResponse) {
        this.scheduleResponse = scheduleResponse;
    }

    public ScheduleResponseModel(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ScheduleResponse getScheduleResponse() {
        return scheduleResponse;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
