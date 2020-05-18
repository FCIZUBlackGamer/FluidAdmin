package com.thetatechno.fluidadmin.network.repositories;

import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.model.CustomerList;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientRepository {
    MutableLiveData<CustomerList> clientsMutableLiveData = new MutableLiveData<>();
    private static String TAG = ClientRepository.class.getSimpleName();

    public MutableLiveData getAllClients(final String facilityId, final String langId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<CustomerList> call = myServicesInterface.getAllClients(facilityId, langId);
        call.enqueue(new Callback<CustomerList>() {
            @Override
            public void onResponse(Call<CustomerList> call, Response<CustomerList> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {

                    if (response.body() != null) {
                        clientsMutableLiveData.setValue(response.body());

                    }
                }
            }

            @Override
            public void onFailure(Call<CustomerList> call, Throwable t) {
                clientsMutableLiveData.setValue(null);

            }
        });
        return clientsMutableLiveData;
    }
}
