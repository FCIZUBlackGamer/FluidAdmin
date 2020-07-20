package com.thetatechno.fluidadmin.model.session_model;

public class SessionResponseModel {
    String errorMessage;
    SessionResponse sessionResponse;

    public SessionResponseModel(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public SessionResponseModel(SessionResponse sessionResponse) {
        this.sessionResponse = sessionResponse;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public SessionResponse getSessionResponse() {
        return sessionResponse;
    }
}
