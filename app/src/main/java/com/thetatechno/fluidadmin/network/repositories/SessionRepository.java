package com.thetatechno.fluidadmin.network.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.model.APIResponse;
import com.thetatechno.fluidadmin.model.Error;
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.model.session_model.SessionResponse;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionRepository {
    private MutableLiveData<SessionResponse> sessionResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<APIResponse> addSessionLiveData = new MutableLiveData<>();
    private MutableLiveData<APIResponse> updateSessionLiveData = new MutableLiveData<>();
    private MutableLiveData<Error> deleteSessionLiveData = new MutableLiveData<>();

    private static String TAG = CodeRepository.class.getSimpleName();

    public MutableLiveData<SessionResponse> getAllSessions(final String langId, final String scheduleId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<SessionResponse> call = myServicesInterface.getAllSession(langId, scheduleId);
        call.enqueue(new Callback<SessionResponse>() {
            @Override
            public void onResponse(Call<SessionResponse> call, Response<SessionResponse> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "get All codes response " + response.toString());
                    if (response.body() != null) {
                        sessionResponseLiveData.setValue(response.body());
                    } else {
                        sessionResponseLiveData.setValue(null);
                    }

                }
            }

            @Override
            public void onFailure(Call<SessionResponse> call, Throwable t) {
                sessionResponseLiveData.setValue(null);
                t.printStackTrace();

            }
        });
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
        Call<Error> call = myServicesInterface.deleteSession(languageId, id);
        call.enqueue(new Callback<Error>() {

            @Override
            public void onResponse(@NonNull Call<Error> call, @NonNull Response<Error> response) {
                if (response.isSuccessful()) {
                    deleteSessionLiveData.setValue(response.body());

                }
            }

            @Override
            public void onFailure(@NonNull Call<Error> call, @NonNull Throwable t) {
                deleteSessionLiveData.setValue(null);
                t.printStackTrace();

            }

        });
        return deleteSessionLiveData;
    }


}
