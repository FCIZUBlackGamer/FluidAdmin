package com.thetatechno.fluidadmin.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Code implements Serializable {

    @SerializedName("codeType")
    @Expose
    private String codeType;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("systemRequired")
    @Expose
    private String systemRequired;

    @SerializedName("userCode")
    @Expose
    private String userCode;
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("langId")
    @Expose
    private String langId;

    public String getLangId() {
        return langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

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
