package com.thetatecno.fluidadmin.listeners;

import com.thetatecno.fluidadmin.utils.EnumCode;

public interface OnFragmentInteractionWithFacilityTypesListener extends onDisplayPlusBtnInterface {
    void onAddOrUpdateFacility(EnumCode.ClinicTypeCode clinicTypeCode);
    void onDisplayAddBtn();
}
