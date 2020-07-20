package com.thetatechno.fluidadmin.network.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.AddOrUpdateStatus;
import com.thetatechno.fluidadmin.model.AddOrUpdateStatusResponse;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponseModel;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.utils.Constants;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BranchesRepository {
    MutableLiveData<BranchesResponseModel> branchesMutableLiveData = new MutableLiveData<>();
    MutableLiveData<AddOrUpdateStatusResponse> addNewBranchMutableLiveData = new MutableLiveData<>();
    MutableLiveData<AddOrUpdateStatusResponse> updateBranchMutableLiveData = new MutableLiveData<>();
    private static String TAG = BranchesRepository.class.getSimpleName();

    public MutableLiveData<BranchesResponseModel> getAllBranches(String language) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<BranchesResponse> call = myServicesInterface.getBranches(language);
        call.enqueue(new Callback<BranchesResponse>() {
            @Override
            public void onResponse(Call<BranchesResponse> call, Response<BranchesResponse> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                        branchesMutableLiveData.setValue(new BranchesResponseModel(response.body()));
                }
                else  {
                    branchesMutableLiveData.setValue(new BranchesResponseModel("Failed to load branches."));
                }
            }

            @Override
            public void onFailure(Call<BranchesResponse> call, Throwable t) {
                branchesMutableLiveData.setValue(new BranchesResponseModel("Error,Try again"));

                t.printStackTrace();
            }
        });
        return branchesMutableLiveData;
    }

    public MutableLiveData<AddOrUpdateStatusResponse> addNewBranch(final Branch branch) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<AddOrUpdateStatusResponse> call = myServicesInterface.addBranch(branch);
        call.enqueue(new Callback<AddOrUpdateStatusResponse>() {

            @Override
            public void onResponse(@NotNull Call<AddOrUpdateStatusResponse> call, @NotNull Response<AddOrUpdateStatusResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                            addNewBranchMutableLiveData.setValue(response.body());

                    } else
                        addNewBranchMutableLiveData.setValue(null);
                } else {
                    addNewBranchMutableLiveData.setValue(null);

                }
            }

            @Override
            public void onFailure(@NotNull Call<AddOrUpdateStatusResponse> call, Throwable t) {
                call.cancel();
                t.printStackTrace();
                addNewBranchMutableLiveData.setValue(null);
            }

        });
        return addNewBranchMutableLiveData;

    }

    public MutableLiveData<AddOrUpdateStatusResponse> updateBranch(final Branch branch) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<AddOrUpdateStatusResponse> call = myServicesInterface.updateBranch(branch);
        call.enqueue(new Callback<AddOrUpdateStatusResponse>() {

            @Override
            public void onResponse(@NonNull Call<AddOrUpdateStatusResponse> call, @NonNull Response<AddOrUpdateStatusResponse> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "updateBranch: response " + response.toString());
                    updateBranchMutableLiveData.setValue(response.body());
                } else
                    updateBranchMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<AddOrUpdateStatusResponse> call, Throwable t) {
                t.printStackTrace();
                updateBranchMutableLiveData.setValue(null);
            }

        });
        return updateBranchMutableLiveData;

    }

    public void deleteBranch(final String branchId, final OnDataChangedCallBackListener<Integer> onDataChangedCallBackListener) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<AddOrUpdateStatus> call = myServicesInterface.deleteBranch(branchId);
        call.enqueue(new Callback<AddOrUpdateStatus>() {

            @Override
            public void onResponse(@NonNull Call<AddOrUpdateStatus> call, @NonNull Response<AddOrUpdateStatus> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "deleteBranch response " + response.toString());
                    if (response.body() != null)
                            onDataChangedCallBackListener.onResponse(response.body().getStatus());


                } else
                    onDataChangedCallBackListener.onResponse(null);
            }

            @Override
            public void onFailure(@NonNull Call<AddOrUpdateStatus> call, @NonNull Throwable t) {
                call.cancel();
                onDataChangedCallBackListener.onResponse(null);
                t.printStackTrace();
            }

        });

    }
}
