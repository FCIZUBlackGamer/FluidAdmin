package com.thetatechno.fluidadmin.ui.codeList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.CodeList;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.CodeRepository;

public class CodeListViewModel extends ViewModel {
    CodeRepository codeRepository = new CodeRepository();
    public MutableLiveData<CodeList> getDataForCode(String codeType, String langId) {

        return codeRepository.getAllCodes(codeType, langId);
    }
}
