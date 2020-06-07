package com.thetatechno.fluidadmin.model.branches_model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Branch implements Serializable {
    @SerializedName("siteId")
    @Expose
    private String siteId;

    @SerializedName("langId")
    private String langId;

    @SerializedName("description")
    @Expose
    private String description = "";

    @SerializedName("emailAddress")
    @Expose
    private String email = "";
    @SerializedName("mobileNo")
    @Expose
    private String mobileNumber = "";
    @SerializedName("address")
    @Expose
    private String address = "";

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    @NonNull
    @Override
    public String toString() {
        return description;
    }
}
