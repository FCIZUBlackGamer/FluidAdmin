package com.thetatechno.fluidadmin.ui.timeSlotList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.appointment_model.AppointmentBooked;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentCalenderDaysListData;
import com.thetatechno.fluidadmin.model.Staff;
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

    public MutableLiveData<TimeSlotListData> getAvailableTimeSlots(String dayToBook, String sessionCode, String providerId) {
        return appointmentCalenderRepository.getTimeSlotForSpecificDay(dayToBook, providerId, sessionCode, Constants.APPOINTMENT_LENGTH);
    }

    public MutableLiveData<Status> bookAppointment(String appointmentId,String clientId) {
        AppointmentBooked appointmentBooked = new AppointmentBooked();
        appointmentBooked.setAppointmentId(appointmentId);
        appointmentBooked.setClientId(clientId);
        appointmentBooked.setAppointmentType("BN");
        return appointmentCalenderRepository.bookAppointment(appointmentBooked);
    }

    public MutableLiveData<AppointmentCalenderDaysListData> getScheduledCalenderDaysListForSpecificProvider(String selectedDate, String specialityCode, String providerId) {

        return appointmentCalenderRepository.getAppointmentData(selectedDate, specialityCode, providerId);

    }

    public MutableLiveData<Staff> getProviderData(String specialityCode, String providerId) {

        providerRepository.getProviderData(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.PRVDR.toString(), specialityCode, providerId);

        return providerMutableLiveData;
    }


}
