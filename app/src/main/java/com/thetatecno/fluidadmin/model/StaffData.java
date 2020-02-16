package com.thetatecno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StaffData {
    @SerializedName("data")
    @Expose
    private List<Staff> staffList = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Staff> getStaffList() {
        return staffList;
    }

    public void setStaffList(List<Staff> data) {
        this.staffList = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
