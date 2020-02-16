package com.thetatecno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerList {
    @SerializedName("data")
    @Expose
    private List<Facility> facilities = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> data) {
        this.facilities = data;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
