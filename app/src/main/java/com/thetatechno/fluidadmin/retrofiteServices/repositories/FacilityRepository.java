package com.thetatechno.fluidadmin.retrofiteServices.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Facilities;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.model.FacilityCodes;
import com.thetatechno.fluidadmin.model.State;
import com.thetatechno.fluidadmin.retrofiteServices.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.retrofiteServices.interfaces.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacilityRepository {
    MutableLiveData<Facilities> facilitiesMutableLiveData = new MutableLiveData<>();
    private static String TAG = FacilityRepository.class.getSimpleName();

    public MutableLiveData getAllFacilities(final String facilityId, final String langId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Facilities> call = myServicesInterface.getAllFacilities(facilityId, langId);
        call.enqueue(new Callback<Facilities>() {
            @Override
            public void onResponse(Call<Facilities> call, Response<Facilities> response) {
                if (response.isSuccessful()) {
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
    public void getFacilityListForSpecificType(final String facilityId, final String langId, final String typeCode, final OnDataChangedCallBackListener<Facilities> onDataChangedCallBackListener) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Facilities> call = myServicesInterface.getAllWaitingListFacilities(facilityId, langId,typeCode);
        call.enqueue(new Callback<Facilities>() {
            @Override
            public void onResponse(Call<Facilities> call, Response<Facilities> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        onDataChangedCallBackListener.onResponse(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<Facilities> call, Throwable t) {
                onDataChangedCallBackListener.onResponse(null);

            }
        });

    }
    public void insertFacility(final Facility facility, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.addFacility(facility);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NonNull Call<State> call, @NonNull Response<State> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "insertFacility: response " + response.toString());
                    if (response.body().getStatus() != null) {
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());
                        Log.i(TAG, "insertFacility: status " + response.body().getStatus());
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

    public void updateFacility(final Facility facility, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.updateFacility(facility);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NonNull Call<State> call, @NonNull Response<State> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "updateFacility: response " + response.toString());
                    if (response.body().getStatus() != null) {
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());
                        Log.i(TAG, "updateFacility: status " + response.body().getStatus());

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

    public void deleteFacility(final String facilityId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.deleteFacility(facilityId);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NonNull Call<State> call, @NonNull Response<State> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "deleteFacility: response " + response.toString());
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

    public void linkToFacility(final String facilityId, FacilityCodes facilityCodes, final OnDataChangedCallBackListener<String> onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.addToFacilities(facilityId, facilityCodes);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NonNull Call<State> call, @NonNull Response<State> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "linkToFacility: response " + response.toString());
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
