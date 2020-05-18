package com.thetatechno.fluidadmin.ui.stafflist;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.StaffData;
import com.thetatechno.fluidadmin.network.repositories.StaffRepository;

public class StaffListViewModel extends ViewModel {
    StaffRepository staffRepository = new StaffRepository();

    public MutableLiveData<StaffData> getStaffData(String langId, String typeCode) {

        return staffRepository.getAllStuff(langId, typeCode);
    }


}
