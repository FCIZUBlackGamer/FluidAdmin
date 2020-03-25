package com.thetatechno.fluidadmin.ui.addorupdatefacility;


import android.widget.LinearLayout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.model.Device;
import com.thetatechno.fluidadmin.model.DeviceListData;
import com.thetatechno.fluidadmin.model.Facilities;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.DeviceRepository;
import com.thetatechno.fluidadmin.retrofiteServices.repositories.FacilityRepository;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;
import com.thetatechno.fluidadmin.utils.Validation;

import java.util.ArrayList;
import java.util.List;

public class FacilityAddViewModel extends ViewModel {

    private MutableLiveData<String> facilityAddOrUpdateMessage = new MutableLiveData<>();
    private FacilityRepository facilityRepository = new FacilityRepository();
    private DeviceRepository deviceRepository = new DeviceRepository();
    private MutableLiveData<List<Facility>> facilitiesWaitListStringMutableLiveData = new MutableLiveData<List<Facility>>();
    private MutableLiveData<List<String>> devicesStringMutableLiveData = new MutableLiveData<List<String>>();
    private String message = "";
    List<Device> deviceList = new ArrayList<>();
    List<Facility> facilities = new ArrayList<>();
    private String typeMessage, idValidateMessage, descriptionMessage;

    public MutableLiveData<String> addNewFacility(Facility facility, String deviceDescription, String waitingAreaDescription) {
        if (facility.getType().equals(EnumCode.ClinicTypeCode.CLINIC.toString())) {
            if (!getWaitingAreaCode(waitingAreaDescription).isEmpty())
                facility.setWaitingAreaId(getWaitingAreaCode(waitingAreaDescription));
            facility.setDeviceId("");
            facility.setDescription("");
        } else if (facility.getType().equals(EnumCode.ClinicTypeCode.WAITAREA.toString())) {
            if (!getDeviceID(deviceDescription).isEmpty())
                facility.setDeviceId(getDeviceID(deviceDescription));
            facility.setWaitingAreaId("");
            facility.setWaitingAreaDescription("");
        }
        facilityRepository.insertFacility(facility, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (Integer.parseInt(b) > 0) {
                    message = "Added facility successfully";
                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    message = "Failed to add facility.";
                }
                facilityAddOrUpdateMessage.setValue(message);

            }
        });
        return facilityAddOrUpdateMessage;
    }

    public MutableLiveData<String> updateFacility(Facility facility, String deviceDescription, String waitingAreaDescription) {
        if (facility.getType().equals(EnumCode.ClinicTypeCode.CLINIC.toString())) {
            if (!getWaitingAreaCode(waitingAreaDescription).isEmpty())
                facility.setWaitingAreaId(getWaitingAreaCode(waitingAreaDescription));
            facility.setDeviceId("");
            facility.setDescription("");
        } else if (facility.getType().equals(EnumCode.ClinicTypeCode.WAITAREA.toString())) {
            if (!getDeviceID(deviceDescription).isEmpty())
                facility.setDeviceId(getDeviceID(deviceDescription));
            facility.setWaitingAreaId("");
            facility.setWaitingAreaDescription("");
        }

        facilityRepository.updateFacility(facility, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (Integer.parseInt(b) > 0) {
                    message = "updated facility successfully";
                } else if (b.equals(Constants.ADD_DELETE_OR_UPDATE_FAIL_STATE)) {
                    message = "Failed to update facility.";
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

        facilityRepository.getFacilityListForSpecificType(facilityId, PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.ClinicTypeCode.WAITAREA.toString(), new OnDataChangedCallBackListener<Facilities>() {
            @Override
            public void onResponse(Facilities facilitiesListResponse) {
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

    public MutableLiveData<List<String>> getDevicesList() {

        deviceRepository.getAllDevices(new OnDataChangedCallBackListener<DeviceListData>() {
            @Override
            public void onResponse(DeviceListData devicesListResponse) {
                if (devicesListResponse != null) {
                    if (devicesListResponse.getDeviceList() != null) {
                        deviceList = devicesListResponse.getDeviceList();
                        List<String> deviceListIds = new ArrayList<>();
                        deviceListIds.add("");
                        for (Device device : devicesListResponse.getDeviceList()) {
                            deviceListIds.add(device.getDescription());
                        }
                        devicesStringMutableLiveData.setValue(deviceListIds);
                    } else {
                        devicesStringMutableLiveData.setValue(null);
                    }


                } else
                    devicesStringMutableLiveData.setValue(null);
            }
        });

        return devicesStringMutableLiveData;

    }

    public String validateFacilityType(String facilityType) {
        if (isValidFacilityType(facilityType)) {
            typeMessage = "";
        } else {
            typeMessage = App.getContext().getResources().getString(R.string.choose_facility_type_error);
        }
        return typeMessage;
    }

    private boolean isValidFacilityType(String speciality) {
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
