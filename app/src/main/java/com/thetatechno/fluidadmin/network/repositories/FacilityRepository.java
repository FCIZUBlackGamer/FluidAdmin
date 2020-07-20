package com.thetatechno.fluidadmin.network.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.AddOrUpdateStatus;
import com.thetatechno.fluidadmin.model.facility_model.FacilitiesResponse;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.facility_model.FacilityCodes;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.model.facility_model.FacilityListModel;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacilityRepository {
    MutableLiveData<FacilitiesResponse> facilitiesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<FacilityListModel> facilityListModelMutableLiveData = new MutableLiveData<>();
    private static String TAG = FacilityRepository.class.getSimpleName();

    public MutableLiveData<FacilityListModel> getAllFacilities(final String facilityId, final String langId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<FacilitiesResponse> call = myServicesInterface.getAllFacilities(facilityId, langId);
        call.enqueue(new Callback<FacilitiesResponse>() {
            @Override
            public void onResponse(Call<FacilitiesResponse> call, Response<FacilitiesResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        facilityListModelMutableLiveData.setValue(new FacilityListModel(response.body()));

                    }
                }
                else{
                    facilityListModelMutableLiveData.setValue(new FacilityListModel("Failed to load facilities."));

                }
            }

            @Override
            public void onFailure(Call<FacilitiesResponse> call, Throwable t) {
                facilityListModelMutableLiveData.setValue(new FacilityListModel("Error,Try again."));


            }
        });
        return facilityListModelMutableLiveData;
    }
    public MutableLiveData<FacilitiesResponse> getRoomFacilitiesForSpecificSiteID(final String siteId, final String langId, String typeCode) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<FacilitiesResponse> call = myServicesInterface.getFacilityForSpecificSiteId(siteId, langId,typeCode);
        call.enqueue(new Callback<FacilitiesResponse>() {
            @Override
            public void onResponse(Call<FacilitiesResponse> call, Response<FacilitiesResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        facilitiesMutableLiveData.setValue(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<FacilitiesResponse> call, Throwable t) {
                facilitiesMutableLiveData.setValue(null);

            }
        });
        return facilitiesMutableLiveData;
    }

    public void getFacilityListForSpecificType(final String facilityId, final String langId, final String typeCode, final OnDataChangedCallBackListener<FacilitiesResponse> onDataChangedCallBackListener) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<FacilitiesResponse> call = myServicesInterface.getAllWaitingListFacilities(facilityId, langId,typeCode);
        call.enqueue(new Callback<FacilitiesResponse>() {
            @Override
            public void onResponse(Call<FacilitiesResponse> call, Response<FacilitiesResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        onDataChangedCallBackListener.onResponse(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<FacilitiesResponse> call, Throwable t) {
                onDataChangedCallBackListener.onResponse(null);

            }
        });

    }
    public void insertFacility(final Facility facility, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<AddOrUpdateStatus> call = myServicesInterface.addFacility(facility);
        call.enqueue(new Callback<AddOrUpdateStatus>() {

            @Override
            public void onResponse(@NonNull Call<AddOrUpdateStatus> call, @NonNull Response<AddOrUpdateStatus> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "insertFacility: response " + response.toString());

                        onDataChangedCallBackListener.onResponse(response.body().getStatus());
                        Log.i(TAG, "insertFacility: status " + response.body().getStatus());


                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(Call<AddOrUpdateStatus> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
            }

        });


    }

    public void updateFacility(final Facility facility, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<AddOrUpdateStatus> call = myServicesInterface.updateFacility(facility);
        call.enqueue(new Callback<AddOrUpdateStatus>() {

            @Override
            public void onResponse(@NonNull Call<AddOrUpdateStatus> call, @NonNull Response<AddOrUpdateStatus> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "updateFacility: response " + response.toString());

                        onDataChangedCallBackListener.onResponse(response.body().getStatus());
                        Log.i(TAG, "updateFacility: status " + response.body().getStatus());

                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(Call<AddOrUpdateStatus> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
            }

        });


    }

    public void deleteFacility(final String facilityId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Status> call = myServicesInterface.deleteFacility(facilityId);
        call.enqueue(new Callback<Status>() {

            @Override
            public void onResponse(@NonNull Call<Status> call, @NonNull Response<Status> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "deleteFacility: response " + response.toString());
                    if (response.body().getStatus() != null)
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());


                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
            }

        });

    }

    public void linkToFacility(final String facilityId, FacilityCodes facilityCodes, final OnDataChangedCallBackListener<String> onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Status> call = myServicesInterface.addToFacilities(facilityId, facilityCodes);
        call.enqueue(new Callback<Status>() {

            @Override
            public void onResponse(@NonNull Call<Status> call, @NonNull Response<Status> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "linkToFacility: response " + response.toString());
                    if (response.body().getStatus() != null)
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());


                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
            }

        });

    }


}
