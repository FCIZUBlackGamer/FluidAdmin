package com.thetatecno.fluidadmin.retrofiteServices.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.thetatecno.fluidadmin.OnDataChangedCallBackListener;
import com.thetatecno.fluidadmin.model.Facilities;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.retrofiteServices.interfaces.MyServicesInterface;
import com.thetatecno.fluidadmin.retrofiteServices.interfaces.RetrofitInstance;
import com.thetatecno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacilityRepository {
    MutableLiveData<Facilities> facilitiesMutableLiveData = new MutableLiveData<>();


    public MutableLiveData getAllFacilities(final String facilityId, final String langId,final String typeCode) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Facilities> call = myServicesInterface.getFacilities(facilityId, langId,typeCode);
        call.enqueue(new Callback<Facilities>() {
            @Override
            public void onResponse(Call<Facilities> call, Response<Facilities> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {

                    if (response.body() != null) {
                        facilitiesMutableLiveData.setValue(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<Facilities> call, Throwable t) {
                facilitiesMutableLiveData.setValue(null);

            }
        });
        return facilitiesMutableLiveData;
    }

    public void insertFacility(final Facility facility, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Void> call = myServicesInterface.addFacility(facility);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i("Facility", "add Facility response " + response.toString());
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

    public void updateFacility(final Facility facility, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Void> call = myServicesInterface.updateFacility(facility);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i("Facility", "update Facility response " + response.toString());
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

    public void deleteFacility(final String facilityId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Void> call = myServicesInterface.deleteFacility(facilityId);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i("Facility", "delete Facility response " + response.toString());
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
}
