package com.thetatecno.fluidadmin.retrofiteServices.interfaces;

import com.thetatecno.fluidadmin.model.Code;
import com.thetatecno.fluidadmin.model.CodeList;
import com.thetatecno.fluidadmin.model.CustomerList;
import com.thetatecno.fluidadmin.model.Facilities;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.model.Person;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.model.StaffData;
import com.thetatecno.fluidadmin.model.State;
import com.thetatecno.fluidadmin.utils.Constants;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface MyServicesInterface {


    @GET("/ords/fluid/api/facility")
    Call<Facilities> getFacilities(@Query(Constants.FACILITY_ID) String facilityId, @Query(Constants.LANG_ID) String langId, @Query(Constants.FACILITY_TYPE_CODE) String typeCode);

    @POST("/ords/fluid/api/facility")
    Call<State> addFacility(@Body Facility facility);

    @DELETE("/ords/fluid/api/facility")
    Call<State> deleteFacility(@Query(Constants.ID) String facilityId);

    @PUT("/ords/fluid/api/facility")
    Call<State> updateFacility(@Body Facility facility);

    @GET("/ords/fluid/api/code")
    Call<CodeList> getCodes(@Query("code") String code, @Query(Constants.LANG_ID) String langId);

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
    Call<State> deleteStuff(@Query("staffId")String staffId);

    @GET("/ords/fluid/api/client")
    Call<CustomerList> getAllClients(@Query("clientId")String clientId, @Query("langId") String langId);
}
