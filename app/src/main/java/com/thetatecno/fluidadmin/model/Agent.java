package com.thetatecno.fluidadmin.model;

import java.util.ArrayList;
import java.util.List;

public class Agent extends Person {
    private List<Facility> facilities = new ArrayList<>();

    public List<Facility> getFacilities() {
        return facilities;
    }

    public void setFacilities(List<Facility> facilities) {
        this.facilities = facilities;
    }
}
