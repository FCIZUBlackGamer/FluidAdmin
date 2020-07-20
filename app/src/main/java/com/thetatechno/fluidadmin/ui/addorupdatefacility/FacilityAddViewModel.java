package com.thetatechno.fluidadmin.ui.addorupdatefacility;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponseModel;
import com.thetatechno.fluidadmin.model.device_model.Device;
import com.thetatechno.fluidadmin.model.device_model.DeviceListModel;
import com.thetatechno.fluidadmin.model.facility_model.FacilitiesResponse;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.network.repositories.BranchesRepository;
import com.thetatechno.fluidadmin.network.repositories.DeviceRepository;
import com.thetatechno.fluidadmin.network.repositories.FacilityRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;
import com.thetatechno.fluidadmin.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class FacilityAddViewModel extends ViewModel {
    private MutableLiveData<String> facilityAddOrUpdateMessage = new MutableLiveData<>();
    private FacilityRepository facilityRepository = new FacilityRepository();
    private BranchesRepository branchesRepository = new BranchesRepository();
    private DeviceRepository deviceRepository = new DeviceRepository();
    private MutableLiveData<List<Facility>> facilitiesWaitListStringMutableLiveData = new MutableLiveData<List<Facility>>();
    private MutableLiveData<DeviceListModel> devicesLiveData = new MutableLiveData<DeviceListModel>();
    private String message = "";
    List<Device> deviceList = new ArrayList<>();
    List<Facility> facilities = new ArrayList<>();
    private String typeMessage, idValidateMessage, descriptionMessage,siteValidateMessage;

    public MutableLiveData<String> addNewFacility(Facility facility, String deviceDescription, String waitingAreaDescription) {
        Facility facilityUpdated = validateFacilityWithData(facility,deviceDescription,waitingAreaDescription);

        facilityRepository.insertFacility(facilityUpdated, (OnDataChangedCallBackListener<Integer>) b -> {
            if (b > 0) {
                message = "Added facility successfully";
            } else  {
                message = "Failed to add facility, Try later";
            }
            facilityAddOrUpdateMessage.setValue(message);

        });
        return facilityAddOrUpdateMessage;
    }

    private Facility validateFacilityWithData(Facility facility, String deviceDescription, String waitingAreaDescription) {
        if (facility.getType().equals(EnumCode.ClinicTypeCode.CLINIC.toString())) {
            if (!getWaitingAreaCode(waitingAreaDescription).isEmpty())
                facility.setWaitingAreaId(getWaitingAreaCode(waitingAreaDescription));
            facility.setDeviceId("");
            facility.setDeviceDescription("");
            facility.setWaitingAreaDescription("");
        } else if (facility.getType().equals(EnumCode.ClinicTypeCode.WAITAREA.toString())) {
            if (!getDeviceID(deviceDescription).isEmpty())
                facility.setDeviceId(getDeviceID(deviceDescription));
            facility.setWaitingAreaId("");
            facility.setWaitingAreaDescription("");
            facility.setDeviceDescription("");
        }
        return facility;
    }
        public MutableLiveData<String> updateFacility(Facility facility, String deviceDescription, String waitingAreaDescription) {
       Facility facilityUpdated = validateFacilityWithData(facility,deviceDescription,waitingAreaDescription);

        facilityRepository.updateFacility(facilityUpdated, new OnDataChangedCallBackListener<Integer>() {
            @Override
            public void onResponse(Integer b) {
                if (b > 0) {
                    message = "updated facility successfully";
                } else {
                    message = "Failed to update,Try later" ;
                }
                facilityAddOrUpdateMessage.setValue(message);

            }
        });
        return facilityAddOrUpdateMessage;
    }

    private String getDeviceID(String deviceDescription) {
        if (!deviceDescription.isEmpty()) {

            for (Device device : deviceList) {
                if (device.getDescription().equals(deviceDescription))
                    return device.getId();
            }
        }
        return "";
    }

    private String getWaitingAreaCode(String waitingAreaDescription) {
        if (!waitingAreaDescription.isEmpty()) {

            for (Facility facility : facilities) {
                if (facility.getDescription().equals(waitingAreaDescription))
                    return facility.getId();
            }
        }
        return "";
    }

    public MutableLiveData<List<Facility>> getFacilityDataForWaitingAreaList(String facilityId) {

        facilityRepository.getFacilityListForSpecificType(facilityId, PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.ClinicTypeCode.WAITAREA.toString(), new OnDataChangedCallBackListener<FacilitiesResponse>() {
            @Override
            public void onResponse(FacilitiesResponse facilitiesListResponse) {
                if (facilitiesListResponse != null) {
                    if (facilitiesListResponse.getFacilities() != null) {
                        facilities = facilitiesListResponse.getFacilities();
                        List<Facility> waitListIds = new ArrayList<>();
                        waitListIds.add(new Facility());
                        for (Facility facility : facilitiesListResponse.getFacilities()) {
                            waitListIds.add(facility);
                        }
                        facilitiesWaitListStringMutableLiveData.setValue(waitListIds);
                    } else {
                        facilitiesWaitListStringMutableLiveData.setValue(null);
                    }


                } else
                    facilitiesWaitListStringMutableLiveData.setValue(null);
            }
        });

        return facilitiesWaitListStringMutableLiveData;

    }

    public MutableLiveData<DeviceListModel> getDevicesList() {
        return deviceRepository.getAllDevices();
    }
    public  MutableLiveData<BranchesResponseModel> getAllBranches(){
        return branchesRepository.getAllBranches(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }

    public String validateFacilityType(String facilityType) {
        if (isValideType(facilityType)) {
            typeMessage = "";
        } else {
            typeMessage = App.getContext().getResources().getString(R.string.choose_facility_type_error);
        }
        return typeMessage;
    }
    public String validateSite(String siteDescription) {
        if (isValideType(siteDescription)) {
            siteValidateMessage = "";
        } else {
            siteValidateMessage = App.getContext().getResources().getString(R.string.choose_facility_type_error);
        }
        return siteValidateMessage;
    }
    private boolean isValideType(String speciality) {
        if (Validation.isValidForWord(speciality))
            return true;
        else
            return false;
    }

    public String validateId(String id) {
        if (isValidForID(id)) {
            idValidateMessage = "";

        } else {
            idValidateMessage = App.getContext().getResources().getString(R.string.id_error_message);
        }
        return idValidateMessage;
    }

    public String validateDescription(String description) {
        if (isValidForDescription(description)) {
            descriptionMessage = "";

        } else {
            descriptionMessage = App.getContext().getResources().getString(R.string.description_error_message);
        }
        return descriptionMessage;
    }

    private boolean isValidForID(String id) {
        if (!Validation.isValidForId(id))
            return false;
        else
            return true;

    }

    private boolean isValidForDescription(String description) {
        if (!Validation.isValidForWord(description))
            return false;
        else
            return true;
    }
}
