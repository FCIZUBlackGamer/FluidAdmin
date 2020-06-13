package com.thetatechno.fluidadmin.model.staff_model;

public class StaffListModel  {

    private String errorMessage;
    private StaffData staffData;

    public StaffListModel(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public StaffListModel(StaffData staffData) {
        this.staffData = staffData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }



    public StaffData getStaffData() {
        return staffData;
    }


}
