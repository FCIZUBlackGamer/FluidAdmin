package com.thetatechno.fluidadmin.ui.Session;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.session_model.SessionResponse;
import com.thetatechno.fluidadmin.model.session_model.SessionResponseModel;
import com.thetatechno.fluidadmin.network.repositories.SessionRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.PreferenceController;

public class SessionListViewModel extends ViewModel {
    SessionRepository sessionRepository = new SessionRepository();
public MutableLiveData<SessionResponseModel> getAllSessions(){
    return sessionRepository.getAllSessions(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
}
}
