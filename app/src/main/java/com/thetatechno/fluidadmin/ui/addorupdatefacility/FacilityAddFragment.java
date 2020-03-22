package com.thetatechno.fluidadmin.ui.addorupdatefacility;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.ui.HomeActivity;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_FACILITY;


public class FacilityAddFragment extends Fragment {
    private Facility facility;
    private TextInputEditText facilityIdEditTxt, facilityDescriptionEditTxt;
    private TextInputLayout facilityIdEditTxtInputLayout , facilityDescriptionEditTxtInputLayout;
    private Button cancelBtn, addOrUpdateBtn;
    private boolean isDataFound;
    private FacilityAddViewModel facilityAddViewModel;
    private NavController navController;
    private Spinner deviceIdSpinner, waitingAreaSpinner;
    private RadioGroup facilityTypeRadioGroup;
    private final static String TAG = "FacilityAddFragment";
    private ArrayAdapter<Facility> waitingAreaListAdapter;
    private List<Facility> waitAreaDescriptionList;
    private List<String> devicesDescriptionList;
    private String addOrUpdateMessage, addNewFacilityMessage;
    private ImageView deviceArrowDownImg;
    private ImageView waitingAreaArrowDownImg;
    private String idTxt, descriptionTxt, facilityTypeTxt;
    private String idValidateMessage, descriptionValidateMessage, facilityTypeValidateMessage;

