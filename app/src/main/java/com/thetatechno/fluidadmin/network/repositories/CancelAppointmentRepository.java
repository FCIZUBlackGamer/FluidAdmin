package com.thetatechno.fluidadmin.network.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelAppointmentRepository {
    private MutableLiveData<Status> statusMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<Status> cancelAppointment(String appointmentId) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Status> call = myServicesInterface.cancelAppointment(appointmentId);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    statusMutableLiveData.setValue(response.body());
                    if (response.body() != null)
                        Log.i("Response", response.body().getStatus());
                } else {
                    statusMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                System.out.println(t.getStackTrace().toString());
                statusMutableLiveData.setValue(null);
            }
        });
        return statusMutableLiveData;
    }

}
