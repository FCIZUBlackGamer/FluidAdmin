package com.thetatechno.fluidadmin.ui.addOrUpdateSession;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.APIResponse;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.facility_model.Facilities;
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.model.staff_model.StaffListModel;
import com.thetatechno.fluidadmin.network.repositories.BranchesRepository;
import com.thetatechno.fluidadmin.network.repositories.FacilityRepository;
import com.thetatechno.fluidadmin.network.repositories.SessionRepository;
import com.thetatechno.fluidadmin.network.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;
import com.thetatechno.fluidadmin.utils.Validation;

public class AddOrUpdateSessionViewModel extends ViewModel {
    private SessionRepository sessionRepository = new SessionRepository();
    private FacilityRepository facilityRepository = new FacilityRepository();
    private StaffRepository staffRepository = new StaffRepository();
    private BranchesRepository branchesRepository = new BranchesRepository();
    private String startDateMessage, startTimeValidateMessage, endTimeValidateMessage, descriptionValidateMessage,providerNameValdateMessage, siteDescriptionValidateMessage,facilityValidateMessage;

    public  MutableLiveData<APIResponse> addSession(Session session)
    {
        return sessionRepository.addSession(session);
    }

    public MutableLiveData<APIResponse> updateSession(Session session) {
        return sessionRepository.modifySession(session);
    }

    public MutableLiveData<Facilities> getFacilities(String siteId) {
        if (siteId.isEmpty())
            return facilityRepository.getAllFacilities("", PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
        else
            return facilityRepository.getAllFacilitiesForSpecificSiteID(siteId, PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }


    public MutableLiveData<StaffListModel> getStaffData() {

        return staffRepository.getAllStuff(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.PRVDR.toString());
    }

    public MutableLiveData<BranchesResponse> getAllBranches() {
        return branchesRepository.getAllBranches(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }

    private boolean isValidForDescription(String description) {
        if (!Validation.isValidForWord(description))
            return false;
        else
            return true;
    }


    private boolean isValidTime(String time){
        if(Validation.isValidTime(time))
            return true;
        else return false;
    }

    private boolean isValidTxt(String word) {
        if (Validation.isValidForWord(word))
            return true;
        else
            return false;
    }

    public String validateProviderName(String providerName) {
        if (Validation.isValidForName(providerName)) {
            providerNameValdateMessage = "";
        } else {
            providerNameValdateMessage = App.getContext().getResources().getString(R.string.choose_facility_type_error);
        }
        return providerNameValdateMessage;
    }

    public String validateFacilityType(String facilityType) {
        if (isValidTxt(facilityType)) {
            facilityValidateMessage = "";
        } else {
            facilityValidateMessage = App.getContext().getResources().getString(R.string.choose_facility_type_error);
        }
        return facilityValidateMessage;
    }

    public String validateSite(String siteDescription) {
        if (isValidTxt(siteDescription)) {
            siteDescriptionValidateMessage = "";
        } else {
            siteDescriptionValidateMessage = App.getContext().getResources().getString(R.string.choose_facility_type_error);
        }
        return siteDescriptionValidateMessage;
    }

    public String validateStartDate(String startDate){
        if (Validation.isValidDate(startDate))
            startDateMessage = "";
        else
            startDateMessage = App.getContext().getString(R.string.start_date_txt);
        return startDateMessage;
    }

    public String validateStartTime(String startTime){
        if (isValidTime(startTime))
            startTimeValidateMessage = "";
        else
            startTimeValidateMessage = App.getContext().getString(R.string.start_time_txt);
        return startTimeValidateMessage;
    }

    public String validateEndTime(String endTime, String startTime) {
        if (Validation.isValidTime(startTime)) {
            if (Validation.isValidEndAndStartTime(endTime, startTime))
                endTimeValidateMessage = "";
            else {
                endTimeValidateMessage = App.getContext().getString(R.string.end_time_greater_than_error_message);
            }
        } else
            endTimeValidateMessage = App.getContext().getString(R.string.end_time_txt);
        return endTimeValidateMessage;
    }

}

