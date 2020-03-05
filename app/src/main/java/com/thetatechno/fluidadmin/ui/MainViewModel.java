package com.thetatechno.fluidadmin.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.model.FacilityCodes;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.model.StaffData;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.ClientRepository;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.CodeRepository;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.FacilityRepository;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.PreferenceController;

public class MainViewModel extends ViewModel {
    final static String TAG = MainViewModel.class.getSimpleName();
    StaffRepository staffRepository = new StaffRepository();
    CodeRepository codeRepository = new CodeRepository();
    FacilityRepository facilityRepository = new FacilityRepository();
    ClientRepository clientRepository = new ClientRepository();
    MutableLiveData<String> deletedMessageLiveData = new MutableLiveData<>();
    String message;


    public MutableLiveData<StaffData> getStaffDataForAgents(String langId, String typeCode) {

        return staffRepository.getAllStuff(langId, typeCode);
    }

    public MutableLiveData<StaffData> getStaffDataForProviders(String langId, String typeCode) {

        return staffRepository.getAllStuff(langId, typeCode);
    }

    public MutableLiveData getFacilityDataForClinics(String facilityId, String langId) {

        return facilityRepository.getAllFacilities(facilityId, langId);
    }

    public MutableLiveData getAllClients(String facilityId, String langId) {

        return clientRepository.getAllClients(facilityId, langId);
    }

    public MutableLiveData getDataForCode(String facilityId, String langId) {

        return codeRepository.getAllCodes(facilityId, langId);
    }

    public MutableLiveData<String> deleteAgentOrProvider(final Staff staff) {
        staffRepository.deleteStaff(staff.getStaffId(), new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                Log.i(TAG, "delete state " + b);
                if (b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE)) {
                    message = "Delete successfully staff " + staff.getFirstName();
                    deletedMessageLiveData.setValue(message);
                } else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE)) {
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
                if (b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE)) {
                    message = "Delete code successfully " + code.getCode();
                    codeRepository.getAllCodes("", PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
                } else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE)) {
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
                if (b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE)) {
                    message = "Delete facility successfully" + facility.getCode();
                    facilityRepository.getAllFacilities(facility.getCode(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
                } else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE)) {
                    message = "Failed to delete code " + facility.getCode();
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
