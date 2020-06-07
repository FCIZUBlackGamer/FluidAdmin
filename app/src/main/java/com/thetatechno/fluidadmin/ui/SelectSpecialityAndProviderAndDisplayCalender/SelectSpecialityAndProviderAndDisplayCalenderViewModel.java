package com.thetatechno.fluidadmin.ui.SelectSpecialityAndProviderAndDisplayCalender;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.thetatechno.fluidadmin.model.appointment_model.AppointmentCalenderDaysListData;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.code_model.Code;
import com.thetatechno.fluidadmin.network.repositories.AppointmentCalenderRepository;
import com.thetatechno.fluidadmin.network.repositories.BranchesRepository;
import com.thetatechno.fluidadmin.network.repositories.ClientRepository;
import com.thetatechno.fluidadmin.network.repositories.CodeRepository;
import com.thetatechno.fluidadmin.network.repositories.StaffRepository;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelectSpecialityAndProviderAndDisplayCalenderViewModel extends ViewModel {
    private AppointmentCalenderRepository appointmentCalenderRepository = new AppointmentCalenderRepository();
    private BranchesRepository branchesRepository = new BranchesRepository();
    CodeRepository codeRepository = new CodeRepository();
    StaffRepository providerRepository = new StaffRepository();
    private List<Code> specialityCodeList = new ArrayList<>();
    ClientRepository clientRepository = new ClientRepository();

    public MutableLiveData getSpecialities() {
        String codeType = EnumCode.Code.STFFGRP.toString();
        String langId = PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase();
        return codeRepository.getAllCodes(codeType, langId);
    }

    public MutableLiveData<BranchesResponse> getSites() {
        String langId = PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase();
        return branchesRepository.getAllBranches(langId);
    }

    public MutableLiveData getAllProviders(String specialityCode, String providerId) {

       return providerRepository.getAllProvidersInSpeciality(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.PRVDR.toString(),specialityCode,providerId);

    }

    public MutableLiveData<String> getSpecialityCodeFromSelectedDescription(String codeDescription) {
        MutableLiveData<String> codeMutableLiveData = new MutableLiveData<>();
        if (!codeDescription.isEmpty()) {
            for (Code code : specialityCodeList) {
                if (code.getDescription().equals(codeDescription))
                    codeMutableLiveData.setValue(code.getCode());
            }
        } else
            codeMutableLiveData.setValue("");
        return codeMutableLiveData;
    }


    public MutableLiveData<AppointmentCalenderDaysListData> getScheduledCalenderDaysList(String date, String specialityCode, String providerId) {
        EspressoTestingIdlingResource.increment();
        if (date.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            Date todayDate = new Date();
            EspressoTestingIdlingResource.decrement();
            return appointmentCalenderRepository.getAppointmentData(formatter.format(todayDate), specialityCode, providerId);
        } else
            return appointmentCalenderRepository.getAppointmentData(date, specialityCode, providerId);
    }

    public MutableLiveData<AppointmentCalenderDaysListData> getScheduledCalenderDaysList(String specialityCode) {
        EspressoTestingIdlingResource.increment();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        EspressoTestingIdlingResource.decrement();
        return appointmentCalenderRepository.getAppointmentData(formatter.format(date), specialityCode);
    }

    public MutableLiveData getAllClients() {
        String langId = PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase();

        return clientRepository.getAllClients(langId);
    }
}
