package com.thetatechno.fluidadmin.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.model.FacilityCodes;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.CodeRepository;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.FacilityRepository;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.Constants;

public class MainViewModel extends ViewModel {
    final static String TAG = MainViewModel.class.getSimpleName();
    StaffRepository staffRepository = new StaffRepository();
    CodeRepository codeRepository = new CodeRepository();
    FacilityRepository facilityRepository = new FacilityRepository();
    MutableLiveData<String> deletedMessageLiveData = new MutableLiveData<>();
    String message;

    public MutableLiveData<String> deleteAgentOrProvider(final Staff staff) {
        staffRepository.deleteStaff(staff.getStaffId(), new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                Log.i(TAG, "deleteAgentOrProvider: delete state " + b);
                if (b.equals(Constants.DELETE_SUCCESS_STATE)) {
                    message = "Delete successfully " + staff.getFirstName();
                    deletedMessageLiveData.setValue(message);
                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    message = "Failed to delete.";
                    deletedMessageLiveData.setValue(message);
                }


            }
        });
        return deletedMessageLiveData;
    }

    public MutableLiveData<String> deleteCode(final Code code) {
        codeRepository.deleteCode(code.getCodeType(), code.getCode(), new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                Log.i(TAG, "deleteCode: delete state " + b);

                if (b.equals(Constants.DELETE_SUCCESS_STATE)) {
                    message = "Delete code successfully " + code.getCode();
//                    codeRepository.getAllCodes(EnumCode.Code.STFFGRP.toString(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
                    deletedMessageLiveData.setValue(message);

                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    message = "Failed to delete code " + code.getCode();
                    deletedMessageLiveData.setValue(message);
                }

            }
        });
        return deletedMessageLiveData;
    }

    public MutableLiveData<String> deleteFacility(final Facility facility) {
        facilityRepository.deleteFacility(facility.getCode(), new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                Log.i(TAG, "deleteFacility: delete state " + b);

                if (b.equals(Constants.DELETE_SUCCESS_STATE)) {
                    message = "Delete facility successfully" + facility.getCode();
//                    facilityRepository.getAllFacilities(facility.getCode(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
                    deletedMessageLiveData.setValue(message);

                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    message = "Failed to delete facility " + facility.getCode();
                    deletedMessageLiveData.setValue(message);
                }

            }
        });
        return deletedMessageLiveData;
    }

    public void linkToFacility(String staffId, FacilityCodes facilityCodes, final OnDataChangedCallBackListener<String> onDataChangedCallBackListener) {
        facilityRepository.linkToFacility(staffId, facilityCodes, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                onDataChangedCallBackListener.onResponse(b);
            }
        });
    }

}
