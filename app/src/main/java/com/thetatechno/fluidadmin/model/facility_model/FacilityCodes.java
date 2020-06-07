package com.thetatechno.fluidadmin.model.facility_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FacilityCodes {
    public List<String> getSelectedFacilities() {
        return selectedFacilities;
    }

    public void setSelectedFacilities(List<String> selectedFacilities) {
        this.selectedFacilities = selectedFacilities;
    }

    @SerializedName("facilities")
    @Expose
    List<String> selectedFacilities;

}
