package com.thetatecno.fluidadmin.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.retrofiteServices.repositories.CodeRepository;

public class CodeListViewModel extends ViewModel {
    CodeRepository codeRepository = new CodeRepository();
    public MutableLiveData getDataForCode(String facilityId, String langId) {

        return codeRepository.getAllCodes(facilityId, langId);
    }
}
