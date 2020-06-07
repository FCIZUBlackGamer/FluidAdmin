package com.thetatechno.fluidadmin.network.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.model.StaffData;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffRepository {

    MutableLiveData<StaffData> facilitiesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Staff> staffMutableLiveData = new MutableLiveData<>();
    private static String TAG = StaffRepository.class.getSimpleName();

    public MutableLiveData getAllStuff(final String langId, final String typeCode) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<StaffData> call = myServicesInterface.getAllStuff(langId, typeCode);
        call.enqueue(new Callback<StaffData>() {
            @Override
            public void onResponse(Call<StaffData> call, Response<StaffData> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {

                    if (response.body() != null) {
                        facilitiesMutableLiveData.setValue(response.body());

                    } else {
                        Log.e(TAG, "No Data for body");
                    }
                } else if (response.code() == 404) {
                    Log.e(TAG, " server error 404 not found ");

                }
            }

            @Override
            public void onFailure(Call<StaffData> call, Throwable t) {
                facilitiesMutableLiveData.setValue(null);
                t.printStackTrace();

            }
        });
        return facilitiesMutableLiveData;
    }

    public MutableLiveData getAllProvidersInSpeciality(final String langId, final String typeCode, final String specialityCode, String providerId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<StaffData> call = myServicesInterface.getAllProviders(langId, typeCode, specialityCode, providerId);
        call.enqueue(new Callback<StaffData>() {
            @Override
            public void onResponse(Call<StaffData> call, Response<StaffData> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {

                    if (response.body() != null) {
                        facilitiesMutableLiveData.setValue(response.body());

                    } else {
                        Log.e(TAG, "No Data for body");
                    }
                } else if (response.code() == 404) {
                    Log.e(TAG, " server error 404 not found ");

                }
            }

            @Override
            public void onFailure(Call<StaffData> call, Throwable t) {
                facilitiesMutableLiveData.setValue(null);
                t.printStackTrace();

            }
        });
        return facilitiesMutableLiveData;
    }

    public MutableLiveData<Staff> getProviderData(final String langId, final String typeCode, final String specialityCode, String providerId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<StaffData> call = myServicesInterface.getAllProviders(langId, typeCode, specialityCode, providerId);
        call.enqueue(new Callback<StaffData>() {
            @Override
            public void onResponse(Call<StaffData> call, Response<StaffData> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {

                    if (response.body() != null) {
                        if (response.body().getStaffList() != null) {

                            Staff provider = response.body().getStaffList().get(0);
                            staffMutableLiveData.setValue(provider);
                        }
                        else
                            staffMutableLiveData.setValue(null);
                    }
                    staffMutableLiveData.setValue(null);


                }
                    else if(response.body().getStatus()!=null)
                    staffMutableLiveData.setValue(null);


            }

            @Override
            public void onFailure(Call<StaffData> call, Throwable t) {
                staffMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });
        return staffMutableLiveData;
    }

    public void insertNewStaff(final Staff staff, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Status> call = myServicesInterface.insertNewStuff(staff);
        call.enqueue(new Callback<Status>() {

            @Override
            public void onResponse(@NonNull Call<Status> call, @NonNull Response<Status> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus() != null) {
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());
                        Log.i(TAG, "insertNewStaff: response " + response.body().getStatus());
                    }


                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
                t.printStackTrace();

            }

        });
    }

    public void updateStaff(final Staff staff, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Status> call = myServicesInterface.updateStaff(staff);
        call.enqueue(new Callback<Status>() {

            @Override
            public void onResponse(@NonNull Call<Status> call, @NonNull Response<Status> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "updateAgent: response " + response.body().getStatus());
                    if (response.body().getStatus() != null) {
                        Log.i(TAG, "updateAgent: response " + response.body().getStatus());
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());
                    }

                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
                t.printStackTrace();

            }

        });

    }

    public void deleteStaff(final String staffId, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Status> call = myServicesInterface.deleteStuff(staffId);
        call.enqueue(new Callback<Status>() {

            @Override
            public void onResponse(@NonNull Call<Status> call, @NonNull Response<Status> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "deleteStaff: response " + response.toString());
                    if (response.body().getStatus() != null)
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());

                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
                t.printStackTrace();

            }

        });

    }
}
