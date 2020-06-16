package com.thetatechno.fluidadmin.network.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.model.AddOrUpdateScheduleResponse;
import com.thetatechno.fluidadmin.model.Error;

import com.thetatechno.fluidadmin.model.shedule.Schedule;
import com.thetatechno.fluidadmin.model.shedule.ScheduleResponse;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleRepository {

    private MutableLiveData<ScheduleResponse> scheduleResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<AddOrUpdateScheduleResponse> addScheduleLiveData = new MutableLiveData<>();
    private MutableLiveData<AddOrUpdateScheduleResponse> updateScheduleLiveData = new MutableLiveData<>();
    private MutableLiveData<Error> deleteScheduleLiveData = new MutableLiveData<>();

    private static String TAG = CodeRepository.class.getSimpleName();

    public MutableLiveData<ScheduleResponse> getAllSchedules(final String langId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<ScheduleResponse> call = myServicesInterface.getAllSchedule(langId);
        call.enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "get All codes response " + response.toString());
                    if (response.body() != null) {
                        scheduleResponseLiveData.setValue(response.body());
                    } else {
                        scheduleResponseLiveData.setValue(null);
                    }

                }
            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                scheduleResponseLiveData.setValue(null);
                t.printStackTrace();

            }
        });
        return scheduleResponseLiveData;
    }

    public MutableLiveData<AddOrUpdateScheduleResponse> addSchedule(final Schedule schedule) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<AddOrUpdateScheduleResponse> call = myServicesInterface.addSchedule(schedule);
        call.enqueue(new Callback<AddOrUpdateScheduleResponse>() {

            @Override
            public void onResponse(Call<AddOrUpdateScheduleResponse> call, Response<AddOrUpdateScheduleResponse> response) {
                Log.i(TAG, "insertCode: response " + response.toString());
                if (response.isSuccessful()) {
                    addScheduleLiveData.setValue(response.body());
                } else
                    addScheduleLiveData.setValue(null);
            }

            @Override
            public void onFailure(Call<AddOrUpdateScheduleResponse> call, Throwable t) {
                addScheduleLiveData.setValue(null);
                t.printStackTrace();
            }

        });
        return addScheduleLiveData;
    }

    public MutableLiveData<AddOrUpdateScheduleResponse> modifySchedule(final Schedule schedule) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<AddOrUpdateScheduleResponse> call = myServicesInterface.updateSchedule(schedule);
        call.enqueue(new Callback<AddOrUpdateScheduleResponse>() {

            @Override
            public void onResponse(Call<AddOrUpdateScheduleResponse> call, Response<AddOrUpdateScheduleResponse> response) {
                Log.i(TAG, "insertCode: response " + response.toString());
                if (response.isSuccessful()) {
                    updateScheduleLiveData.setValue(response.body());
                } else
                    updateScheduleLiveData.setValue(null);
            }

            @Override
            public void onFailure(Call<AddOrUpdateScheduleResponse> call, Throwable t) {
                updateScheduleLiveData.setValue(null);
                t.printStackTrace();
            }

        });
        return updateScheduleLiveData;
    }

    public MutableLiveData<Error> deleteSchedule(String languageId, String id) {

        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<Error> call = myServicesInterface.deleteSchedule(languageId, id);
        call.enqueue(new Callback<Error>() {

            @Override
            public void onResponse(@NonNull Call<Error> call, @NonNull Response<Error> response) {
                if (response.isSuccessful()) {
                    deleteScheduleLiveData.setValue(response.body());

                }
            }

            @Override
            public void onFailure(@NonNull Call<Error> call, @NonNull Throwable t) {
                deleteScheduleLiveData.setValue(null);
                t.printStackTrace();

            }

        });
        return deleteScheduleLiveData;
    }

}
