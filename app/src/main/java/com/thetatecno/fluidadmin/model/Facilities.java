package com.thetatecno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Facilities {
    @SerializedName("data")
    @Expose
    private List<Facility> facilities = null;

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> data) {
        this.facilities = data;
    }
}
