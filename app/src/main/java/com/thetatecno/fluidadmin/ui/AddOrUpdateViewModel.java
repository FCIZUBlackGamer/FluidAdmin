package com.thetatecno.fluidadmin.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.OnDataChangedCallBackListener;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.StaffRepository;

public class AddOrUpdateViewModel extends ViewModel {
    StaffRepository staffRepository = new StaffRepository();
    MutableLiveData<String> addedSuccessLiveData = new MutableLiveData<>();
    String message ;
    public MutableLiveData<String> addNewStaff(Staff staff){
        staffRepository.insertNewStaff(staff, new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean b) {
                if(b.booleanValue())
                    message = "Added successfully";
                else
                    message = "Failed to add.";
                addedSuccessLiveData.setValue(message);

            }
        });
        return addedSuccessLiveData;
    }
    public MutableLiveData<String> updateStaff(Staff staff){
        staffRepository.updateStaff(staff, new OnDataChangedCallBackListener<Boolean>() {
            @Override
            public void onResponse(Boolean b) {
                if(b.booleanValue())
                    message = "updated successfully";
                else
                    message = "Failed to update.";
                addedSuccessLiveData.setValue(message);

            }
        });
        return addedSuccessLiveData;
    }
}
