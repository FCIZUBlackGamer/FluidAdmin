package com.thetatecno.fluidadmin.model;

import java.util.List;

public class Person {
    private List<FullName> fullNameList;
    private String email;
    private String mobileNumber;
    private String gender;
    private String code;
    String imageLink;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public List<FullName> getFullNameList() {
        return fullNameList;
    }

    public void setFullNameList(List<FullName> fullNameList) {
        this.fullNameList = fullNameList;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
