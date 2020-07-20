package com.thetatechno.fluidadmin.ui.clientList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnHandlingErrorCallback;
import com.thetatechno.fluidadmin.model.ClientListModel;
import com.thetatechno.fluidadmin.network.repositories.ClientRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.CheckForNetwork;
import com.thetatechno.fluidadmin.utils.PreferenceController;

public class ClientListViewModel extends ViewModel implements OnHandlingErrorCallback {
   private ClientRepository clientRepository = new ClientRepository();
   private MutableLiveData<ClientListModel> clientListModelMutableLiveData = new MutableLiveData<>();

    public void getAllClients() {

        String langId = PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase();
        if(CheckForNetwork.isConnectionOn(App.getContext()))
        clientListModelMutableLiveData = clientRepository.getAllClients(langId,this);
        else {
            onNoNetworkConnection();
        }

    }

    @Override
    public void onNoNetworkConnection() {
        clientListModelMutableLiveData.setValue(new ClientListModel(R.string.no_internet_connection));
    }

    @Override
    public void onError() {
        clientListModelMutableLiveData.setValue(new ClientListModel(R.string.fail_to_load_clients_try_again_txt));

    }
    public LiveData<ClientListModel> getClientData() {
        return clientListModelMutableLiveData;
    }

}
