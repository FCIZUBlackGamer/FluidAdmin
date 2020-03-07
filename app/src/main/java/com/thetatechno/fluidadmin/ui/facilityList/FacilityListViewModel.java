package com.thetatechno.fluidadmin.ui.facilityList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Facilities;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.FacilityRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.ArrayList;
import java.util.List;

public class FacilityListViewModel extends ViewModel {
    FacilityRepository facilityRepository = new FacilityRepository();
    MutableLiveData<List<String>> facilitiesWaitListStringMutableLiveData = new MutableLiveData<List<String>>();

    public MutableLiveData<Facilities> getFacilityDataForClinics(String facilityId) {

        return facilityRepository.getAllFacilities(facilityId, PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }

    public MutableLiveData<List<String>> getFacilityDataForWaitingAreaList(String facilityId) {

        facilityRepository.getAllWaitListFacilities(facilityId, PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.ClinicTypeCode.WAITAREA.toString(), new OnDataChangedCallBackListener<Facilities>() {
            @Override
            public void onResponse(Facilities facilitiesListResponse) {
                if (facilitiesListResponse != null) {
                    if (!facilitiesListResponse.getFacilities().equals(null)) {
                        List<String> waitListIds = new ArrayList<>();
                        for (Facility facility : facilitiesListResponse.getFacilities()) {
                            waitListIds.add(facility.getCode());
                        }
                        facilitiesWaitListStringMutableLiveData.setValue(waitListIds);
                    } else {
                        facilitiesWaitListStringMutableLiveData.setValue(null);
                    }


                } else
                    facilitiesWaitListStringMutableLiveData.setValue(null);
            }
        });

        return facilitiesWaitListStringMutableLiveData;

    }

}
