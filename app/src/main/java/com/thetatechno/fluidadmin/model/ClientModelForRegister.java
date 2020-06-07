package com.thetatechno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientModelForRegister {
    @SerializedName("clientId")
    @Expose
    private String clientId;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("middleName")
    @Expose
    private String middleName;
    @SerializedName("familyName")
    @Expose
    private String familyName;
    @SerializedName("emailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("mobileNo")
    @Expose
    private String mobile;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("genderCode")
    @Expose
    private String gender;
    @SerializedName("genderDesc")
    @Expose
    private String genderDesc;
    @SerializedName("dob")
    @Expose
    private String dateOfBirth;
    @SerializedName("nationalityCode")
    private String nationalityCode;
    @SerializedName("nationalityDesc")
    private String nationalityDesc;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("personalId")
    @Expose
    private String personalId;
    @SerializedName("idTypeCode")
    @Expose
    private String idTypeCode;
    @SerializedName("image_file")
    @Expose
    private String imageFile;
    @SerializedName("langId")
    @Expose
    private String langId;

    public ClientModelForRegister() {
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dateOfBirth;
    }

    public void setDob(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getIdTypeCode() {
        return idTypeCode;
    }

    public void setIdTypeCode(String idTypeCode) {
        this.idTypeCode = idTypeCode;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return mobile;
    }

    public void setPhone(String mobile) {
        this.mobile = mobile;
    }

    public String getGenderDesc() {
        return genderDesc;
    }

    public void setGenderDesc(String genderDesc) {
        this.genderDesc = genderDesc;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationalityDesc() {
        return nationalityDesc;
    }

    public void setNationalityDesc(String nationalityDesc) {
        this.nationalityDesc = nationalityDesc;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
