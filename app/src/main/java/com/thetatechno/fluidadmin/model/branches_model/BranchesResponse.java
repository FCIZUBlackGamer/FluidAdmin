package com.thetatechno.fluidadmin.model.branches_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BranchesResponse {
    @SerializedName("data")
    @Expose
    private List<Branch> branchList = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Branch> getBranchList() {
        return branchList;
    }

    public void setCodeList(List<Branch> data) {
        this.branchList = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
