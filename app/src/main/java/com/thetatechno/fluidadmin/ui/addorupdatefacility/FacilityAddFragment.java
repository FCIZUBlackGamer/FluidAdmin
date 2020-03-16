package com.thetatechno.fluidadmin.ui.addorupdatefacility;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.model.Device;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.ui.HomeActivity;
import com.thetatechno.fluidadmin.ui.facilityList.FacilityListViewModel;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_FACILITY;


public class FacilityAddFragment extends Fragment {
    private Facility facility;
    EditText facilityIdEditTxt, facilityDescriptionEditTxt;
    Button cancelBtn, addOrUpdateBtn;
    boolean isDataFound;
    FacilityAddViewModel facilityAddViewModel;
    NavController navController;
    Spinner deviceIdSpinner, waitingAreaSpinner;
    RadioGroup facilityTypeRadioGroup;
    final static String TAG = "FacilityAddFragment";
    ArrayAdapter<Facility> waitingAreaListAdapter;
    List<Facility> waitAreaDescriptionList;
    List<String> devicesDescriptionList;
    RelativeLayout waitingAreaSpinnerLayout;
    RelativeLayout deviceSpinnerLayout;
    public FacilityAddFragment() {
        // Required empty public constructor
    }


    public static FacilityAddFragment newInstance(Facility facility) {
        FacilityAddFragment fragment = new FacilityAddFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FACILITY, facility);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            facility = (Facility) getArguments().getSerializable(ARG_FACILITY);
        }

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
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
        updateData();
        facilityTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.clinicTyperadioButton) {
                    waitingAreaSpinner.setEnabled(true);
                    deviceIdSpinner.setEnabled(false);
                } else if (checkedId == R.id.waitingAreaTypeRadioButton) {
                    waitingAreaSpinner.setEnabled(false);
                    deviceIdSpinner.setEnabled(true);

                }
            }
        });
        return view;
    }

    private void initViews(View view) {

        facilityIdEditTxt = view.findViewById(R.id.facility_id_et);
        facilityDescriptionEditTxt = view.findViewById(R.id.desc_et);
        facilityTypeRadioGroup = view.findViewById(R.id.typeRadioGroup);
        waitingAreaSpinner = view.findViewById(R.id.waitingAreaSpinner);
        deviceIdSpinner = view.findViewById(R.id.deviceIdSpinner);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        addOrUpdateBtn = view.findViewById(R.id.addOrUpdateBtn);
        waitingAreaSpinnerLayout = view.findViewById(R.id.waitingAreaLayoutSpinner);
        deviceSpinnerLayout = view.findViewById(R.id.deviceIdRelativeLayout);


        addOrUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                if (isDataFound) {

                    facilityAddViewModel.updateFacility(facility,deviceIdSpinner.getSelectedItem().toString(),waitingAreaSpinner.getSelectedItem().toString()).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i(TAG, "update response message " + s);
                            if(!s.isEmpty())
                                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
                            if (s.contains("success")) {
                                onAddOrUpdateClicked();
                            }


                        }
                    });
                } else {

                    facilityAddViewModel.addNewFacility(facility,deviceIdSpinner.getSelectedItem().toString(),waitingAreaSpinner.getSelectedItem().toString()).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i(TAG, "add response message " + s);
                            if(!s.isEmpty())
                            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                            if (s.contains("successfully")) {
                                onAddOrUpdateClicked();
                            }


                        }
                    });

                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelOrBackButtonClicked();
            }
        });

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
                    if (facility != null)
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
    private void getDevicesListDescription(){
        facilityAddViewModel.getDevicesList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> deviceList) {
                if (deviceList.size() > 0) {
                    devicesDescriptionList = deviceList;
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(getContext(), R.layout.simple_spinner_dropdown_item, devicesDescriptionList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    deviceIdSpinner.setAdapter(adapter);

                    if (facility != null)
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
        navController.navigate(R.id.action_facilityAddFragment_to_clinicList);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (facility.getCode() != null) {
            ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Update Facility");
        } else {

            ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Add Facility");
        }
    }

    private void updateData() {
        if (facility != null) {
            isDataFound = true;
            facilityIdEditTxt.setText(facility.getCode());
            facilityIdEditTxt.setEnabled(false);
            facilityDescriptionEditTxt.setText(facility.getDescription());
            if (facility.getType().equals(EnumCode.ClinicTypeCode.CLINIC.toString())) {
                facilityTypeRadioGroup.check(R.id.clinicTyperadioButton);
                deviceIdSpinner.setEnabled(false);
                waitingAreaSpinner.setEnabled(true);
            } else if (facility.getType().equals(EnumCode.ClinicTypeCode.WAITAREA.toString())) {
                facilityTypeRadioGroup.check(R.id.waitingAreaTypeRadioButton);
                waitingAreaSpinner.setEnabled(false);
                deviceIdSpinner.setEnabled(true);
            }
            addOrUpdateBtn.setHint(getResources().getString(R.string.update_txt));

        } else {
            isDataFound = false;
            facility = new Facility();
            facilityIdEditTxt.setText("");
            facilityIdEditTxt.setEnabled(true);
            facilityDescriptionEditTxt.setText("");
            addOrUpdateBtn.setHint(getResources().getString(R.string.add_txt));
            waitingAreaSpinner.setEnabled(false);

            deviceIdSpinner.setEnabled(false);

        }
    }

    private void getData() {
        facility.setCode(facilityIdEditTxt.getText().toString());
        facility.setDescription(facilityDescriptionEditTxt.getText().toString());
        int id = facilityTypeRadioGroup.getCheckedRadioButtonId();
        if (id == R.id.clinicTyperadioButton)
            facility.setType(EnumCode.ClinicTypeCode.CLINIC.toString());
        else
            facility.setType(EnumCode.ClinicTypeCode.WAITAREA.toString());

        facility.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
        if (id == facilityTypeRadioGroup.getCheckedRadioButtonId())
            facility.setWaitingAreaId(waitingAreaSpinner.getSelectedItem().toString());
    }


}
