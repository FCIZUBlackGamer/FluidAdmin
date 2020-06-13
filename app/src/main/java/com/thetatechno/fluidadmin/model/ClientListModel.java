package com.thetatechno.fluidadmin.model;

public class ClientListModel {
    ClientData clientData;
    private int failureErrorMessageResourceId;

    public ClientListModel(ClientData clientData) {
        this.clientData = clientData;
    }

    public ClientListModel(int failureErrorMessage) {
        this.failureErrorMessageResourceId = failureErrorMessage;
    }

    public ClientData getClientData() {
        return clientData;
    }

    public int getFailureErrorMessage() {
        return failureErrorMessageResourceId;
    }
}
