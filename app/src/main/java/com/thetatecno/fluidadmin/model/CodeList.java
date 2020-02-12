package com.thetatecno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CodeList {
    @SerializedName("data")
    @Expose
    private List<Code> codeList = null;

    public List<Code> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<Code> data) {
        this.codeList = data;
    }
}
