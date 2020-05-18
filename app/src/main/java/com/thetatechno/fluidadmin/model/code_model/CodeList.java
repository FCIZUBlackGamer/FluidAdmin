package com.thetatechno.fluidadmin.model.code_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CodeList {
    @SerializedName("data")
    @Expose
    private List<Code> codeList = null;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Code> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<Code> data) {
        this.codeList = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
