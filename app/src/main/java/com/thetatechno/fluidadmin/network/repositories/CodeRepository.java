package com.thetatechno.fluidadmin.network.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.AddOrUpdateStatus;
import com.thetatechno.fluidadmin.model.code_model.Code;
import com.thetatechno.fluidadmin.model.code_model.CodeList;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.model.specialities_model.SpecialityCodeListModel;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeRepository {
    MutableLiveData<SpecialityCodeListModel> codesMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<CodeList> idListMutableLiveData = new MutableLiveData<>();
    private static String TAG = CodeRepository.class.getSimpleName();

    public MutableLiveData<SpecialityCodeListModel> getAllCodes(final String codeType, final String langId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<CodeList> call = myServicesInterface.getCodes(codeType, langId);
        call.enqueue(new Callback<CodeList>() {
            @Override
            public void onResponse(Call<CodeList> call, Response<CodeList> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "get All codes response " + response.toString());
                        codesMutableLiveData.setValue(new SpecialityCodeListModel(response.body()));
                }
                else {
                    codesMutableLiveData.setValue(new SpecialityCodeListModel(" Failed to load."));
                }
            }

            @Override
            public void onFailure(Call<CodeList> call, Throwable t) {
                codesMutableLiveData.setValue(new SpecialityCodeListModel("Error,Try again"));

            }
        });
        return codesMutableLiveData;
    }

    public void getAllCodes(final String codeType, final String langId,final OnDataChangedCallBackListener<CodeList> onDataChangedCallBackListener) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<CodeList> call = myServicesInterface.getCodes(codeType, langId);
        call.enqueue(new Callback<CodeList>() {
            @Override
            public void onResponse(Call<CodeList> call, Response<CodeList> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "get All codes response " + response.toString());
                    if (response.body() != null) {
                        onDataChangedCallBackListener.onResponse(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<CodeList> call, Throwable t) {
                onDataChangedCallBackListener.onResponse(null);

            }
        });

    }

    public void insertCode(final Code code, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<AddOrUpdateStatus> call = myServicesInterface.addCode(code);
        call.enqueue(new Callback<AddOrUpdateStatus>() {

            @Override
            public void onResponse(Call<AddOrUpdateStatus> call, Response<AddOrUpdateStatus> response) {
                Log.i(TAG, "insertCode: response " + response.toString());
                if (response.isSuccessful()) {
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());
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

    public void updateCode(final Code code, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<AddOrUpdateStatus> call = myServicesInterface.updateCode(code);
        call.enqueue(new Callback<AddOrUpdateStatus>() {

            @Override
            public void onResponse(@NonNull Call<AddOrUpdateStatus> call, @NonNull Response<AddOrUpdateStatus> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "updateCode: response " + response.toString());
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());


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

    public void deleteCode(final String codeType, final String code, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Status> call = myServicesInterface.deleteCode(code, codeType);
        call.enqueue(new Callback<Status>() {

            @Override
            public void onResponse(@NonNull Call<Status> call, @NonNull Response<Status> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "delete code response " + response.toString());
                    if (response.body() != null)
                        if(response.body().getStatus()!=null)
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());


                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(@NonNull Call<Status> call, @NonNull Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
            }

        });

    }
    public MutableLiveData<CodeList> getAllIDTypes(final String codeType, final String langId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<CodeList> call = myServicesInterface.getIDTypesCode(codeType, langId);
        EspressoTestingIdlingResource.increment();
        call.enqueue(new Callback<CodeList>() {
            @Override
            public void onResponse(Call<CodeList> call, Response<CodeList> response) {
                EspressoTestingIdlingResource.increment();
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "get All codes response " + response.toString());
                    if (response.body() != null) {
                        idListMutableLiveData.setValue(response.body());

                    }
                }
                EspressoTestingIdlingResource.decrement();
            }

            @Override
            public void onFailure(Call<CodeList> call, Throwable t) {
                idListMutableLiveData.setValue(null);

            }
        });
        EspressoTestingIdlingResource.decrement();
        return idListMutableLiveData;
    }

}
