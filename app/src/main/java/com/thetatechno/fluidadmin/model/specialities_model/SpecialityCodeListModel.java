package com.thetatechno.fluidadmin.model.specialities_model;

import com.thetatechno.fluidadmin.model.code_model.CodeList;
import com.thetatechno.fluidadmin.model.staff_model.StaffData;

public class SpecialityCodeListModel {
    private String errorMessage;
    private CodeList codeList;

    public SpecialityCodeListModel(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public SpecialityCodeListModel(CodeList codeList) {
        this.codeList = codeList;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public CodeList getCodeList() {
        return codeList;
    }

}
