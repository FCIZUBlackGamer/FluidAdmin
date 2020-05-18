package com.thetatechno.fluidadmin.network.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.State;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.code_model.Code;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.utils.Constants;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BranchesRepository {
    MutableLiveData<BranchesResponse> branchesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> addNewBranchMutableLiveData = new MutableLiveData<>();
    private static String TAG = BranchesRepository.class.getSimpleName();

    public MutableLiveData<BranchesResponse> getAllBranches(String language) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<BranchesResponse> call = myServicesInterface.getBranches(language);
        call.enqueue(new Callback<BranchesResponse>() {
            @Override
            public void onResponse(Call<BranchesResponse> call, Response<BranchesResponse> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "get All codes response " + response.toString());
                    if (response.body() != null) {
                        branchesMutableLiveData.setValue(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<BranchesResponse> call, Throwable t) {
                branchesMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });
        return branchesMutableLiveData;
    }

    public MutableLiveData<String> addNewBranch(final Branch branch) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.addBranch(branch);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NotNull Call<State> call, @NotNull Response<State> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() != null)
                            addNewBranchMutableLiveData.setValue(response.body().getStatus());

                    } else
                        addNewBranchMutableLiveData.setValue(null);
                } else {
                    addNewBranchMutableLiveData.setValue(null);

                }
            }

            @Override
            public void onFailure(@NotNull Call<State> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                addNewBranchMutableLiveData.setValue(null);
            }

        });
        return addNewBranchMutableLiveData;

    }

    public void updateBranch(final Branch branch, final OnDataChangedCallBackListener<String> onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.updateBranch(branch);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NonNull Call<State> call, @NonNull Response<State> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "updateBranch: response " + response.toString());
                    if (response.body().getStatus() != null)
                        onDataChangedCallBackListener.onResponse(response.body().getStatus());


                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(Call<State> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                onDataChangedCallBackListener.onResponse(null);
            }

        });

    }

    public void deleteBranch(final String branchId, final OnDataChangedCallBackListener<String> onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<State> call = myServicesInterface.deleteBranch(branchId);
        call.enqueue(new Callback<State>() {

            @Override
            public void onResponse(@NonNull Call<State> call, @NonNull Response<State> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "deleteBranch response " + response.toString());
                    if (response.body() != null)
                        if (response.body().getStatus() != null)
                            onDataChangedCallBackListener.onResponse(response.body().getStatus());


                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(@NonNull Call<State> call, @NonNull Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
                t.printStackTrace();
            }

        });

    }
}
