package com.thetatechno.fluidadmin.ui.Schedule;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.shedule_model.ScheduleResponse;
import com.thetatechno.fluidadmin.model.shedule_model.ScheduleResponseModel;
import com.thetatechno.fluidadmin.network.repositories.ScheduleRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.PreferenceController;

public class ScheduleListViewModel extends ViewModel {
    ScheduleRepository sessionRepository = new ScheduleRepository();
    public MutableLiveData<ScheduleResponseModel> getAllSchedules(){
        return sessionRepository.getAllSchedules(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }
}
