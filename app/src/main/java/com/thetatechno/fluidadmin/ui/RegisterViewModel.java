package com.thetatechno.fluidadmin.ui;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.AddNewOrModifyClientResponse;
import com.thetatechno.fluidadmin.model.ClientModelForRegister;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.model.code_model.CodeList;
import com.thetatechno.fluidadmin.model.specialities_model.SpecialityCodeListModel;
import com.thetatechno.fluidadmin.network.repositories.ClientRepository;
import com.thetatechno.fluidadmin.network.repositories.CodeRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;
import com.thetatechno.fluidadmin.utils.Validation;

public class RegisterViewModel extends ViewModel {
    private String firstNameValidateMessage;
    private String genderValidateMessage;
    private String emailValidateMessage;
    private String passwordValidateMessage;
    private String imgUrlValidateMessage;
    private String dateOfBirthValidateMessage;
    private String customerIdValidateMessage;
    private CodeRepository codeRepository = new CodeRepository();
    private ClientRepository clientRepository = new ClientRepository();
    private String email, password, mobileNumber;
    MutableLiveData<Status> createPatientStatus = new MutableLiveData<>();
    private MutableLiveData<SpecialityCodeListModel> nationalityLiveData = new MutableLiveData<>();
    private MutableLiveData<CodeList> idTypeLiveData = new MutableLiveData<>();

    public RegisterViewModel() {

    }

    public MutableLiveData<SpecialityCodeListModel> getNationalityList() {
        nationalityLiveData = codeRepository.getAllCodes(EnumCode.Code.NTNLTY.toString(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
        return nationalityLiveData;
    }

    public MutableLiveData<CodeList> getIDTypes() {
        idTypeLiveData = null;
        idTypeLiveData = codeRepository.getAllIDTypes(EnumCode.Code.IDTYP.toString(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
        return idTypeLiveData;
    }

    public MutableLiveData addNewClient(String firstName, String middleName, String familyName, String email, String mobile, String dateOfBirth, String gender, String nationalityCode, String idTypeCode, String idNumber) {
        ClientModelForRegister client = new ClientModelForRegister();
        client.setFirstName(firstName);
        client.setMiddleName(middleName);
        client.setFamilyName(familyName);
        client.setEmail(email);
        client.setMobile(mobile);
        client.setDateOfBirth(dateOfBirth);
        client.setSexCode(gender);
        client.setNationalityCode(nationalityCode);
        client.setPersonalIdCode(idTypeCode);
        client.setPersonalId(idNumber);
        return clientRepository.addNewClient(client);
    }

    public MutableLiveData<AddNewOrModifyClientResponse> addNewCustomer(String firstName, String middleName, String familyName, String email, String mobile, String dateOfBirth,String guardId, String gender, String nationalityCode, String idTypeCode, String idNumber) {
        ClientModelForRegister client = new ClientModelForRegister();
        client.setFirstName(firstName);
        client.setMiddleName(middleName);
        client.setFamilyName(familyName);
        client.setEmail(email);
        client.setMobile(mobile);
        client.setDateOfBirth(dateOfBirth);
        client.setSexCode(gender);
        client.setNationalityCode(nationalityCode);
        client.setPersonalIdCode(idTypeCode);
        client.setGardianId(guardId);
        client.setPersonalId(idNumber);
        return clientRepository.addNewCustomer(client);
    }

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

    public String validateDateOfBirth(String dateOfBirth) {
        if (isValidDate(dateOfBirth)) {
            dateOfBirthValidateMessage = "";

        } else {
            dateOfBirthValidateMessage = App.getContext().getResources().getString(R.string.invalid_date);
        }
        return dateOfBirthValidateMessage;
    }

    private boolean isValidDate(String dateOfBirth) {
        if (dateOfBirth.trim().isEmpty())
            return false;
        else {
            return true;
        }
    }


}
