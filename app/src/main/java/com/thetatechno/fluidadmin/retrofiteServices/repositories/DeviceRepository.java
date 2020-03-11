package com.thetatechno.fluidadmin.retrofiteServices.repositories;

import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.DeviceListData;
import com.thetatechno.fluidadmin.model.Facilities;
import com.thetatechno.fluidadmin.retrofiteServices.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.retrofiteServices.interfaces.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceRepository {
    MutableLiveData<DeviceListData> devicesMutableLiveData = new MutableLiveData<>();
    private static String TAG = DeviceRepository.class.getSimpleName();

    public MutableLiveData getAllDevices() {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<DeviceListData> call = myServicesInterface.getAllDevicesList();
        call.enqueue(new Callback<DeviceListData>() {
            @Override
            public void onResponse(Call<DeviceListData> call, Response<DeviceListData> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        devicesMutableLiveData.setValue(response.body());

                    }
                }
                else {
                    // TODO : handle another codes of api response
                }
            }

            @Override
            public void onFailure(Call<DeviceListData> call, Throwable t) {
                devicesMutableLiveData.setValue(null);

            }
        });
        return devicesMutableLiveData;
    }
    public void getAllDevices(final OnDataChangedCallBackListener<DeviceListData> onDataChangedCallBackListener) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<DeviceListData> call = myServicesInterface.getAllDevicesList();
        call.enqueue(new Callback<DeviceListData>() {
            @Override
            public void onResponse(Call<DeviceListData> call, Response<DeviceListData> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        onDataChangedCallBackListener.onResponse(response.body());


                    }
                }
                else {
                    // TODO : handle another codes of api response
                }
            }

            @Override
            public void onFailure(Call<DeviceListData> call, Throwable t) {
                onDataChangedCallBackListener.onResponse(null);
            }
        });

    }
}
