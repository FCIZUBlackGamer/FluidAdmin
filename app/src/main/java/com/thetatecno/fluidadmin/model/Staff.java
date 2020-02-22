package com.thetatecno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Staff extends Person implements Serializable {
    @SerializedName("staffId")
    @Expose
    private String staffId;
    @SerializedName("specialityCode")
    @Expose
    private String specialityCode;
    @SerializedName("typeCode")
    @Expose
    private String typeCode;
    @SerializedName("FACLITIES")
    @Expose
    private List<Facility> facilityList = new ArrayList<>();
    @SerializedName("speciality")
    @Expose
    private String speciality;
    @SerializedName("langId")
    @Expose
    private String langId;

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getSpecialityCode() {
        return specialityCode;
    }

    public void setSpecialityCode(String specialityCode) {
        this.specialityCode = specialityCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public List<Facility> getFacilityList() {
        return facilityList;
    }

    public void setFacilityList(List<Facility> facilityList) {
        this.facilityList = facilityList;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

}
