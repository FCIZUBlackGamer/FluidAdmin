package com.thetatechno.fluidadmin.network.repositories;

import androidx.lifecycle.MutableLiveData;


import com.thetatechno.fluidadmin.model.appointment_model.AppointmentListData;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentListRepository {
    private MutableLiveData<AppointmentListData> mutableLiveData = new MutableLiveData<>();
   AppointmentListData myAppointmentsData= new AppointmentListData();
    public MutableLiveData<AppointmentListData> getAppointmentList(final String providerId, String date) {

        MyServicesInterface myServicesInterface =  RetrofitInstance.getService();
        Call<AppointmentListData> call = myServicesInterface.getAppointments(providerId,date);
        call.enqueue(new Callback<AppointmentListData>() {
            @Override
            public void onResponse(Call<AppointmentListData> call, Response<AppointmentListData> response) {
                EspressoTestingIdlingResource.increment();
                if (response.isSuccessful()) {
                    AppointmentListData AppointmentListData = response.body();
                    if(AppointmentListData != null && AppointmentListData.getAppointments() !=null ) {
                        myAppointmentsData =  AppointmentListData;
                        mutableLiveData.setValue(myAppointmentsData);
                    }
                } else {
                    mutableLiveData.setValue(myAppointmentsData);
                }
                EspressoTestingIdlingResource.decrement();
            }

            @Override
            public void onFailure(Call<AppointmentListData> call, Throwable t) {
                t.printStackTrace();
                mutableLiveData.setValue(null);
            }
        });
        return mutableLiveData;
    }
}
