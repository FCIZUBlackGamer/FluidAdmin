package com.thetatechno.fluidadmin.model.device_model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Device {
    @SerializedName("ID")
    @Expose
    String id;
    @SerializedName("DESCRIPTION")
    @Expose
    String description = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    @SerializedName("MAC_ADDRESS")
    @Expose
    String macAddress;

    @NonNull
    @Override
    public String toString() {
        return description;
    }
}
