package com.thetatechno.fluidadmin.ui.showSession;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.session_model.SessionResponse;
import com.thetatechno.fluidadmin.network.repositories.SessionRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.PreferenceController;

public class ShowSessionsViewModel extends ViewModel {
    SessionRepository sessionRepository = new SessionRepository();
    public MutableLiveData<SessionResponse> getAllSessionsRelatedToSchedule(String scheduleID){
        return sessionRepository.getSessionsRelatedToSchedule(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(),scheduleID);
    }
}

