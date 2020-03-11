package com.thetatechno.fluidadmin.ui.addorupdatefacility;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

import java.util.ArrayList;
import java.util.List;

public class FacilityAddViewModel extends ViewModel {

    private MutableLiveData<String> facilityAddOrUpdateMessage = new MutableLiveData<>();
    private FacilityRepository facilityRepository = new FacilityRepository();
    private DeviceRepository deviceRepository = new DeviceRepository();
    private MutableLiveData<List<String>> facilitiesWaitListStringMutableLiveData = new MutableLiveData<List<String>>();
    private MutableLiveData<List<Device>> devicesStringMutableLiveData = new MutableLiveData<List<Device>>();
    private String message;
    List<Device> deviceList  = new ArrayList<>();

    public MutableLiveData<String> addNewFacility(Facility facility) {
        facilityRepository.insertFacility(facility, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE))
                    message = "Added facility successfully";
                else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE))
                    message = "Failed to add facility.";
                facilityAddOrUpdateMessage.setValue(message);

            }
        });
        return facilityAddOrUpdateMessage;
    }

    public MutableLiveData<String> updateFacility(Facility facility,String deviceDescription) {
        facility.setDeviceId(getDeviceID(deviceDescription));
        facilityRepository.updateFacility(facility, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                if (b.equals(Constants.ADD_OR_UPDATE_SUCCESS_STATE))
                    message = "updated facility successfully";
                else if (b.equals(Constants.ADD_OR_UPDATE_FAIL_STATE))
                    message = "Failed to update facility.";
                facilityAddOrUpdateMessage.setValue(message);

            }
        });
        return facilityAddOrUpdateMessage;
    }
    private String getDeviceID(String deviceDescription){
        if(!deviceDescription.isEmpty()){

            for(Device device : deviceList){
                if(device.getDescription().equals(deviceDescription))
                    return device.getId();
            }
        }
        return "";
    }

    public MutableLiveData<List<String>> getFacilityDataForWaitingAreaList(String facilityId) {

        facilityRepository.getFacilityListForSpecificType(facilityId, PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.ClinicTypeCode.WAITAREA.toString(), new OnDataChangedCallBackListener<Facilities>() {
            @Override
            public void onResponse(Facilities facilitiesListResponse) {
                if (facilitiesListResponse != null) {
                    if (!facilitiesListResponse.getFacilities().equals(null)) {
                        List<String> waitListIds = new ArrayList<>();
                        for (Facility facility : facilitiesListResponse.getFacilities()) {
                            waitListIds.add(facility.getCode());
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

    public MutableLiveData<List<Device>> getDevicesList() {

        deviceRepository.getAllDevices(new OnDataChangedCallBackListener<DeviceListData>() {
            @Override
            public void onResponse(DeviceListData devicesListResponse) {
                if (devicesListResponse != null) {
                    if (devicesListResponse.getDeviceList()!=null) {
                        deviceList = devicesListResponse.getDeviceList();
                        List<Device> deviceListIds = new ArrayList<>();
                        for (Device device : devicesListResponse.getDeviceList()) {
                            deviceListIds.add(device);
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

}
