package com.thetatechno.fluidadmin.ui;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Error;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.code_model.Code;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.facility_model.FacilityCodes;
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.model.shedule_model.Schedule;
import com.thetatechno.fluidadmin.network.repositories.BranchesRepository;
import com.thetatechno.fluidadmin.network.repositories.CancelAppointmentRepository;
import com.thetatechno.fluidadmin.network.repositories.CodeRepository;
import com.thetatechno.fluidadmin.network.repositories.FacilityRepository;
import com.thetatechno.fluidadmin.network.repositories.ScheduleRepository;
import com.thetatechno.fluidadmin.network.repositories.SessionRepository;
import com.thetatechno.fluidadmin.network.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.PreferenceController;

public class MainViewModel extends ViewModel {
    final static private String TAG = MainViewModel.class.getSimpleName();
    private StaffRepository staffRepository = new StaffRepository();
    private CodeRepository codeRepository = new CodeRepository();
    private FacilityRepository facilityRepository = new FacilityRepository();
   private BranchesRepository branchesRepository = new BranchesRepository();
   private CancelAppointmentRepository cancelAppointmentRepository = new CancelAppointmentRepository();
    private ScheduleRepository scheduleRepository = new ScheduleRepository();
    private SessionRepository sessionRepository = new SessionRepository();
    private MutableLiveData<String> deletedStaffMessageLiveData = new MutableLiveData<>();
    private MutableLiveData<String> deletedCodeMessageLiveData = new MutableLiveData<>();
    private  MutableLiveData<String> deletedFacilityMessageLiveData = new MutableLiveData<>();
    private MutableLiveData<Error> deletedSessionLiveData = new MutableLiveData<>();
    private MutableLiveData<Error> deletedScheduledLiveData = new MutableLiveData<>();
    private String messageForStaff = "";
    private String messageForCode = "";
    private  String messageForSFacility = "";

    public MutableLiveData<String> deleteAgentOrProvider(final Staff staff) {
        staffRepository.deleteStaff(staff.getStaffId(), new OnDataChangedCallBackListener<Integer>() {
            @Override
            public void onResponse(Integer b) {
                Log.i(TAG, "deleteAgentOrProvider: delete state " + b);
                if (b == Constants.DELETE_SUCCESS_STATE) {
                    messageForStaff = "Delete " + staff.getFirstName() + " successfully";
                }
                else if (b == -2292){
                    messageForStaff = "Cannot delete, Provider has a schedule.";

                }else if (b == Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE) {
                    messageForStaff = "Failed to delete.";

                }
                deletedStaffMessageLiveData.setValue(messageForStaff);

            }
        });
        return deletedStaffMessageLiveData;
    }

    public MutableLiveData<String> deleteCode(final Code code) {
        codeRepository.deleteCode(code.getCodeType(), code.getCode(), new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                Log.i(TAG, "deleteCode: delete state " + b);

                if (b.equals(Constants.DELETE_SUCCESS_STATE)) {
                    messageForCode = "Delete code " + code.getCode() + " successfully ";
//                    codeRepository.getAllCodes(EnumCode.Code.STFFGRP.toString(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
                    deletedCodeMessageLiveData.setValue(messageForCode);

                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    messageForCode = "Failed to delete code " + code.getCode();
                    deletedCodeMessageLiveData.setValue(messageForCode);
                }


            }
        });
        return deletedCodeMessageLiveData;
    }

    public MutableLiveData<String> deleteFacility(final Facility facility) {
        facilityRepository.deleteFacility(facility.getId(), new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                Log.i(TAG, "deleteFacility: delete state " + b);

                if (b.equals(Constants.DELETE_SUCCESS_STATE)) {
                    messageForSFacility = "Delete facility " + facility.getId()+ " successfully";
                    deletedFacilityMessageLiveData.setValue(messageForSFacility);

                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    messageForSFacility = "Failed to delete facility " + facility.getId();
                    deletedFacilityMessageLiveData.setValue(messageForSFacility);
                }
                else if(b.equals("-2292")) {
                    messageForSFacility = "cann't delete facility";
                    deletedFacilityMessageLiveData.setValue(messageForSFacility);
                }

            }
        });
        return deletedFacilityMessageLiveData;
    }

    public MutableLiveData<String> deleteBranch(final Branch branch) {
        branchesRepository.deleteBranch(branch.getSiteId(), new OnDataChangedCallBackListener<Integer>() {
            @Override
            public void onResponse(Integer b) {
                Log.i(TAG, "deleteBranch: delete state " + b);

                if (b.equals(Constants.DELETE_SUCCESS_STATE)) {
                    messageForSFacility = "Delete branch " + branch.getDescription()+ " successfully";
                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    messageForSFacility = "Failed to delete branch " + branch.getDescription();
                }
                else if(b.equals("-2292")) {
                    messageForSFacility = "cann't delete branch";
                }
                deletedFacilityMessageLiveData.setValue(messageForSFacility);


            }
        });
        return deletedFacilityMessageLiveData;
    }



    public MutableLiveData<Error> deleteSchedule(final Schedule schedule) {
       return scheduleRepository.deleteSchedule(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(),schedule.getId());
    }

    public MutableLiveData<Error> deleteSession(final Session session) {
        return sessionRepository.deleteSession(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(),session.getId());
    }

    public void linkToFacility(String staffId, FacilityCodes facilityCodes, final OnDataChangedCallBackListener<String> onDataChangedCallBackListener) {
        facilityRepository.linkToFacility(staffId, facilityCodes, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                onDataChangedCallBackListener.onResponse(b);
            }
        });
    }
    MutableLiveData<Status> cancelAppointment(String appointmentId) {
        return cancelAppointmentRepository.cancelAppointment(appointmentId);
    }

}
