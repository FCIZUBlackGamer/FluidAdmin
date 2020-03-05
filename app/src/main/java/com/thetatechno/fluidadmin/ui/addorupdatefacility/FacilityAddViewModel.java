package com.thetatechno.fluidadmin.ui.addorupdatefacility;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.FacilityRepository;
import com.thetatechno.fluidadmin.utils.Constants;

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
