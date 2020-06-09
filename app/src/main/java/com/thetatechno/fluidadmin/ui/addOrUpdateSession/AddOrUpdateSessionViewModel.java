package com.thetatechno.fluidadmin.ui.addOrUpdateSession;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.Error;
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.network.repositories.SessionRepository;

public class AddOrUpdateSessionViewModel extends ViewModel {
    private SessionRepository sessionRepository = new SessionRepository();
    public  MutableLiveData<Error> addSession(Session session)
    {
        return sessionRepository.addSession(session);
    }

    public MutableLiveData<Error> updateSession(Session session) {
        return sessionRepository.modifySession(session);
    }
}
