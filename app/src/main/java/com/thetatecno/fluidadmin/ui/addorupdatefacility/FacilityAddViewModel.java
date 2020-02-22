package com.thetatecno.fluidadmin.ui.addorupdatefacility;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.FacilityRepository;
import com.thetatecno.fluidadmin.utils.Constants;

public class FacilityAddViewModel extends ViewModel {
    MutableLiveData<String> facilityAddOrUpdateMessage = new MutableLiveData<>();
    FacilityRepository facilityRepository = new FacilityRepository();
    String message ;
    public MutableLiveData<String> addNewFacility(Facility facility){
        facilityRepository.insertFacility(facility, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if(b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE))
                    message = "Added facility successfully";
                else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE))
                    message = "Failed to add facility.";
                facilityAddOrUpdateMessage.setValue(message);

            }
        });
        return facilityAddOrUpdateMessage;
    }
    public MutableLiveData<String> updateFacility(Facility facility){
        facilityRepository.updateFacility(facility, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if(b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE))
                    message = "updated facility successfully";
                else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE))
                    message = "Failed to update facility.";
                facilityAddOrUpdateMessage.setValue(message);

            }
        });
        return facilityAddOrUpdateMessage;
    }

}
