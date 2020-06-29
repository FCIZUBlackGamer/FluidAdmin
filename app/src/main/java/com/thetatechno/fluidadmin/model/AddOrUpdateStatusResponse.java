package com.thetatechno.fluidadmin.model;

import com.google.gson.annotations.SerializedName;

public class AddOrUpdateStatusResponse {
    @SerializedName("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
