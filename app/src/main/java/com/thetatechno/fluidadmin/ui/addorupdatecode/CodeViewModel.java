package com.thetatechno.fluidadmin.ui.addorupdatecode;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.CodeRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.Validation;

public class CodeViewModel extends ViewModel {
    MutableLiveData<String> codeAddOrUpdateMessage = new MutableLiveData<>();
    CodeRepository codeRepository = new CodeRepository();
    String message;
    private String  idValidateMessage, descriptionMessage;

    public MutableLiveData<String> addNewCode(Code code) {
        codeRepository.insertCode(code, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (Integer.parseInt(b) > 0) {
                    message = "Add code successfully";
                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    message = "Failed to add code.";
                }
                codeAddOrUpdateMessage.setValue(message);

            }
        });
        return codeAddOrUpdateMessage;
    }

    public MutableLiveData<String> updateCode(Code code) {
        codeRepository.updateCode(code, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (Integer.parseInt(b) > 0) {
                    message = "updated code successfully";
                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    message = "Failed to update code.";
                }
                codeAddOrUpdateMessage.setValue(message);

            }
        });
        return codeAddOrUpdateMessage;
    }

    public String validateId(String id) {
        if (isValidForID(id)) {
            idValidateMessage = "";

        } else {
            idValidateMessage = App.getContext().getResources().getString(R.string.id_error_message);
        }
        return idValidateMessage;
    }
    public String validateDescription(String description) {
        if (isValidForDescription(description)) {
            descriptionMessage = "";

        } else {
            descriptionMessage = App.getContext().getResources().getString(R.string.description_error_message);
        }
        return descriptionMessage;
    }
    private boolean isValidForID(String id) {
        if (!Validation.isValidForId(id))
            return false;
        else
            return true;

    }
    private boolean isValidForDescription(String description){
        if(!Validation.isValidForWord(description))
            return false;
        else
            return true;
    }
}
