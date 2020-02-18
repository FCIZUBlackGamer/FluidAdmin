package com.thetatecno.fluidadmin.retrofiteServices.repositories;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.thetatecno.fluidadmin.OnDataChangedCallBackListener;
import com.thetatecno.fluidadmin.model.Facilities;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.model.Person;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.model.StaffData;
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

    public void insertNewStaff(final Staff staff, final OnDataChangedCallBackListener<Boolean> onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Void> call = myServicesInterface.insertNewStuff(staff);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "insertNewStaff: response " + response.toString());
                    onDataChangedCallBackListener.onResponse(true);
                } else
                    onDataChangedCallBackListener.onResponse(false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(false);
            }

        });

    }

    public void updateStaff(final Staff staff, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Void> call = myServicesInterface.updateStaff(staff);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "updateStaff: response " + response.toString());
                    onDataChangedCallBackListener.onResponse(true);
                } else
                    onDataChangedCallBackListener.onResponse(false);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(false);
            }

        });

    }

    public void deleteStaff(final String staffId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Void> call = myServicesInterface.deleteStuff(staffId);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "deleteStaff: response " + response.toString());
                    onDataChangedCallBackListener.onResponse(true);
                } else {
                    Log.i(TAG, "deleteStaff: state not ok  response " + response.toString());
                    onDataChangedCallBackListener.onResponse(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                call.cancel();
                Log.i(TAG, "deleteStaff: onFailure:  " + t.toString());
                onDataChangedCallBackListener.onResponse(false);
            }

        });

    }
}
