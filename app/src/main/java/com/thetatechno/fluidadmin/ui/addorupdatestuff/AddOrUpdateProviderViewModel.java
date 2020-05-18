package com.thetatechno.fluidadmin.ui.addorupdatestuff;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.code_model.Code;
import com.thetatechno.fluidadmin.model.code_model.CodeList;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.network.repositories.CodeRepository;
import com.thetatechno.fluidadmin.network.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;
import com.thetatechno.fluidadmin.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class AddOrUpdateProviderViewModel extends ViewModel {

    private StaffRepository staffRepository = new StaffRepository();
    private CodeRepository codeRepository = new CodeRepository();
    private MutableLiveData<String> addedSuccessLiveData = new MutableLiveData<>();
    private MutableLiveData<List<String>> specialtiesMutableLiveData = new MutableLiveData<>();
    private String message = "";
    private String firstNameValidateMessage, familyNameValidateMessage, emailValidateMessage, specialityValidateMessage, idValidateMessage, phoneNumberMessage;
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

    private boolean isValidEmail(String email) {
        if (!Validation.isValidEmail(email))
            return false;
        else
            return true;
    }

    private boolean isValidPassword(String password) {
        if (!Validation.isValidPassword(password))
            return false;
        else
            return true;

    }

    private boolean isValidFirstName(String firstName) {
        if (!Validation.isValidForName(firstName.trim()))
            return false;
        else
            return true;

    }

    private boolean isValidFamilyName(String familyName) {
        if (!Validation.isValidForName(familyName))
            return false;
        else
            return true;

    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if (!Validation.isValidPhoneNumber(phoneNumber))
            return false;
        else
            return true;

    }

    private boolean isValidForID(String id) {
        if (!Validation.isValidForId(id))
            return false;
        else
            return true;

    }


    public String validateEmail(String email) {
        if (isValidEmail(email)) {
            emailValidateMessage = "";
        } else {
            emailValidateMessage = App.getContext().getResources().getString(R.string.error_message_for_email);
        }

        return emailValidateMessage;
    }

    public String validatePhoneNumber(String phoneNumber) {
        if (isValidPhoneNumber(phoneNumber)) {
            phoneNumberMessage = "";

        } else {
            phoneNumberMessage = App.getContext().getResources().getString(R.string.error_message_for_number);
        }
        return phoneNumberMessage;
    }

    public String validateFirstName(String firstName) {
        if (isValidFirstName(firstName)) {
            firstNameValidateMessage = "";

        } else {
            firstNameValidateMessage = App.getContext().getResources().getString(R.string.aphabets_error_message);
        }
        return firstNameValidateMessage;
    }

    public String validateFamilyName(String familyName) {
        if (isValidFamilyName(familyName)) {
            familyNameValidateMessage = "";
        } else {
            familyNameValidateMessage = App.getContext().getResources().getString(R.string.aphabets_error_message);
        }
        return familyNameValidateMessage;
    }

    public String validateSpeciality(String speciality) {
        if (isValidSpeciality(speciality)) {
            specialityValidateMessage = "";
        } else {
            specialityValidateMessage = App.getContext().getResources().getString(R.string.choose_speciality_error_message);
        }
        return specialityValidateMessage;
    }

    private boolean isValidSpeciality(String speciality) {
        if (Validation.isValidForSpeciality(speciality))
        {
            for (Code code : specialityCodeList) {
                if (code.getDescription().equals(speciality))
                    return true;
            }
            return false;

        }
        else
            return false;
    }

    public String validateId(String customerId) {
        if (isValidForID(customerId)) {
            idValidateMessage = "";

        } else {
            idValidateMessage = App.getContext().getResources().getString(R.string.id_error_message);
        }
        return idValidateMessage;
    }

}

