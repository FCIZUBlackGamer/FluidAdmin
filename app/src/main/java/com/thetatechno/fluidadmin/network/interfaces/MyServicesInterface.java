package com.thetatechno.fluidadmin.network.interfaces;

import com.thetatechno.fluidadmin.model.AddNewOrModifyClientResponse;
import com.thetatechno.fluidadmin.model.AddOrUpdateScheduleResponse;
import com.thetatechno.fluidadmin.model.APIResponse;
import com.thetatechno.fluidadmin.model.AddOrUpdateStatus;
import com.thetatechno.fluidadmin.model.AddOrUpdateStatusResponse;
import com.thetatechno.fluidadmin.model.ClientModelForRegister;
import com.thetatechno.fluidadmin.model.ConfirmAppointmentResponse;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentBooked;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentCalenderDaysListData;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentListData;
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.model.session_model.SessionResponse;
import com.thetatechno.fluidadmin.model.shedule_model.Schedule;
import com.thetatechno.fluidadmin.model.shedule_model.ScheduleResponse;
import com.thetatechno.fluidadmin.model.time_slot_model.TimeSlotListData;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.code_model.Code;
import com.thetatechno.fluidadmin.model.code_model.CodeList;
import com.thetatechno.fluidadmin.model.ClientData;
import com.thetatechno.fluidadmin.model.device_model.DeviceListData;
import com.thetatechno.fluidadmin.model.facility_model.FacilitiesResponse;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.facility_model.FacilityCodes;
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.model.staff_model.StaffData;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface MyServicesInterface {


    @GET("/ords/fluid/api/facility")
    Call<FacilitiesResponse> getAllWaitingListFacilities(@Query(Constants.FACILITY_ID) String facilityId, @Query(Constants.LANG_ID) String langId, @Query(Constants.FACILITY_TYPE_CODE) String typeCode);

    @GET("/ords/fluid/api/facility")
    Call<FacilitiesResponse> getAllFacilities(@Query(Constants.FACILITY_ID) String facilityId, @Query(Constants.LANG_ID) String langId);

    @GET("/ords/fluid/api/facility")
    Call<FacilitiesResponse> getFacilityForSpecificSiteId(@Query("siteId") String siteId, @Query(Constants.LANG_ID) String langId, @Query(Constants.FACILITY_TYPE_CODE) String typeCode);

    @POST("/ords/fluid/api/facility")
    Call<AddOrUpdateStatus> addFacility(@Body Facility facility);

    @DELETE("/ords/fluid/api/facility")
    Call<Status> deleteFacility(@Query(Constants.ID) String facilityId);

    @PUT("/ords/fluid/api/facility")
    Call<AddOrUpdateStatus> updateFacility(@Body Facility facility);

    @GET("/ords/fluid/api/code")
    Call<CodeList> getCodes(@Query("codeType") String codeType, @Query(Constants.LANG_ID) String langId);

    @POST("/ords/fluid/api/code")
    Call<AddOrUpdateStatus> addCode(@Body Code code);

    @DELETE("/ords/fluid/api/code")
    Call<Status> deleteCode(@Query("code") String code, @Query("codeType") String codeType);

    @PUT("/ords/fluid/api/code")
    Call<AddOrUpdateStatus> updateCode(@Body Code code);

    @GET("/ords/fluid/api/staff")
    Call<StaffData> getAllStuff(@Query("langId") String langId, @Query("typeCode") String typeCode);

    @GET("/ords/fluid/api/staff")
    Call<StaffData> getProviderData(@Query("langId") String langId, @Query("typeCode") String typeCode, @Query("specialityCode") String specialityCode, @Query("staffId") String providerId);

    @GET("/ords/fluid/api/staff")
    Call<StaffData> getAllProvidersInSpeciality(@Query("langId") String langId, @Query("typeCode") String typeCode, @Query("specialityCode") String specialityCode);

    @POST("/ords/fluid/api/staff")
    Call<AddOrUpdateStatus> insertNewStuff(@Body Staff staff);

    @PUT("/ords/fluid/api/staff")
    Call<AddOrUpdateStatus> updateStaff(@Body Staff staff);

    @DELETE("/ords/fluid/api/staff")
    Call<AddOrUpdateStatus> deleteStuff(@Query("staffId") String staffId);

    @GET("/ords/fluid/clnt/getClient")
    Call<ClientData> getAllClients(@Query("langId") String langId);

    @GET("/ords/fluid/api/client")
    Call<ClientData> getSpecificClientWithId(@Query("clientId") String clientId, @Query("langId") String langId);

    @PUT("/ords/fluid/api/addAgentFacilities")
    Call<Status> addToFacilities(@Query("staffId") String staffId, @Body FacilityCodes facilityCodes);

    @GET("/ords/fluid/api/device")
    Call<DeviceListData> getAllDevicesList();

    @GET("/ords/fluid/api/device")
    Call<DeviceListData> getSpecificDeviceData(@Query("deviceId") String deviceId);

    /*  branches API  */
    @GET("/ords/fluid/api/site")
    Call<BranchesResponse> getBranches(@Query("langId") String language);

    @POST("/ords/fluid/api/site")
    Call<AddOrUpdateStatusResponse> addBranch(@Body Branch newBranch);

    @PUT("/ords/fluid/api/site")
    Call<AddOrUpdateStatusResponse> updateBranch(@Body Branch newBranch);

    @DELETE("/ords/fluid/api/site")
    Call<AddOrUpdateStatus> deleteBranch(@Query("siteId") String siteId);


    @GET("/ords/fluid/appt/getApptCalendar")
    Call<AppointmentCalenderDaysListData> getAppointmentCalender(@Query("monthDate") String dateOfSpecificDay, @Query("specialtyCode") String specialtyCode, @Query("providerId") String providerId, @Query("apptLength") String apptLength, @Query("apptType") String apptType, @Query(Constants.LANG_ID) String langId, @Query("siteId") String siteId);

    @GET("/ords/fluid/appt/getApptCalendar")
    Call<AppointmentCalenderDaysListData> getAppointmentCalender(@Query("monthDate") String dateOfSpecificDay, @Query("specialtyCode") String specialtyCode);

    @GET("/ords/fluid/appt/getAvailableSlots")
    Call<TimeSlotListData> getAvailableSlots(@Query("bookDay") String bookDay, @Query("sessionId") String sessionId, @Query("providerId") String providerId, @Query("apptLength") String apptLength, @Query("apptType") String apptType, @Query(Constants.LANG_ID) String langId);

    @POST("/ords/fluid/appt/bookAppointment")
    Call<ConfirmAppointmentResponse> bookAppointment(@Body AppointmentBooked appointmentBooked);

    @POST("/ords/fluid/appt/cancelAppointment")
    Call<Status> cancelAppointment(@Query("apptId") String appointmentId, @Query(Constants.LANG_ID) String langId);

    @GET("/ords/fluid/api/appointment")
    Call<AppointmentListData> getAppointments(@Query("providerId") String providerId, @Query("apptDate") String date, @Query("clientID") String clientID);

    @GET("/ords/fluid/appt/getProviderAppts")
    Call<AppointmentListData> getAppointments(@Query("providerId") String providerId, @Query("apptDate") String date);

    // apis for register new customer
    @GET("/ords/fluid/api/code")
    Call<CodeList> getIDTypesCode(@Query("codeType") String idTypeCode, @Query(Constants.LANG_ID) String langId);

    @POST("/ords/fluid/api/patient")
    Call<Status> addNewPatient(@Body ClientModelForRegister clientModelForRegister);

    @POST("/ords/fluid/clnt/addClient")
    Call<AddNewOrModifyClientResponse> addNewClient(@Body ClientModelForRegister customer);

    @GET("/ords/fluid/schedule/getSchedule")
    Call<ScheduleResponse> getAllSchedule(@Query("langId") String langId);

    @POST("/ords/fluid/schedule/addSchedule")
    Call<AddOrUpdateScheduleResponse> addSchedule(@Body Schedule schedule);

    @PUT("/ords/fluid/schedule/modifySchedule")
    Call<AddOrUpdateScheduleResponse> updateSchedule(@Body Schedule schedule);

    @DELETE("/ords/fluid/schedule/deleteSchedule")
    Call<APIResponse> deleteSchedule(@Query("langId") String langId, @Query("id") String id);

    @GET("/ords/fluid/session/getSession")
    Call<SessionResponse> getSessionsRelatedToSchedule(@Query("langId") String langId, @Query("scheduleId") String scheduleId);

    @GET("/ords/fluid/session/getSession")
    Call<SessionResponse> getAllSessions(@Query("langId") String langId);

    @POST("/ords/fluid/session/addSession")
    Call<APIResponse> addSession(@Body Session session);

    @PUT("/ords/fluid/session/modifySession")
    Call<APIResponse> modifySession(@Body Session session);

    @DELETE("/ords/fluid/session/deleteSession")
    Call<APIResponse> deleteSession(@Query("langId") String langId, @Query("id") String id);
}
