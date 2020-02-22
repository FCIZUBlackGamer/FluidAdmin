package com.thetatecno.fluidadmin;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.model.StaffData;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.StaffRepository;

public class AgentListViewModel extends ViewModel {
    StaffRepository staffRepository = new StaffRepository();

    public MutableLiveData<StaffData> getStaffDataForAgents(String langId, String typeCode) {

        return staffRepository.getAllStuff(langId, typeCode);
    }


}