    public FacilityAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            facility = (Facility) getArguments().getSerializable(ARG_FACILITY);
        }
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onCancelOrBackButtonClicked();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.facility_add_view, container, false);
        facilityAddViewModel = ViewModelProviders.of(this).get(FacilityAddViewModel.class);
        initViews(view);
        getWaitingAreaDescriptionList();
        getDevicesListDescription();
        updateUiWithFacilityData();
        facilityTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.clinicTyperadioButton) {
                    updateSpinnersInUiWhenRoomTypeSelected();

                } else if (checkedId == R.id.waitingAreaTypeRadioButton) {
                    updateSpinnersInUiWhenWaitingAreaTypeSelected();

                }
            }
        });
        addOrUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EspressoTestingIdlingResource.increment();
                idTxt = facilityIdEditTxt.getText().toString();
                descriptionTxt = facilityDescriptionEditTxt.getText().toString();
                int id = facilityTypeRadioGroup.getCheckedRadioButtonId();
                if (id == R.id.clinicTyperadioButton)
                    facilityTypeTxt = EnumCode.ClinicTypeCode.CLINIC.toString();
                else if (id == R.id.waitingAreaTypeRadioButton)
                    facilityTypeTxt = EnumCode.ClinicTypeCode.WAITAREA.toString();
                else
                    facilityTypeTxt = "";

                if (isDataValid()) {
                    addDataToFacilityObject();
                    if (isDataFound) {
                        if (addOrUpdateMessage == null)
                            updateFacility();
                        else
                            facilityAddViewModel.updateFacility(facility, deviceIdSpinner.getSelectedItem().toString(), waitingAreaSpinner.getSelectedItem().toString());
                    } else {
                        if (addNewFacilityMessage == null)
                            addNewFacility();
                        else
                            facilityAddViewModel.addNewFacility(facility, deviceIdSpinner.getSelectedItem().toString(), waitingAreaSpinner.getSelectedItem().toString());

                    }
                }
                EspressoTestingIdlingResource.decrement();

            }

        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelOrBackButtonClicked();
            }
        });
        return view;
    }

    private void addNewFacility() {
        facilityAddViewModel.addNewFacility(facility, deviceIdSpinner.getSelectedItem().toString(), waitingAreaSpinner.getSelectedItem().toString()).observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
              EspressoTestingIdlingResource.increment();
                addNewFacilityMessage = s;
                Log.i(TAG, "add response message " + addNewFacilityMessage);
                if (!addNewFacilityMessage.isEmpty())
                    Toast.makeText(getActivity(), addNewFacilityMessage, Toast.LENGTH_SHORT).show();
                if (addNewFacilityMessage.contains("successfully")) {
                    onAddOrUpdateClicked();
                }
                EspressoTestingIdlingResource.decrement();


            }
        });
    }

    private void initViews(View view) {

        facilityIdEditTxt = view.findViewById(R.id.facilityIdEdtTxt);
        facilityDescriptionEditTxt = view.findViewById(R.id.facilityDesriptionEdtTxt);
        facilityIdEditTxtInputLayout = view.findViewById(R.id.facilityIdTxtInputLayout);
        facilityDescriptionEditTxtInputLayout = view.findViewById(R.id.facility_description_txt_input_layout);
        facilityTypeRadioGroup = view.findViewById(R.id.typeRadioGroup);
        waitingAreaSpinner = view.findViewById(R.id.waitingAreaSpinner);
        deviceIdSpinner = view.findViewById(R.id.deviceIdSpinner);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        addOrUpdateBtn = view.findViewById(R.id.addOrUpdateFacilityBtn);
        deviceArrowDownImg = view.findViewById(R.id.device_arrow_down_img);
        waitingAreaArrowDownImg = view.findViewById(R.id.waiting_area_arrow_down_img);

    }

    private void getWaitingAreaDescriptionList() {
        facilityAddViewModel.getFacilityDataForWaitingAreaList("").observe(this, new Observer<List<Facility>>() {
            @Override
            public void onChanged(List<Facility> waitAreaList) {
                if (waitAreaList.size() > 0) {
                    waitAreaDescriptionList = waitAreaList;
                    waitingAreaListAdapter =
                            new ArrayAdapter<>(getContext(), R.layout.simple_spinner_dropdown_item, waitAreaDescriptionList);
                    waitingAreaListAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    waitingAreaSpinner.setAdapter(waitingAreaListAdapter);
                    if (facility.getWaitingAreaId() != null) {
                        for (int i = 0; i < waitAreaDescriptionList.size(); i++) {
                            if (waitAreaDescriptionList.get(i).getDescription().equals(facility.getWaitingAreaDescription())) {
                                waitingAreaSpinner.setSelection(i);
                            }
                        }
                    }
                }
            }
        });
    }

    private void getDevicesListDescription() {
        facilityAddViewModel.getDevicesList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> deviceList) {
                if (deviceList.size() > 0) {
                    devicesDescriptionList = deviceList;
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(getContext(), R.layout.simple_spinner_dropdown_item, devicesDescriptionList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    deviceIdSpinner.setAdapter(adapter);
                    if (facility.getDeviceId() != null) {
                        for (int i = 0; i < devicesDescriptionList.size(); i++) {
                            if (devicesDescriptionList.get(i).equals(facility.getDeviceDescription())) {
                                deviceIdSpinner.setSelection(i);
                            }
                        }
                    }

                }
            }
        });
    }

    private void onCancelOrBackButtonClicked() {
        navController.popBackStack();

    }

    void onAddOrUpdateClicked() {
        EspressoTestingIdlingResource.increment();
        navController.navigate(R.id.action_facilityAddFragment_to_clinicList);
        EspressoTestingIdlingResource.decrement();

    }

    private void updateUiWithFacilityData() {
        if (facility != null) {
            isDataFound = true;
            facilityIdEditTxt.setText(facility.getId());
            facilityIdEditTxt.setEnabled(false);
            facilityDescriptionEditTxt.setText(facility.getDescription());
            if (facility.getType().equals(EnumCode.ClinicTypeCode.CLINIC.toString())) {
                facilityTypeRadioGroup.check(R.id.clinicTyperadioButton);
                updateSpinnersInUiWhenRoomTypeSelected();

            } else if (facility.getType().equals(EnumCode.ClinicTypeCode.WAITAREA.toString())) {
                facilityTypeRadioGroup.check(R.id.waitingAreaTypeRadioButton);
                updateSpinnersInUiWhenWaitingAreaTypeSelected();

            }
            updateTitle(getResources().getString(R.string.update_facility));
            addOrUpdateBtn.setHint(getResources().getString(R.string.update_txt));

        } else {
            isDataFound = false;
            facility = new Facility();
            facilityIdEditTxt.setText("");
            facilityIdEditTxt.setEnabled(true);
            facilityDescriptionEditTxt.setText("");
            addOrUpdateBtn.setHint(getResources().getString(R.string.add_txt));
            updateSpinnersInUiWhenAddingNewFacility();
            updateTitle(getResources().getString(R.string.add_facility));
        }
    }

    private void addDataToFacilityObject() {
        facility.setId(idTxt);
        facility.setDescription(descriptionTxt);
        facility.setType(facilityTypeTxt);
        facility.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }

    private void updateSpinnersInUiWhenRoomTypeSelected() {
        deviceIdSpinner.setEnabled(false);
        waitingAreaSpinner.setEnabled(true);
        waitingAreaArrowDownImg.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        deviceArrowDownImg.setImageResource(R.drawable.ic_arrow_drop_down_gray);

    }

    private void updateSpinnersInUiWhenWaitingAreaTypeSelected() {
        waitingAreaSpinner.setEnabled(false);
        deviceIdSpinner.setEnabled(true);
        waitingAreaArrowDownImg.setImageResource(R.drawable.ic_arrow_drop_down_gray);
        deviceArrowDownImg.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
    }

    private void updateSpinnersInUiWhenAddingNewFacility() {
        waitingAreaSpinner.setEnabled(false);
        deviceIdSpinner.setEnabled(false);
        deviceArrowDownImg.setImageResource(R.drawable.ic_arrow_drop_down_gray);
        waitingAreaArrowDownImg.setImageResource(R.drawable.ic_arrow_drop_down_gray);
    }

    private void updateTitle(String message) {
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle(message);

    }

    private void updateFacility() {
        EspressoTestingIdlingResource.increment();
        facilityAddViewModel.updateFacility(facility, deviceIdSpinner.getSelectedItem().toString(), waitingAreaSpinner.getSelectedItem().toString()).observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                addOrUpdateMessage = s;
                EspressoTestingIdlingResource.decrement();
                Log.i(TAG, "update response message " + addOrUpdateMessage);
                if (!addOrUpdateMessage.isEmpty())
                    Toast.makeText(getActivity(), addOrUpdateMessage, Toast.LENGTH_SHORT);
                if (addOrUpdateMessage.contains("success")) {
                    EspressoTestingIdlingResource.increment();
                    onAddOrUpdateClicked();
                    EspressoTestingIdlingResource.decrement();
                }

            }
        });
    }


    private boolean isDataValid() {
        if (isIdValid(idTxt) && isDescriptionValid(descriptionTxt) && isFacilityTypeSelected(facilityTypeTxt) )
            return true;
        else
            return false;
    }

    private boolean isIdValid(String id) {
        idValidateMessage = facilityAddViewModel.validateId(id);
        if (idValidateMessage.isEmpty())
            return true;
        else {
            facilityIdEditTxtInputLayout.setError(idValidateMessage);
            requestFocus(facilityIdEditTxt);
            return false;
        }

    }

    private boolean isDescriptionValid(String description) {
        descriptionValidateMessage = facilityAddViewModel.validateDescription(description);
        if (descriptionValidateMessage.isEmpty())

            return true;
        else {
            facilityDescriptionEditTxtInputLayout.setError(descriptionValidateMessage);
            requestFocus(facilityDescriptionEditTxt);
            return false;
        }

    }

    private boolean isFacilityTypeSelected(String facilityype) {
        facilityTypeValidateMessage = facilityAddViewModel.validateFacilityType(facilityype);
        if (facilityTypeValidateMessage.isEmpty())
            return true;
        else {
            Toast.makeText(getContext(),facilityTypeValidateMessage,Toast.LENGTH_SHORT).show();
            return false;
        }

    }



    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
