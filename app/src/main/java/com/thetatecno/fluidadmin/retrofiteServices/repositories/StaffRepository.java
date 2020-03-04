package com.thetatecno.fluidadmin.retrofiteServices.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.thetatecno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.model.StaffData;
import com.thetatecno.fluidadmin.model.State;
import com.thetatecno.fluidadmin.retrofiteServices.interfaces.MyServicesInterface;
import com.thetatecno.fluidadmin.retrofiteServices.interfaces.RetrofitInstance;
import com.thetatecno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffRepository {

    MutableLiveData<StaffData> facilitiesMutableLiveData = new MutableLiveData<>();
    private static String TAG = StaffRepository.class.getSimpleName();
    public MutableLiveData getAllStuff( final String langId,final String typeCode) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<StaffData> call = myServicesInterface.getAllStuff(langId, typeCode);
        call.enqueue(new Callback<StaffData>() {
            @Override
            public void onResponse(Call<StaffData> call, Response<StaffData> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {

                    if (response.body() != null) {
                        facilitiesMutableLiveData.setValue(response.body());

                    }else {
                        Log.e("Res", "No Data for body");
                    }
                }else {
                    Log.e("Res", "No Data");
                }
            }

            @Override
            public void onFailure(Call<StaffData> call, Throwable t) {
                facilitiesMutableLiveData.setValue(null);
                Log.e("Error", t.getMessage());

            }
        });
        return facilitiesMutableLiveData;
    }

    public void insertNewStaff(final Staff staff, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.insertNewStuff(staff);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NonNull Call<State> call, @NonNull Response<State> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "insertNewStaff: response " + response.toString());
                    if (response.body().getStatus() != null)
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());


                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(Call<State> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
            }

        });
    }

    public void updateStaff(final Staff staff, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.updateStaff(staff);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NonNull Call<State> call, @NonNull Response<State> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "updateStaff: response " + response.body().getStatus());
                    if (response.body().getStatus() != null) {
                        Log.i(TAG, "updateStaff: response " + response.body().getStatus());
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());
                    }

                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(Call<State> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
            }

        });

    }

    public void deleteStaff(final String staffId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.deleteStuff(staffId);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NonNull Call<State> call, @NonNull Response<State> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "deleteStaff: response " + response.toString());
                    if (response.body().getStatus() != null)
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());

                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(Call<State> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
            }

        });

    }
}
