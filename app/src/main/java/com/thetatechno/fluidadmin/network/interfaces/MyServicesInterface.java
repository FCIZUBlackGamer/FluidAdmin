package com.thetatechno.fluidadmin.network.interfaces;

import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.code_model.Code;
import com.thetatechno.fluidadmin.model.code_model.CodeList;
import com.thetatechno.fluidadmin.model.CustomerList;
import com.thetatechno.fluidadmin.model.device_model.DeviceListData;
import com.thetatechno.fluidadmin.model.Facilities;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.model.FacilityCodes;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.model.StaffData;
import com.thetatechno.fluidadmin.model.State;
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
    Call<Facilities> getAllWaitingListFacilities(@Query(Constants.FACILITY_ID) String facilityId, @Query(Constants.LANG_ID) String langId, @Query(Constants.FACILITY_TYPE_CODE) String typeCode);

    @GET("/ords/fluid/api/facility")
    Call<Facilities> getAllFacilities(@Query(Constants.FACILITY_ID) String facilityId, @Query(Constants.LANG_ID) String langId);

    @POST("/ords/fluid/api/facility")
    Call<State> addFacility(@Body Facility facility);

    @DELETE("/ords/fluid/api/facility")
    Call<State> deleteFacility(@Query(Constants.ID) String facilityId);

    @PUT("/ords/fluid/api/facility")
    Call<State> updateFacility(@Body Facility facility);

    @GET("/ords/fluid/api/code")
    Call<CodeList> getCodes(@Query("codeType") String codeType, @Query(Constants.LANG_ID) String langId);

    @POST("/ords/fluid/api/code")
    Call<State> addCode(@Body Code code);

    @DELETE("/ords/fluid/api/code")
    Call<State> deleteCode(@Query("code") String code, @Query("codeType") String codeType);

    @PUT("/ords/fluid/api/code")
    Call<State> updateCode(@Body Code code);

    @GET("/ords/fluid/api/staff")
    Call<StaffData> getAllStuff(@Query("langId") String langId, @Query("typeCode") String typeCode);

    @POST("/ords/fluid/api/staff")
    Call<State> insertNewStuff(@Body Staff staff);

    @PUT("/ords/fluid/api/staff")
    Call<State> updateStaff(@Body Staff staff);

    @DELETE("/ords/fluid/api/staff")
    Call<State> deleteStuff(@Query("staffId") String staffId);

    @GET("/ords/fluid/api/client")
    Call<CustomerList> getAllClients(@Query("clientId") String clientId, @Query("langId") String langId);

    @PUT("/ords/fluid/api/addAgentFacilities")
    Call<State> addToFacilities(@Query("staffId") String staffId, @Body FacilityCodes facilityCodes);

    @GET("/ords/fluid/api/device")
    Call<DeviceListData> getAllDevicesList();

    @GET("/ords/fluid/api/device")
    Call<DeviceListData> getSpecificDeviceData(@Query("deviceId") String deviceId);

    /*  branches API  */
    @GET("/ords/fluid/api/site")
    Call<BranchesResponse> getBranches(@Query("langId") String language);

    @POST("/ords/fluid/api/site")
    Call<State> addBranch(@Body Branch newBranch);

    @PUT("/ords/fluid/api/site")
    Call<State> updateBranch(@Body Branch newBranch);

    @DELETE("/ords/fluid/api/site")
    Call<State> deleteBranch(@Query("siteId") String siteId);



}
