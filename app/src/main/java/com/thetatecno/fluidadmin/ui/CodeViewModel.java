package com.thetatecno.fluidadmin.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.OnDataChangedCallBackListener;
import com.thetatecno.fluidadmin.model.Code;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.CodeRepository;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.StaffRepository;

public class CodeViewModel extends ViewModel {
MutableLiveData<String> codeAddOrUpdateMessage = new MutableLiveData<>();
    CodeRepository codeRepository = new CodeRepository();
    String message ;
    public MutableLiveData<String> addNewCode(Code code){
        codeRepository.insertCode(code, new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean b) {
                if(b.booleanValue())
                    message = "Add code successfully";
                else
                    message = "Failed to add code.";
                codeAddOrUpdateMessage.setValue(message);

            }
        });
        return codeAddOrUpdateMessage;
    }
    public MutableLiveData<String> updateCode(Code code){
        codeRepository.updateCode(code, new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean b) {
                if(b.booleanValue())
                    message = "updated code successfully";
                else
                    message = "Failed to update code.";
                codeAddOrUpdateMessage.setValue(message);

            }
        });
        return codeAddOrUpdateMessage;
    }
}
