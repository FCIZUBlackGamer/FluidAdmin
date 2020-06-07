package com.thetatechno.fluidadmin.ui.appointments;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.StaffData;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentListData;
import com.thetatechno.fluidadmin.network.repositories.AppointmentListRepository;
import com.thetatechno.fluidadmin.network.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppointmentsViewModel extends ViewModel {
    private StaffRepository staffRepository = new StaffRepository();
    private AppointmentListRepository appointmentListRepository = new AppointmentListRepository();

    public MutableLiveData<StaffData> getStaffData() {

        return staffRepository.getAllStuff(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.PRVDR.toString());
    }

    public String getTodayDateInFormat() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date todayDate = new Date();
        return formatter.format(todayDate);
    }

    public MutableLiveData<AppointmentListData> getAppointments(String providerId,String date){
        return appointmentListRepository.getAppointmentList(providerId,date);
    }


}
