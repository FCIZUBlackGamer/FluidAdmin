package com.thetatecno.fluidadmin.retrofiteServices.interfaces;

import com.thetatecno.fluidadmin.model.Code;
import com.thetatecno.fluidadmin.model.CodeList;
import com.thetatecno.fluidadmin.model.Facilities;
import com.thetatecno.fluidadmin.model.Facility;
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
    Call <Void> addFacility(@Body Facility facility);
    @DELETE("/ords/fluid/api/facility")
    Call<Void> deleteFacility(@Query(Constants.ID) String facilityId);
    @PUT("/ords/fluid/api/facility")
    Call <Void> updateFacility(@Body Facility facility);
    @GET("/ords/fluid/api/code")
    Call<CodeList> getCodes(@Query("code") String code, @Query(Constants.LANG_ID) String langId);
    @POST("/ords/fluid/api/code")
    Call <Void> addCode(@Body Code code);
    @DELETE("/ords/fluid/api/code")
    Call<Void> deleteCode(@Query("code") String code, @Query("codeType") String codeType);
    @PUT("/ords/fluid/api/code")
    Call <Void> updateCode(@Body Code code);






}
