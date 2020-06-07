package com.thetatechno.fluidadmin.ui.Schedule;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.session_model.SessionResponse;
import com.thetatechno.fluidadmin.model.shedule.ScheduleResponse;
import com.thetatechno.fluidadmin.network.repositories.ScheduleRepository;
import com.thetatechno.fluidadmin.network.repositories.SessionRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.PreferenceController;

public class ScheduleListViewModel extends ViewModel {
    ScheduleRepository sessionRepository = new ScheduleRepository();
    public MutableLiveData<ScheduleResponse> getAllSchedules(){
        return sessionRepository.getAllSchedules(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }
}
