package com.thetatechno.fluidadmin.network.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;


import com.thetatechno.fluidadmin.model.appointment_model.AppointmentBooked;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentCalenderDaysListData;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.model.time_slot_model.TimeSlotListData;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentCalenderRepository {

    private MutableLiveData<AppointmentCalenderDaysListData> mutableLiveData = new MutableLiveData<>();
    private MutableLiveData<TimeSlotListData> timeSlotListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Status> statusMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<AppointmentCalenderDaysListData> getAppointmentData(String dateOfSpecificDay, String specialtyCode, String providerId, String apptLength, String apptType) {
        EspressoTestingIdlingResource.increment();
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<AppointmentCalenderDaysListData> call = myServicesInterface.getAppointmentCalender(dateOfSpecificDay, specialtyCode, providerId, apptLength, apptType, PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
        call.enqueue(new Callback<AppointmentCalenderDaysListData>() {
            @Override
            public void onResponse(Call<AppointmentCalenderDaysListData> call, Response<AppointmentCalenderDaysListData> response) {
                EspressoTestingIdlingResource.increment();
                if (response.isSuccessful()) {
                    AppointmentCalenderDaysListData appointmentCalenderDaysListData = response.body();
                    mutableLiveData.setValue(appointmentCalenderDaysListData);
                } else {

                    mutableLiveData.setValue(null);
                }
                EspressoTestingIdlingResource.decrement();

            }

            @Override
            public void onFailure(Call<AppointmentCalenderDaysListData> call, Throwable t) {
                t.printStackTrace();
                mutableLiveData.setValue(null);
            }
        });
        EspressoTestingIdlingResource.decrement();
        return mutableLiveData;
    }

    public MutableLiveData<AppointmentCalenderDaysListData> getAppointmentData(String dateOfSpecificDay, String specialtyCode) {
        EspressoTestingIdlingResource.increment();
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<AppointmentCalenderDaysListData> call = myServicesInterface.getAppointmentCalender(dateOfSpecificDay, specialtyCode);
        call.enqueue(new Callback<AppointmentCalenderDaysListData>() {
            @Override
            public void onResponse(Call<AppointmentCalenderDaysListData> call, Response<AppointmentCalenderDaysListData> response) {
                EspressoTestingIdlingResource.increment();
                if (response.isSuccessful()) {
                    AppointmentCalenderDaysListData appointmentCalenderDaysListData = response.body();
                    mutableLiveData.setValue(appointmentCalenderDaysListData);
                } else {

                    mutableLiveData.setValue(null);
                }
                EspressoTestingIdlingResource.decrement();

            }

            @Override
            public void onFailure(Call<AppointmentCalenderDaysListData> call, Throwable t) {
                t.printStackTrace();
                mutableLiveData.setValue(null);
            }
        });
        EspressoTestingIdlingResource.decrement();
        return mutableLiveData;
    }

    public MutableLiveData<TimeSlotListData> getTimeSlotForSpecificDay(String dayToBook, String providerId, String sessionId, String apptLength, String apptType) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<TimeSlotListData> call = myServicesInterface.getAvailableSlots(dayToBook,sessionId, providerId, apptLength, apptType, PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
        call.enqueue(new Callback<TimeSlotListData>() {
            @Override
            public void onResponse(Call<TimeSlotListData> call, Response<TimeSlotListData> response) {
                if (response.isSuccessful()) {
                    timeSlotListMutableLiveData.setValue(response.body());
                } else {

                    timeSlotListMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<TimeSlotListData> call, Throwable t) {
                t.printStackTrace();
                timeSlotListMutableLiveData.setValue(null);
            }
        });
        return timeSlotListMutableLiveData;
    }

    public MutableLiveData<Status> bookAppointment(AppointmentBooked appointmentBooked) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Status> call = myServicesInterface.bookAppointment(appointmentBooked);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    statusMutableLiveData.setValue(response.body());
                    if (response.body() != null)
                        Log.i("Response", response.body().getStatus());
                } else {
                    statusMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                t.printStackTrace();
                statusMutableLiveData.setValue(null);
            }
        });
        return statusMutableLiveData;
    }


}
