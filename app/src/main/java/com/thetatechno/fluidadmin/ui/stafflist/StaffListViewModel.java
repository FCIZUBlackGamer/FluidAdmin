package com.thetatechno.fluidadmin.ui.stafflist;

import android.app.Activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.staff_model.StaffData;
import com.thetatechno.fluidadmin.model.staff_model.StaffListModel;
import com.thetatechno.fluidadmin.network.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.CheckForNetwork;

public class StaffListViewModel extends ViewModel {
    StaffRepository staffRepository = new StaffRepository();
    MutableLiveData<StaffListModel> staffListModelMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<StaffListModel> getStaffData(String langId, String typeCode) {
        if (CheckForNetwork.isConnectionOn(App.getContext())) {
            staffListModelMutableLiveData = staffRepository.getAllStuff(langId, typeCode);

        }
        else
            staffListModelMutableLiveData.setValue(new StaffListModel("Error in connection"));

        return staffListModelMutableLiveData;

    }


}
