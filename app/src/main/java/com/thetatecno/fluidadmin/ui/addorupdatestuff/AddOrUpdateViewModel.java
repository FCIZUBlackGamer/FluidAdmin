package com.thetatecno.fluidadmin.ui.addorupdatestuff;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatecno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatecno.fluidadmin.model.CodeList;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.CodeRepository;
import com.thetatecno.fluidadmin.retrofiteServices.repositories.StaffRepository;
import com.thetatecno.fluidadmin.utils.Constants;

public class AddOrUpdateViewModel extends ViewModel {
    StaffRepository staffRepository = new StaffRepository();
    CodeRepository codeRepository = new CodeRepository();
    MutableLiveData<String> addedSuccessLiveData = new MutableLiveData<>();
    String message = "" ;
    public MutableLiveData<String> addNewStaff(Staff staff){
        staffRepository.insertNewStaff(staff, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if(b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE))
                    message = "Added successfully";
                else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE))
                    message = "Failed to add.";
                addedSuccessLiveData.setValue(message);

            }
        });
        return addedSuccessLiveData;
    }
    public MutableLiveData<String> updateStaff(Staff staff){
        staffRepository.updateStaff(staff, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if(b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE))
                    message = "updated successfully";
                else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE))
                    message = "Failed to update.";
                addedSuccessLiveData.setValue(message);

            }
        });
        return addedSuccessLiveData;
    }

}
