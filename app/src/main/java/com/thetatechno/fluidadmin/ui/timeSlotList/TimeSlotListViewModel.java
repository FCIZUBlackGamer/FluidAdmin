package com.thetatechno.fluidadmin.ui.timeSlotList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.ConfirmAppointmentResponse;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentBooked;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentCalenderDaysListData;
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.model.time_slot_model.TimeSlotListData;
import com.thetatechno.fluidadmin.network.repositories.AppointmentCalenderRepository;
import com.thetatechno.fluidadmin.network.repositories.StaffRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;


public class TimeSlotListViewModel extends ViewModel {
    private AppointmentCalenderRepository appointmentCalenderRepository = new AppointmentCalenderRepository();
    private StaffRepository providerRepository = new StaffRepository();
    private MutableLiveData<Staff> providerMutableLiveData = new MutableLiveData<>();
    int time;
    public MutableLiveData<TimeSlotListData> getAvailableTimeSlots(String dayToBook, String sessionId, String providerId, String apptLength, String apptType) {
        return appointmentCalenderRepository.getTimeSlotForSpecificDay(dayToBook, providerId, sessionId, apptLength, apptType);
    }

    public MutableLiveData<ConfirmAppointmentResponse> bookAppointment(String sessionCode, String appointmentTime,String clientId) {
        AppointmentBooked appointmentBooked = new AppointmentBooked();
        if(appointmentTime.contains("PM"))
            time = Integer.parseInt(appointmentTime.split(":")[0]) + 12;
        else {
            time = Integer.parseInt(appointmentTime.split(":")[0]) ;

        }
        appointmentBooked.setStartTime(time + "");
        appointmentBooked.setClientId(clientId);
        appointmentBooked.setApptType("N");
        appointmentBooked.setSessionId(sessionCode);
        appointmentBooked.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
        return appointmentCalenderRepository.bookAppointment(appointmentBooked);
    }

    public MutableLiveData<AppointmentCalenderDaysListData> getScheduledCalenderDaysListForSpecificProvider(String selectedDate, String specialityCode, String providerId, String apptLength, String apptType,String siteCode) {

        return appointmentCalenderRepository.getAppointmentData(selectedDate, specialityCode, providerId, apptLength, apptType,siteCode);

    }

    public MutableLiveData<Staff> getProviderData(String specialityCode, String providerId) {

        return   providerRepository.getProviderData(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.PRVDR.toString(), specialityCode, providerId);

    }


}
