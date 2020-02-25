package com.thetatecno.fluidadmin.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.model.Facilities;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.FacilityRepository;

public class FacilityListViewModel extends ViewModel {
    FacilityRepository facilityRepository = new FacilityRepository();

    public MutableLiveData<Facilities> getFacilityDataForClinics(String facilityId, String langId, String typeCode) {

        return facilityRepository.getAllFacilities(facilityId, langId, typeCode);
    }
}
