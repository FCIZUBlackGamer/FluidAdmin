package com.thetatechno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientModelForRegister {

    @SerializedName("clientId")
    @Expose
    private String clientId;
    @SerializedName("firstName")
    @Expose
    private String firstName= "";
    @SerializedName("middleName")
    @Expose
    private String middleName = "";
    @SerializedName("familyName")
    @Expose
    private String familyName = " ";
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("mobileNo")
    @Expose
    private String mobile;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("sexCode")
    @Expose
    private String sexCode;
    @SerializedName("sexDescription")
    @Expose
    private String sexDescription;
    @SerializedName("dateOfBirth")
    @Expose
    private String dateOfBirth = "";
    @SerializedName("nationalityCode")
    private String nationalityCode = "";
    @SerializedName("nationalityDesc")
    private String nationalityDesc = "";
    @SerializedName("firebaseToken")
    @Expose
    private String firebaseToken = "";
    @SerializedName("personalId")
    @Expose
    private String personalId;
    @SerializedName("personId")
    @Expose
    private String personId;
    @SerializedName("gardianId")
    private String gardianId ="";
    @SerializedName("personalIdCode")
    @Expose
    private String personalIdCode;
    @SerializedName("imagePath")
    @Expose
    private String imageFile = "";
    @SerializedName("langId")
    @Expose
    private String langId = "EN";

    public ClientModelForRegister(String mobile, String token) {

        this.firebaseToken = token;
        this.mobile = mobile;

    }

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

    public String getSexCode() {
        return sexCode;
    }

    public void setSexCode(String sexCode) {
        this.sexCode = sexCode;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getPersonalIdCode() {
        return personalIdCode;
    }

    public void setPersonalIdCode(String personalIdCode) {
        this.personalIdCode = personalIdCode;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String token) {
        this.firebaseToken = token;
    }

    public String getPhone() {
        return mobile;
    }

    public void setPhone(String mobile) {
        this.mobile = mobile;
    }

    public String getSexDescription() {
        return sexDescription;
    }

    public void setSexDescription(String sexDescription) {
        this.sexDescription = sexDescription;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationalityDescription() {
        return nationalityDesc;
    }

    public void setNationalityDescription(String nationalityDescription) {
        this.nationalityDesc = nationalityDescription;
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

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getGardianId() {
        return gardianId;
    }

    public void setGardianId(String gardianId) {
        this.gardianId = gardianId;
    }
}
