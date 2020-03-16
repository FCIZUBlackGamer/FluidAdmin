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

public class AddOrUpdateProviderViewModel extends ViewModel {

    private StaffRepository staffRepository = new StaffRepository();
    private CodeRepository codeRepository = new CodeRepository();
    private MutableLiveData<String> addedSuccessLiveData = new MutableLiveData<>();
    private MutableLiveData<List<String>> specialtiesMutableLiveData = new MutableLiveData<>();
    private String message = "";
    private List<Code> specialityCodeList = new ArrayList<>();

    public MutableLiveData<String> addNewProvider(Staff staff, String specialityDescription) {
        String specialityCode = getSpecialityCodeFromSelectedDescription(specialityDescription);
        if (!specialityCode.isEmpty()) {
            staff.setSpecialityCode(specialityCode);
        }
        staffRepository.insertNewStaff(staff, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (Integer.parseInt(b) > 0) {
                    message = "Added successfully";
                    addedSuccessLiveData.setValue(message);
                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE))
                    message = "Failed to add.";
                addedSuccessLiveData.setValue(message);

            }
        });
        return addedSuccessLiveData;
    }


    public MutableLiveData<String> updateProvider(Staff staff, String specialityDescription) {
        String specialityCode = getSpecialityCodeFromSelectedDescription(specialityDescription);
        if (!specialityCode.isEmpty()) {
            staff.setSpecialityCode(specialityCode);
        }
        staffRepository.updateStaff(staff, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (Integer.parseInt(b) > 0) {
                    message = "updated successfully";
                    addedSuccessLiveData.setValue(message);
                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE))
                    message = "Failed to update.";
                addedSuccessLiveData.setValue(message);

            }
        });
        return addedSuccessLiveData;
    }

    private String getSpecialityCodeFromSelectedDescription(String codeDescription) {
        if (!codeDescription.isEmpty()) {
            for (Code code : specialityCodeList) {
                if (code.getDescription().equals(codeDescription))
                    return code.getCode();
            }
        }
        return "";
    }

    public MutableLiveData<List<String>> getSpecialities() {
        String codeType = EnumCode.Code.STFFGRP.toString();
        String langId = PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase();
        codeRepository.getAllCodes(codeType, langId, new OnDataChangedCallBackListener<CodeList>() {
            @Override
            public void onResponse(CodeList codeListData) {
                if (codeListData.getCodeList() != null) {
                    specialityCodeList = codeListData.getCodeList();
                    List<String> specialities = new ArrayList<>();
                    specialities.add("");
                    for (Code code : codeListData.getCodeList()) {
                        specialities.add(code.getDescription());
                    }
                    specialtiesMutableLiveData.setValue(specialities);
                } else {
                    specialtiesMutableLiveData.setValue(null);
                }

            }
        });
        return specialtiesMutableLiveData;
    }

}

