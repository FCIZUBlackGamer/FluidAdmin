package com.thetatechno.fluidadmin.listeners;

import com.thetatechno.fluidadmin.model.facility_model.FacilityCodes;

public interface OnConfirmLinkToFacilityListener {
    public void  onConfirmLinkToFacility(String staffId,FacilityCodes facilityCodes);
}
