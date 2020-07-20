package com.thetatechno.fluidadmin.network.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.thetatechno.fluidadmin.model.APIResponse;
import com.thetatechno.fluidadmin.model.AddOrUpdateScheduleResponse;
import com.thetatechno.fluidadmin.model.Error;

import com.thetatechno.fluidadmin.model.shedule_model.Schedule;
import com.thetatechno.fluidadmin.model.shedule_model.ScheduleResponse;
import com.thetatechno.fluidadmin.model.shedule_model.ScheduleResponseModel;
import com.thetatechno.fluidadmin.network.interfaces.MyServicesInterface;
import com.thetatechno.fluidadmin.network.interfaces.RetrofitInstance;
import com.thetatechno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleRepository {

    private MutableLiveData<ScheduleResponseModel> scheduleResponseLiveData = new MutableLiveData<>();
    private MutableLiveData<AddOrUpdateScheduleResponse> addScheduleLiveData = new MutableLiveData<>();
    private MutableLiveData<AddOrUpdateScheduleResponse> updateScheduleLiveData = new MutableLiveData<>();
    private MutableLiveData<Error> deleteScheduleLiveData = new MutableLiveData<>();

    private static String TAG = ScheduleRepository.class.getSimpleName();

    public MutableLiveData<ScheduleResponseModel> getAllSchedules(final String langId) {
        MyServicesInterface myServicesInterface = RetrofitInstance.getService();
        Call<ScheduleResponse> call = myServicesInterface.getAllSchedule(langId);
        call.enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                if (response.code() == Constants.STATE_OK && response.body() != null) {
                    Log.i(TAG, "get All codes response " + response.toString());
                        scheduleResponseLiveData.setValue(new ScheduleResponseModel(response.body()));
                    } else {
                    scheduleResponseLiveData.setValue(new ScheduleResponseModel("Failed to load schedules."));

                }


            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                scheduleResponseLiveData.setValue(new ScheduleResponseModel("Error,try again."));
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
                    Error error = response.body().getError();
                    error.setErrorMessage("Schedule Updated");
                    response.body().setError(error);
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
        Call<APIResponse> call = myServicesInterface.deleteSchedule(languageId, id);
        call.enqueue(new Callback<APIResponse>() {

            @Override
            public void onResponse(@NonNull Call<APIResponse> call, @NonNull Response<APIResponse> response) {
                if (response.isSuccessful()) {
                    deleteScheduleLiveData.setValue(response.body().getError());

                }else {
                    deleteScheduleLiveData.setValue(null);
                }
            }
            @Override
            public void onFailure(@NonNull Call<APIResponse> call, @NonNull Throwable t) {
                deleteScheduleLiveData.setValue(null);
                t.printStackTrace();
            }
        });
        return deleteScheduleLiveData;
    }

}
