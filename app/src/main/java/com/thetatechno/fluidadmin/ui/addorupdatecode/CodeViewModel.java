package com.thetatechno.fluidadmin.ui.addorupdatecode;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.CodeRepository;
import com.thetatechno.fluidadmin.utils.Constants;

public class CodeViewModel extends ViewModel {
MutableLiveData<String> codeAddOrUpdateMessage = new MutableLiveData<>();
    CodeRepository codeRepository = new CodeRepository();
    String message ;
    public MutableLiveData<String> addNewCode(Code code){
        codeRepository.insertCode(code, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if(b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE))
                    message = "Add code successfully";
                else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE))
                    message = "Failed to add code.";
                codeAddOrUpdateMessage.setValue(message);

            }
        });
        return codeAddOrUpdateMessage;
    }
    public MutableLiveData<String> updateCode(Code code){
        codeRepository.updateCode(code, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if(b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE))
                    message = "updated code successfully";
                else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE))
                    message = "Failed to update code.";
                codeAddOrUpdateMessage.setValue(message);

            }
        });
        return codeAddOrUpdateMessage;
    }
}
