package com.thetatecno.fluidadmin.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatecno.fluidadmin.model.Code;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.model.StaffData;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.ClientRepository;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.CodeRepository;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.FacilityRepository;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.StaffRepository;
import com.thetatecno.fluidadmin.utils.App;
import com.thetatecno.fluidadmin.utils.Constants;
import com.thetatecno.fluidadmin.utils.PreferenceController;

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

    public MutableLiveData getStaffDataForClinics(String facilityId, String langId, String typeCode) {

        return facilityRepository.getAllFacilities(facilityId, langId, typeCode);
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
                Log.i(TAG,"delete state "+b);
                if (b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE)) {
                    message = "Delete successfully staff " + staff.getFirstName();
                    staffRepository.getAllStuff(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), staff.getTypeCode());
                } else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE)){
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
                    facilityRepository.getAllFacilities(facility.getCode(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(),facility.getType());
                }
                else if(b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE)) {
                    message = "Failed to delete code " + facility.getCode();
                    deletedMessageLiveData.setValue(message);
                }

            }
        });
        return deletedMessageLiveData;
    }

}
