package com.thetatecno.fluidadmin.model;

public class Code {
    private String description;
    private String codeType ;
    private String code ;
    private String userCode;
    private String systemRequired;

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
