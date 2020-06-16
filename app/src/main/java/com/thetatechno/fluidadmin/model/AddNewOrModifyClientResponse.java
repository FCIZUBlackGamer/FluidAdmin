package com.thetatechno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddNewOrModifyClientResponse extends APIResponse {
    @SerializedName("clientId")
    @Expose
    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
