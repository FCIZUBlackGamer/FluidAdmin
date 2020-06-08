package com.thetatechno.fluidadmin.ui.addOrUpdateSchedule;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Error;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.model.StaffData;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.facility_model.Facilities;
import com.thetatechno.fluidadmin.model.shedule.Schedule;
import com.thetatechno.fluidadmin.network.repositories.BranchesRepository;
import com.thetatechno.fluidadmin.network.repositories.FacilityRepository;
import com.thetatechno.fluidadmin.network.repositories.ScheduleRepository;
import com.thetatechno.fluidadmin.network.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

public class AddOrUpdateScheduleViewModel extends ViewModel {
    private ScheduleRepository scheduleRepository = new ScheduleRepository();
    private MutableLiveData<String> updateBranchLiveData = new MutableLiveData<>();
    private FacilityRepository facilityRepository = new FacilityRepository();
    private StaffRepository staffRepository = new StaffRepository();
    private BranchesRepository branchesRepository = new BranchesRepository();
    private String branchDescriptionValidateMessage, branchAddressValidateMessage, emailValidateMessage, imgUrlValidateMessage, idValidateMessage, phoneNumberMessage;

    public MutableLiveData<Error> addSchedule(Schedule schedule) {
        return scheduleRepository.addSchedule(schedule);
    }

    public MutableLiveData<Error> updateSchedule(Schedule schedule) {
        return scheduleRepository.modifySchedule(schedule);
    }


    public MutableLiveData<Facilities> getAllFacilities() {

        return facilityRepository.getAllFacilities("", PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }

    public MutableLiveData<StaffData> getStaffData() {

        return staffRepository.getAllStuff(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.PRVDR.toString());
    }

    public MutableLiveData<BranchesResponse> getAllBranches() {
        return branchesRepository.getAllBranches(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }
}
