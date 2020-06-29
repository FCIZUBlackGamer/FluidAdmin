package com.thetatechno.fluidadmin.model.staff_model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thetatechno.fluidadmin.model.Person;
import com.thetatechno.fluidadmin.model.facility_model.Facility;

import java.util.ArrayList;
import java.util.List;

public class Staff extends Person implements Parcelable {
    public Staff(Parcel in) {
        staffId = in.readString();
        specialityCode = in.readString();
        typeCode = in.readString();
        speciality = in.readString();
        langId = in.readString();
    }

    public static final Creator<Staff> CREATOR = new Creator<Staff>() {
        @Override
        public Staff createFromParcel(Parcel in) {
            return new Staff(in);
        }

        @Override
        public Staff[] newArray(int size) {
            return new Staff[size];
        }
    };

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
    private List<Facility> facilityList ;
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

    public Staff() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(staffId);
        dest.writeString(specialityCode);
        dest.writeString(typeCode);
        dest.writeString(speciality);
        dest.writeString(langId);
    }

    @NonNull
    @Override
    public String toString() {
        return getFirstName() + " " + getFamilyName();
    }
}
