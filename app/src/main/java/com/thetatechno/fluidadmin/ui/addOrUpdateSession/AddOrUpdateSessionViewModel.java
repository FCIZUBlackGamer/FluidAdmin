package com.thetatechno.fluidadmin.ui.addOrUpdateSession;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.model.Error;
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.network.repositories.SessionRepository;

public class AddOrUpdateSessionViewModel extends ViewModel {
    private SessionRepository sessionRepository = new SessionRepository();
    private MutableLiveData<String> updateBranchLiveData = new MutableLiveData<>();
    Session session;

    private  String branchDescriptionValidateMessage,branchAddressValidateMessage,emailValidateMessage,imgUrlValidateMessage,idValidateMessage , phoneNumberMessage;
    public  MutableLiveData<Error> addSession(Session session)
    {
        return sessionRepository.addSession(session);
    }

    public MutableLiveData<Error> updateSession(Session session) {
        return sessionRepository.modifySession(session);
    }
}
