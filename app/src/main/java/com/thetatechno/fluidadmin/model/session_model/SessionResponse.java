package com.thetatechno.fluidadmin.model.session_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.thetatechno.fluidadmin.model.Error;

import java.util.List;

public class SessionResponse {
    @SerializedName("items")
    @Expose
    private List<Session> sessions = null;
    @SerializedName("error")
    @Expose
    private Error error;

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
