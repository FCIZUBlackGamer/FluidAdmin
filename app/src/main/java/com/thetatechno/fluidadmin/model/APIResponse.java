package com.thetatechno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIResponse {

    @SerializedName("error")
    @Expose
    private Error error;


    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
