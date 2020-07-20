package com.thetatechno.fluidadmin.network.repositories;

import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponseModel;
import com.thetatechno.fluidadmin.model.device_model.DeviceListData;
import com.thetatechno.fluidadmin.model.device_model.DeviceListModel;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceRepository {
    MutableLiveData<DeviceListModel> devicesMutableLiveData = new MutableLiveData<>();

    private static String TAG = DeviceRepository.class.getSimpleName();


    public MutableLiveData<DeviceListModel> getAllDevices() {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<DeviceListData> call = myServicesInterface.getAllDevicesList();
        call.enqueue(new Callback<DeviceListData>() {
            @Override
            public void onResponse(Call<DeviceListData> call, Response<DeviceListData> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        devicesMutableLiveData.setValue(new DeviceListModel(response.body()));
                    }
                } else {
                    devicesMutableLiveData.setValue(new DeviceListModel("Failed to get devices list."));
                }
            }

            @Override
            public void onFailure(Call<DeviceListData> call, Throwable t) {
                devicesMutableLiveData.setValue(new DeviceListModel("Failed to get devices list."));
            }
        });
        return devicesMutableLiveData;
    }
}
