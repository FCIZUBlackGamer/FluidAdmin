package com.thetatechno.fluidadmin.ui;

import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Validation;


public class ValidationViewModel extends ViewModel {

    private String firstNameValidateMessage;
    private String genderValidateMessage;
    private String emailValidateMessage;
    private String passwordValidateMessage;
    private String imgUrlValidateMessage;
    private String dateOfBirthValidateMessage;
    private String customerIdValidateMessage;

    private boolean isValidEmail(String email) {
        if (!Validation.isValidEmail(email))
            return false;
        else
            return true;
    }

    private boolean isValidPassword(String password) {
        if (!Validation.isValidPassword(password))
            return false;
        else
            return true;

    }

    private boolean isValidFirstName(String firstName) {
        if (!Validation.isValidForName(firstName))
            return false;
        else
            return true;

    }

    private boolean isValidFamilyName(String familyName) {
        if (!Validation.isValidForName(familyName))
            return false;
        else
            return true;

    }

    private boolean isValidForID(String customerId) {
        if (!Validation.isValidForId(customerId))
            return false;
        else
            return true;

    }


    public String validateEmail(String email) {
        if (isValidEmail(email)) {
            emailValidateMessage = "";
        } else {
            emailValidateMessage = "Enter a Valid Email";
        }
        return emailValidateMessage;
    }


    public String validateFirstName(String firstName) {
        if (isValidFirstName(firstName)) {
            firstNameValidateMessage = "";

        } else {
            firstNameValidateMessage = "First Name is required Field";
        }
        return firstNameValidateMessage;
    }

    public String validateMiddleName(String middleName) {
        String middleNameValidateMessage;
        if (isValidFirstName(middleName)) {
            middleNameValidateMessage = "";
        } else {
            middleNameValidateMessage = "First Name is required Field";
        }
        return middleNameValidateMessage;
    }

    public String validateFamilyName(String familyName) {
        String familyNameValidateMessage;
        if (isValidFamilyName(familyName)) {
            familyNameValidateMessage = "";
        } else {
            familyNameValidateMessage = "Family Name is required Field";
        }
        return familyNameValidateMessage;
    }


    public String validateCustomerId(String customerId) {
        if (isValidForID(customerId)) {
            customerIdValidateMessage = "";

        } else {
            customerIdValidateMessage = "Medical Id is required Field";
        }
        return customerIdValidateMessage;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        if (!Validation.isValidNumber(phoneNumber))
            return false;
        else
            return true;

    }

    public String validatePhoneNumber(String phoneNumber) {
        String phoneNumberMessage;
        if (isValidPhoneNumber(phoneNumber)) {
            phoneNumberMessage = "";

        } else {
            phoneNumberMessage = App.getContext().getResources().getString(R.string.invalid_mobile_number_txt);
        }
        return phoneNumberMessage;
    }
    public String validateDateOfBirth(String dateOfBirth){
        if(isValidDate(dateOfBirth))
        {
            dateOfBirthValidateMessage = "";

        }
        else {
            dateOfBirthValidateMessage = App.getContext().getResources().getString(R.string.invalid_date);
        }
        return dateOfBirthValidateMessage;
    }

    private boolean isValidDate(String dateOfBirth) {
        if(dateOfBirth.trim().isEmpty())
        return  false;
        else
        {
            return true;
        }
    }


}
