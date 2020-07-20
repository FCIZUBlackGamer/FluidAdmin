package com.thetatechno.fluidadmin.ui.codeList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.code_model.CodeList;
import com.thetatechno.fluidadmin.model.specialities_model.SpecialityCodeListModel;
import com.thetatechno.fluidadmin.network.repositories.CodeRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.CheckForNetwork;

public class CodeListViewModel extends ViewModel {
    CodeRepository codeRepository = new CodeRepository();
    MutableLiveData<SpecialityCodeListModel> mutableLiveData = new MutableLiveData<>();
    public MutableLiveData<SpecialityCodeListModel> getDataForCode(String codeType, String langId) {
       if(CheckForNetwork.isConnectionOn(App.getContext()))
       {
           mutableLiveData = codeRepository.getAllCodes(codeType, langId);
       }
       else {
           mutableLiveData.setValue(new SpecialityCodeListModel("Error in network connection."));
       }
        return mutableLiveData;
    }
}
