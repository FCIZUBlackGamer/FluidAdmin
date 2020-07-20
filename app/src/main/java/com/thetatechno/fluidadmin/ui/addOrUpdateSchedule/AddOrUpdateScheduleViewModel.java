package com.thetatechno.fluidadmin.ui.addOrUpdateSchedule;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.AddOrUpdateScheduleResponse;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponseModel;
import com.thetatechno.fluidadmin.model.facility_model.FacilitiesResponse;
import com.thetatechno.fluidadmin.model.shedule_model.Schedule;
import com.thetatechno.fluidadmin.model.staff_model.StaffListModel;
import com.thetatechno.fluidadmin.network.repositories.BranchesRepository;
import com.thetatechno.fluidadmin.network.repositories.FacilityRepository;
import com.thetatechno.fluidadmin.network.repositories.ScheduleRepository;
import com.thetatechno.fluidadmin.network.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;
import com.thetatechno.fluidadmin.utils.Validation;

public class AddOrUpdateScheduleViewModel extends ViewModel {
    private ScheduleRepository scheduleRepository = new ScheduleRepository();
    private MutableLiveData<String> updateBranchLiveData = new MutableLiveData<>();
    private FacilityRepository facilityRepository = new FacilityRepository();
    private StaffRepository staffRepository = new StaffRepository();
    private BranchesRepository branchesRepository = new BranchesRepository();
    private String startDateMessage, startTimeValidateMessage, endTimeValidateMessage, descriptionValidateMessage, providerNameValdateMessage, siteDescriptionValidateMessage, facilityValidateMessage;

    public MutableLiveData<AddOrUpdateScheduleResponse> addSchedule(Schedule schedule) {
        schedule.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE));
        return scheduleRepository.addSchedule(schedule);
    }

    public MutableLiveData<AddOrUpdateScheduleResponse> updateSchedule(Schedule schedule) {
        schedule.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE));
        return scheduleRepository.modifySchedule(schedule);
    }

    public MutableLiveData<FacilitiesResponse> getFacilities(String siteId) {
//        if (siteId.isEmpty())
//            return facilityRepository.getAllFacilities("", PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
//        else
            return facilityRepository.getRoomFacilitiesForSpecificSiteID(siteId, PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(),EnumCode.ClinicTypeCode.CLINIC.toString());
    }


    public MutableLiveData<StaffListModel> getStaffData() {

        return staffRepository.getAllStuff(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.PRVDR.toString());
    }

    public MutableLiveData<BranchesResponseModel> getAllBranches() {
        return branchesRepository.getAllBranches(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }

    private boolean isValidForDescription(String description) {
        if (!Validation.isValidForWord(description))
            return false;
        else
            return true;
    }

    private boolean isValidTime(String time) {
        if (Validation.isValidTime(time))
            return true;
        else return false;
    }

    private boolean isValidTxt(String word) {
        if (Validation.isValidForWord(word))
            return true;
        else
            return false;
    }

    public String validateDescription(String description) {
        if (isValidForDescription(description)) {
            descriptionValidateMessage = "";
        } else {
            descriptionValidateMessage = App.getContext().getResources().getString(R.string.description_error_message);
        }
        return descriptionValidateMessage;
    }

    public String validateProviderName(String providerName) {
        if (!providerName.trim().isEmpty()) {
            providerNameValdateMessage = "";
        } else {
            providerNameValdateMessage = App.getContext().getResources().getString(R.string.select_provider_name);
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
            siteDescriptionValidateMessage = App.getContext().getResources().getString(R.string.choose_site_message);
        }
        return siteDescriptionValidateMessage;
    }

    public String validateStartDate(String startDate) {
        if (Validation.isValidDate(startDate))
            startDateMessage = "";
        else
            startDateMessage = App.getContext().getString(R.string.start_date_txt);
        return startDateMessage;
    }

    public String validateStartTime(String startTime) {
        if (Validation.isValidTime(startTime))
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
