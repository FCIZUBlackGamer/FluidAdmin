package com.thetatecno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Person {
    @SerializedName("FIRST_NAME")
    @Expose
    private String firstName;
    @SerializedName("FAMILY_NAME")
    @Expose
    private String familyName;
    @SerializedName("EMAIL")
    @Expose
    private String email;
    @SerializedName("PHONE")
    @Expose
    private String mobileNumber;
    @SerializedName("SEX_CODE")
    @Expose
    private String gender;
//    @SerializedName("ID")
//    @Expose
    private String id;
    @SerializedName("IMAGE_PATH")
    @Expose
    String imageLink;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

}
