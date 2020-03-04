package com.thetatecno.fluidadmin.retrofiteServices.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.thetatecno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatecno.fluidadmin.model.Code;
import com.thetatecno.fluidadmin.model.CodeList;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.model.State;
import com.thetatecno.fluidadmin.retrofiteServices.interfaces.MyServicesInterface;
import com.thetatecno.fluidadmin.retrofiteServices.interfaces.RetrofitInstance;
import com.thetatecno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeRepository {
    MutableLiveData<CodeList> codesMutableLiveData = new MutableLiveData<>();
    private static String TAG = CodeRepository.class.getSimpleName();

    public MutableLiveData getAllCodes(final String code, final String langId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<CodeList> call = myServicesInterface.getCodes(code, langId);
        call.enqueue(new Callback<CodeList>() {
            @Override
            public void onResponse(Call<CodeList> call, Response<CodeList> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "get All codes response " + response.toString());
                    Gson gson = new Gson();
                    Log.e("REsult", gson.toJson(response.body()));
                    if (response.body() != null) {

                        codesMutableLiveData.setValue(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<CodeList> call, Throwable t) {
                codesMutableLiveData.setValue(null);

            }
        });
        return codesMutableLiveData;
    }

    public void insertCode(final Code code, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.addCode(code);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(Call<State> call, Response<State> response) {
                Log.i(TAG, "insertCode: response " + response.toString());
                if (response.isSuccessful()) {
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

    public void updateCode(final Code code, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.updateCode(code);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NonNull Call<State> call, @NonNull Response<State> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "updateCode: response " + response.toString());
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

    public void deleteCode(final String codeType, final String code, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.deleteCode(code, codeType);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NonNull Call<State> call,@NonNull Response<State> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "delete code response " + response.toString());
                    if (response.body() != null)
                        if(response.body().getStatus()!=null)
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());


                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure( @NonNull Call<State> call,@NonNull Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
            }

        });

    }
}
