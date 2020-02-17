package com.thetatecno.fluidadmin.ui;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.OnDataChangedCallBackListener;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.FacilityRepository;

public class FacilityAddViewModel extends ViewModel {
    MutableLiveData<String> facilityAddOrUpdateMessage = new MutableLiveData<>();
    FacilityRepository facilityRepository = new FacilityRepository();
    String message ;
    public MutableLiveData<String> addNewFacility(Facility facility){
        facilityRepository.insertFacility(facility, new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean b) {
                if(b.booleanValue())
                    message = "Added facility successfully";
                else
                    message = "Failed to add facility.";
                facilityAddOrUpdateMessage.setValue(message);

            }
        });
        return facilityAddOrUpdateMessage;
    }
    public MutableLiveData<String> updateFacility(Facility facility){
        facilityRepository.updateFacility(facility, new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean b) {
                if(b.booleanValue())
                    message = "updated facility successfully";
                else
                    message = "Failed to update facility.";
                facilityAddOrUpdateMessage.setValue(message);

            }
        });
        return facilityAddOrUpdateMessage;
    }

}
