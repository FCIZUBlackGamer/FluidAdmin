package com.thetatechno.fluidadmin.model.branches_model;

import com.thetatechno.fluidadmin.model.staff_model.StaffData;

public class BranchesResponseModel {
    private String errorMessage;
    private BranchesResponse branchesResponse;


    public String getErrorMessage() {
        return errorMessage;
    }

    public BranchesResponseModel(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public BranchesResponseModel(BranchesResponse branchesResponse) {
        this.branchesResponse = branchesResponse;
    }

    public BranchesResponse getBranchesResponse() {
        return branchesResponse;
    }
}
