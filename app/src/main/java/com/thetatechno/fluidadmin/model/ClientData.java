package com.thetatechno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientData {
    @SerializedName("data")
    @Expose
    private List<Person> personList = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Person> getPersonList() {
        return personList;
    }

    public void setFacilities(List<Person> data) {
        this.personList = data;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
