package com.thetatechno.fluidadmin.model.appointment_model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentDayDetails implements Parcelable {
    @SerializedName("date")
    @Expose
    String date;
    @SerializedName("availableSlots")
    @Expose

    int availableSlots;
    @SerializedName("sessionCode")
    @Expose
    String sessionId;

    @SerializedName("providerId")
    @Expose

    String providerId;

    protected AppointmentDayDetails(Parcel in) {
        date = in.readString();
        availableSlots = in.readInt();
        sessionId = in.readString();
        providerId = in.readString();
    }

    public static final Creator<AppointmentDayDetails> CREATOR = new Creator<AppointmentDayDetails>() {
        @Override
        public AppointmentDayDetails createFromParcel(Parcel in) {
            return new AppointmentDayDetails(in);
        }

        @Override
        public AppointmentDayDetails[] newArray(int size) {
            return new AppointmentDayDetails[size];
        }
    };

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(int availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeInt(availableSlots);
        dest.writeString(sessionId);
        dest.writeString(providerId);
    }
}
