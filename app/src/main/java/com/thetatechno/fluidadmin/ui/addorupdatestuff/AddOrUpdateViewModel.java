package com.thetatechno.fluidadmin.ui.addorupdatestuff;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.network.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.Validation;

public class AddOrUpdateViewModel extends ViewModel {
    private StaffRepository staffRepository = new StaffRepository();
    private MutableLiveData<String> addedSuccessLiveData = new MutableLiveData<>();
    private String message = "";
    private  String firstNameValidateMessage,familyNameValidateMessage,emailValidateMessage,imgUrlValidateMessage,idValidateMessage , phoneNumberMessage;

    public MutableLiveData<String> addNewAgent(Staff staff) {
        staffRepository.insertNewStaff(staff, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (Integer.parseInt(b) > 0) {
                    message = "Added successfully";
                    addedSuccessLiveData.setValue(message);
                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE))
                    message = "Unfortunately failed to add.";
                addedSuccessLiveData.setValue(message);

            }
        });
        return addedSuccessLiveData;
    }

    public MutableLiveData<String> updateAgent(Staff staff) {
        staffRepository.updateStaff(staff, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (Integer.parseInt(b) > 0) {
                    message = "updated successfully";
                    addedSuccessLiveData.setValue(message);
                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE))
                    message = "Failed to update.";
                addedSuccessLiveData.setValue(message);

            }
        });
        return addedSuccessLiveData;
    }

    private boolean isValidEmail(String email){
        if (!Validation.isValidEmail(email))
            return false;
        else
            return true;
    }
    private boolean isValidFirstName(String firstName){
        if (!Validation.isValidForName(firstName)) {
            return false;
        }
        else
            return true;

    }
    private boolean isValidFamilyName(String familyName){
        if (!Validation.isValidForName(familyName))
            return false;
        else
            return true;

    }
    private boolean isValidPhoneNumber(String phoneNumber){
        if (!Validation.isValidPhoneNumber(phoneNumber))
            return false;
        else
            return true;

    }
    private boolean isValidForID(String customerId){
        if (!Validation.isValidForId(customerId))
            return false;
        else
            return true;

    }

    public String validateEmail(String email){
        if (isValidEmail(email)){
            emailValidateMessage ="";
        }
        else{
            emailValidateMessage  = App.getContext().getResources().getString(R.string.error_message_for_email);
        }

        return emailValidateMessage ;
    }
    public String validatePhoneNumber(String phoneNumber){
        if (isValidPhoneNumber(phoneNumber)) {
            phoneNumberMessage ="";

        }
        else{
            phoneNumberMessage = App.getContext().getResources().getString(R.string.error_message_for_number);
        }
        return phoneNumberMessage;
    }
    public String validateFirstName(String firstName){
        if (isValidFirstName(firstName)) {
            firstNameValidateMessage = "";

        }
        else{
            if(!firstName.matches("[a-zA-Z]]"))
            firstNameValidateMessage = App.getContext().getResources().getString(R.string.aphabets_error_message);
            else if(firstName.trim().isEmpty()) {
                firstNameValidateMessage = App.getContext().getResources().getString(R.string.first_name_error_message);

            }
        }
        return firstNameValidateMessage;
    }
    public String validateFamilyName(String familyName){
        if (isValidFamilyName(familyName)) {
            familyNameValidateMessage ="";
        }
        else{
            familyNameValidateMessage  = App.getContext().getResources().getString(R.string.aphabets_error_message);
        }
        return familyNameValidateMessage;
    }
    public String validateId(String customerId){
        if (isValidForID(customerId)) {
            idValidateMessage ="";

        }
        else{
            idValidateMessage = App.getContext().getResources().getString(R.string.id_error_message);
        }
        return idValidateMessage;
    }



}
