package com.thetatechno.fluidadmin.model.device_model;

public class DeviceListModel {
    DeviceListData deviceListData;
    String errorMessage;

    public DeviceListModel(DeviceListData deviceListData) {
        this.deviceListData = deviceListData;
    }

    public DeviceListModel(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public DeviceListData getDeviceListData() {
        return deviceListData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
