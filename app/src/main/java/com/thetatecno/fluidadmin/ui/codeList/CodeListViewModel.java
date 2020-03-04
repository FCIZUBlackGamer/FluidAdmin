package com.thetatecno.fluidadmin.ui.codeList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.model.CodeList;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.CodeRepository;

public class CodeListViewModel extends ViewModel {
    CodeRepository codeRepository = new CodeRepository();
    public MutableLiveData<CodeList> getDataForCode(String code, String langId) {

        return codeRepository.getAllCodes(code, langId);
    }
}
