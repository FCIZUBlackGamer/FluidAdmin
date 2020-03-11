package com.thetatechno.fluidadmin.ui.addorupdatestuff;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.model.CodeList;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.CodeRepository;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.ArrayList;
import java.util.List;

public class AddOrUpdateViewModel extends ViewModel {
    StaffRepository staffRepository = new StaffRepository();
    CodeRepository codeRepository = new CodeRepository();
    MutableLiveData<String> addedSuccessLiveData = new MutableLiveData<>();
    MutableLiveData<List<String>> specialtiesMutableLiveData = new MutableLiveData<>();
    String message = "";

    public MutableLiveData<String> addNewStaff(Staff staff) {
        staffRepository.insertNewStaff(staff, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE)) {
                    message = "Added successfully";
                    addedSuccessLiveData.setValue(message);
                } else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE))
                    message = "Failed to add.";
                addedSuccessLiveData.setValue(message);

            }
        });
        return addedSuccessLiveData;
    }

    public MutableLiveData<String> updateStaff(Staff staff) {
        staffRepository.updateStaff(staff, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE)) {
                    message = "updated successfully";
                    addedSuccessLiveData.setValue(message);
                } else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE))
                    message = "Failed to update.";
                addedSuccessLiveData.setValue(message);

            }
        });
        return addedSuccessLiveData;
    }

    public MutableLiveData<List<String>> getSpecialities() {
        String codeType = EnumCode.Code.STFFGRP.toString();
        String langId = PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase();
         codeRepository.getAllCodes(codeType, langId, new OnDataChangedCallBackListener<CodeList>() {
             @Override
             public void onResponse(CodeList codeListData) {
                 if(codeListData.getCodeList()!=null){
                     List<String> specialities = new ArrayList<>();

                     for(Code code : codeListData.getCodeList()){
                         specialities.add(code.getCode());
                     }
                     specialtiesMutableLiveData.setValue(specialities);
                 }
             }
         });
         return specialtiesMutableLiveData;
    }

}
