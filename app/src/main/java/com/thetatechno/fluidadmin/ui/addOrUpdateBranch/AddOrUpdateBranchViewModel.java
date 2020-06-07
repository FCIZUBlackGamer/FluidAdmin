package com.thetatechno.fluidadmin.ui.addOrUpdateBranch;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.network.repositories.BranchesRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Validation;

public class AddOrUpdateBranchViewModel extends ViewModel {
    private BranchesRepository branchesRepository = new BranchesRepository();
    private MutableLiveData<String> updateBranchLiveData = new MutableLiveData<>();
    Branch branch = new Branch();

    private  String branchDescriptionValidateMessage,branchAddressValidateMessage,emailValidateMessage,imgUrlValidateMessage,idValidateMessage , phoneNumberMessage;
    public  MutableLiveData<String> addNewBranch(String description,String address,String email,String phone)
    {
        branch.setDescription(description);
        branch.setAddress(address);
        branch.setEmail(email);
        branch.setMobileNumber(phone);
        return branchesRepository.addNewBranch(branch);
    }
    public MutableLiveData<String> updateBranch(Branch branch) {
        branchesRepository.updateBranch(branch, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {

            }
        });
        return updateBranchLiveData;
    }



    private Branch setDataToBranch(String description,String address,String email,String phone) {
        branch.setDescription(description);
        branch.setAddress(address);
        branch.setEmail(email);
        branch.setMobileNumber(phone);
        return branch;
    }
    private boolean isValidEmail(String email){
        if (!Validation.isValidEmail(email))
            return false;
        else
            return true;
    }
    private boolean isValidDescription(String description){
        if (!Validation.isValidForWord(description)) {
            return false;
        }
        else
            return true;

    }
    private boolean isValidAddress(String familyName){
        if (!Validation.isValidForWord(familyName))
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
    public String validateDescription(String description) {
        if (isValidDescription(description)) {
            branchDescriptionValidateMessage = "";
        } else {
            branchDescriptionValidateMessage = App.getContext().getResources().getString(R.string.aphabets_error_message);

            return branchDescriptionValidateMessage;
        }
        return branchDescriptionValidateMessage;
    }
    public String validateAddress(String address){
        if (isValidAddress(address)) {
            branchAddressValidateMessage ="";
        }
        else{
            branchAddressValidateMessage  = App.getContext().getResources().getString(R.string.address_error_message);
        }
        return branchAddressValidateMessage;
    }

}
