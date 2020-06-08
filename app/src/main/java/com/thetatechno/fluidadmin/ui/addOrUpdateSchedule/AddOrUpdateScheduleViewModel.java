package com.thetatechno.fluidadmin.ui.addOrUpdateSchedule;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Error;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.shedule.Schedule;
import com.thetatechno.fluidadmin.network.repositories.ScheduleRepository;

public class AddOrUpdateScheduleViewModel extends ViewModel {
    private ScheduleRepository scheduleRepository = new ScheduleRepository();
    private MutableLiveData<String> updateBranchLiveData = new MutableLiveData<>();
    Schedule schedule;

    private  String branchDescriptionValidateMessage,branchAddressValidateMessage,emailValidateMessage,imgUrlValidateMessage,idValidateMessage , phoneNumberMessage;
    public  MutableLiveData<Error> addSchedule(Schedule schedule)
    {
        return scheduleRepository.addSchedule(schedule);
    }
    public MutableLiveData<Error> updateSchedule(Schedule schedule) {
       return scheduleRepository.modifySchedule(schedule);
    }
}
