package com.thetatechno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClientData {
    @SerializedName("items")
    @Expose
    private List<Person> personList = null;
    @SerializedName("error")
    @Expose
    private Error error;

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setFacilities(List<Person> data) {
        this.personList = data;
    }

}
