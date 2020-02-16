package com.thetatecno.fluidadmin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.model.CodeList;
import com.thetatecno.fluidadmin.model.CustomerList;
import com.thetatecno.fluidadmin.model.Facilities;
import com.thetatecno.fluidadmin.model.StaffData;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.ClientRepository;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.CodeRepository;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.FacilityRepository;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.StaffRepository;

public class MainViewModel extends ViewModel {
    StaffRepository staffRepository = new StaffRepository();
    CodeRepository codeRepository = new CodeRepository();
    FacilityRepository facilityRepository = new FacilityRepository();
    ClientRepository clientRepository = new ClientRepository();


    public MutableLiveData<StaffData> getStaffDataForAgents(String langId, String typeCode) {

        return staffRepository.getAllStuff(langId, typeCode);
    }

    public MutableLiveData<StaffData> getStaffDataForProviders(String langId, String typeCode) {

        return staffRepository.getAllStuff(langId, typeCode);
    }

    public MutableLiveData<Facilities> getStaffDataForClinics(String facilityId, String langId, String typeCode) {

        return facilityRepository.getAllFacilities(facilityId, langId, typeCode);
    }

    public MutableLiveData<CustomerList> getStaffDataForPerson(String facilityId, String langId) {

        return clientRepository.getAllClients(facilityId, langId);
    }

    public MutableLiveData<CodeList> getStaffDataForCode(String facilityId, String langId) {

        return codeRepository.getAllCodes(facilityId, langId);
    }

}
