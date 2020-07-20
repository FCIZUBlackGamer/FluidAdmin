package com.thetatechno.fluidadmin.network.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.beloo.widget.chipslayoutmanager.layouter.AbstractPositionIterator;
import com.thetatechno.fluidadmin.model.APIResponse;
import com.thetatechno.fluidadmin.model.Error;
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.model.session_model.SessionResponse;
import com.thetatechno.fluidadmin.model.session_model.SessionResponseModel;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionRepository {
    private MutableLiveData<SessionResponseModel> sessionResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<APIResponse> addSessionLiveData = new MutableLiveData<>();
    private MutableLiveData<APIResponse> updateSessionLiveData = new MutableLiveData<>();
    private MutableLiveData<Error> deleteSessionLiveData = new MutableLiveData<>();
    private String sessionErrorMessage = "Failed to load sessions.";
    private static String TAG = CodeRepository.class.getSimpleName();

    public MutableLiveData<SessionResponseModel> getSessionsRelatedToSchedule(final String langId, final String scheduleId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<SessionResponse> call = myServicesInterface.getSessionsRelatedToSchedule(langId, scheduleId);
        call.enqueue(callback);
        return sessionResponseLiveData;
    }

    Callback<SessionResponse> callback = new Callback<SessionResponse>() {
        @Override
        public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
            if (response.code() == Constants.STATE_OK && response.body() != null) {
                Log.i(TAG, "get sessions response " + response.toString());
                sessionResponseLiveData.setValue(new SessionResponseModel(response.body()));
            } else {
                sessionResponseLiveData.setValue(new SessionResponseModel(sessionErrorMessage));
            }
        }
        @Override
        public void onFailure(Call<SessionResponse> call, Throwable t) {
            sessionResponseLiveData.setValue(new SessionResponseModel("Error, try again."));
            t.printStackTrace();

        }
    };

    public MutableLiveData<SessionResponseModel> getAllSessions(final String langId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<SessionResponse> call = myServicesInterface.getAllSessions(langId);
        call.enqueue(callback);
        return sessionResponseLiveData;
    }

    public MutableLiveData<APIResponse> addSession(final Session session) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<APIResponse> call = myServicesInterface.addSession(session);
        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                Log.i(TAG, "insertCode: response " + response.toString());
                if (response.isSuccessful()) {
                    addSessionLiveData.setValue(response.body());
                } else
                    addSessionLiveData.setValue(null);
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                addSessionLiveData.setValue(null);
                t.printStackTrace();
            }

        });
        return addSessionLiveData;
    }

    public MutableLiveData<APIResponse> modifySession(final Session session) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<APIResponse> call = myServicesInterface.modifySession(session);
        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                Log.i(TAG, "insertCode: response " + response.toString());
                if (response.isSuccessful()) {
                    updateSessionLiveData.setValue(response.body());
                } else
                    updateSessionLiveData.setValue(null);
            }

            @Override
            public void onFailure(Call<APIResponse> call, Throwable t) {
                updateSessionLiveData.setValue(null);
                t.printStackTrace();
            }

        });
        return updateSessionLiveData;
    }

    public MutableLiveData<Error> deleteSession(String languageId, String id) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<APIResponse> call = myServicesInterface.deleteSession(languageId, id);
        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(@NonNull Call<APIResponse> call, @NonNull Response<APIResponse> response) {
                if (response.isSuccessful()) {
                    deleteSessionLiveData.setValue(response.body().getError());

                } else {
                    deleteSessionLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<APIResponse> call, @NonNull Throwable t) {
                deleteSessionLiveData.setValue(null);
                t.printStackTrace();

            }

        });
        return deleteSessionLiveData;
    }


}
