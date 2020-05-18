package com.thetatechno.fluidadmin.ui.clientList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.network.repositories.ClientRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.PreferenceController;

public class ClientListViewModel extends ViewModel {
    ClientRepository clientRepository = new ClientRepository();

    public MutableLiveData getAllClients(String facilityId) {
        String langId = PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase();

        return clientRepository.getAllClients(facilityId, langId);
    }
}
