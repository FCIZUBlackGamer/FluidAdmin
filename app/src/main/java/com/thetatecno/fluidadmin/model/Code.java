package com.thetatecno.fluidadmin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Code implements Serializable {

    @SerializedName("CODE_TYPE")
    @Expose
    private String codeType;
    @SerializedName("CODE")
    @Expose
    private String code;
    @SerializedName("SYSTEM_REQUIRED")
    @Expose
    private String systemRequired;

    @SerializedName("USER_CODE")
    @Expose
    private String userCode;
    @SerializedName("DESCRIPTION")
    @Expose
    private String description;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getSystemRequired() {
        return systemRequired;
    }

    public void setSystemRequired(String systemRequired) {
        this.systemRequired = systemRequired;
    }


}
