package com.thetatecno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Person implements Serializable {
    @SerializedName("firstName")
    @Expose
    private String firstName = "";
    @SerializedName("familyName")
    @Expose
    private String familyName = "";
    @SerializedName("email")
    @Expose
    private String email = "";
    @SerializedName("mobile")
    @Expose
    private String mobileNumber = "";
    @SerializedName("gender")
    @Expose
    private String gender = "";
    @SerializedName("id")
    @Expose
    private String id = "";
    @SerializedName("imageFile")
    @Expose
    String imageLink = "";

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
