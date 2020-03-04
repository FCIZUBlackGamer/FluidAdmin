package com.thetatecno.fluidadmin.ui.clientList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.retrofiteServices.repositories.ClientRepository;

public class ClientListViewModel extends ViewModel {
    ClientRepository clientRepository = new ClientRepository();
    public MutableLiveData getAllClients(String facilityId, String langId) {

        return clientRepository.getAllClients(facilityId, langId);
    }
}
