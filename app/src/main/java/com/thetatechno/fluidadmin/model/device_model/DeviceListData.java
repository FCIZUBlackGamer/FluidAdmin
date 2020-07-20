package com.thetatechno.fluidadmin.model.device_model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeviceListData {
    @SerializedName("data")
    List<Device> deviceList = null;
    @SerializedName("status")
    String status;

    public List<Device> getDeviceList() {
        return deviceList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
