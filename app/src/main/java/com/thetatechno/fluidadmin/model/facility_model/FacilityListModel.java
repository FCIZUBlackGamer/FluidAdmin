package com.thetatechno.fluidadmin.model.facility_model;

public class FacilityListModel {
    FacilitiesResponse response;
    String errorMessage;

    public FacilityListModel(FacilitiesResponse response) {
        this.response = response;
    }

    public FacilityListModel(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public FacilitiesResponse getResponse() {
        return response;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
