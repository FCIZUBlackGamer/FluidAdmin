package com.thetatecno.fluidadmin.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.model.StaffData;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.StaffRepository;

public class StaffListViewModel extends ViewModel {
    StaffRepository staffRepository = new StaffRepository();

    public MutableLiveData<StaffData> getStaffData(String langId, String typeCode) {

        return staffRepository.getAllStuff(langId, typeCode);
    }


}
