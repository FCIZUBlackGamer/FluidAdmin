package com.thetatecno.fluidadmin.retrofiteServices.repositories;

import androidx.lifecycle.MutableLiveData;

import com.thetatecno.fluidadmin.model.CodeList;
import com.thetatecno.fluidadmin.model.CustomerList;
import com.thetatecno.fluidadmin.retrofiteServices.interfaces.MyServicesInterface;
import com.thetatecno.fluidadmin.retrofiteServices.interfaces.RetrofitInstance;
import com.thetatecno.fluidadmin.utils.Constants;

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
