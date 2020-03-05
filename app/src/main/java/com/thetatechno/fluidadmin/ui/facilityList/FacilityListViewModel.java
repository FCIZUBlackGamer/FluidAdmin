package com.thetatechno.fluidadmin.ui.facilityList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.Facilities;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.FacilityRepository;

public class FacilityListViewModel extends ViewModel {
    FacilityRepository facilityRepository = new FacilityRepository();

    public MutableLiveData<Facilities> getFacilityDataForClinics(String facilityId, String langId) {

        return facilityRepository.getAllFacilities(facilityId, langId);
    }
}
