package com.thetatecno.fluidadmin.retrofiteServices.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.thetatecno.fluidadmin.OnDataChangedCallBackListener;
import com.thetatecno.fluidadmin.model.Code;
import com.thetatecno.fluidadmin.model.CodeList;
import com.thetatecno.fluidadmin.retrofiteServices.interfaces.MyServicesInterface;
import com.thetatecno.fluidadmin.retrofiteServices.interfaces.RetrofitInstance;
import com.thetatecno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeRepository {
    MutableLiveData<CodeList> codesMutableLiveData = new MutableLiveData<>();
    private static String TAG = CodeRepository.class.getSimpleName();

    public MutableLiveData getAllCodes(final String facilityId, final String langId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<CodeList> call = myServicesInterface.getCodes(facilityId, langId);
        call.enqueue(new Callback<CodeList>() {
            @Override
            public void onResponse(Call<CodeList> call, Response<CodeList> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {

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
        Call<Void> call = myServicesInterface.addCode(code);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "add code response " + response.toString());
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

    public void updateCode(final Code code, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Void> call = myServicesInterface.updateCode(code);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "update code response " + response.toString());
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

    public void deleteCode(final String codeType,final String code, final OnDataChangedCallBackListener onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Void> call = myServicesInterface.deleteCode(code,codeType);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "delete code response " + response.toString());
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
