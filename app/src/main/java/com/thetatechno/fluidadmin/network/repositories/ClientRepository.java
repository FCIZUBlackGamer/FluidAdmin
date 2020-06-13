package com.thetatechno.fluidadmin.network.repositories;

import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.listeners.OnHandlingErrorCallback;
import com.thetatechno.fluidadmin.model.ClientData;
import com.thetatechno.fluidadmin.model.ClientListModel;
import com.thetatechno.fluidadmin.model.ClientModelForRegister;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientRepository {
    MutableLiveData<ClientListModel> clientListModelMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ClientData> clientsMutableLiveData = new MutableLiveData<>();

    MutableLiveData<Status> statusMutableLiveData = new MutableLiveData<>();
    private static String TAG = ClientRepository.class.getSimpleName();

    public MutableLiveData<ClientListModel> getAllClients(final String langId, OnHandlingErrorCallback onHandlingErrorCallback) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<ClientData> call = myServicesInterface.getAllClients( langId);
        call.enqueue(new Callback<ClientData>() {
            @Override
            public void onResponse(Call<ClientData> call, Response<ClientData> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    clientListModelMutableLiveData.setValue(new ClientListModel(response.body()));
                }
                clientListModelMutableLiveData.setValue(null);

            }

            @Override
            public void onFailure(Call<ClientData> call, Throwable t) {
                onHandlingErrorCallback.onError();

            }
        });
        return clientListModelMutableLiveData;
    }
    public MutableLiveData getAllClients(final String langId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<ClientData> call = myServicesInterface.getAllClients( langId);
        call.enqueue(new Callback<ClientData>() {
            @Override
            public void onResponse(Call<ClientData> call, Response<ClientData> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {

                    if (response.body() != null) {
                        clientsMutableLiveData.setValue(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<ClientData> call, Throwable t) {
                clientsMutableLiveData.setValue(null);

            }
        });
        return clientsMutableLiveData;
    }

    public MutableLiveData addNewClient(ClientModelForRegister customer){
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Status> call = myServicesInterface.addNewPatient(customer);
        call.enqueue(new Callback<Status>() {

            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    statusMutableLiveData.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {

                statusMutableLiveData.setValue(null);
                t.printStackTrace();
            }

        });
        return statusMutableLiveData;
    }
}
