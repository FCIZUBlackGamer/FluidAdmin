package com.thetatechno.fluidadmin.ui.addorupdatefacility;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
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
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.ui.HomeActivity;
import com.thetatechno.fluidadmin.ui.facilityList.FacilityListViewModel;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_FACILITY;


public class FacilityAddFragment extends Fragment {
    private Facility facility;
    EditText facility_id_et, desc_et;
    Button cancel_btn, addOrUpdateBtn;
    boolean isDataFound;
    FacilityAddViewModel facilityAddViewModel;
    FacilityListViewModel facilityListViewModel;
    private  final String ARG_CLINIC_TYPE = "clinic_type";
    NavController navController;
    EnumCode.ClinicTypeCode clinicTypeCode;
    Spinner deviceIdSpinner,waitingAreaSpinner;
    RadioGroup facilityTypeRadioGroup;


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
            clinicTypeCode = (EnumCode.ClinicTypeCode) getArguments().getSerializable(ARG_CLINIC_TYPE);
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
        facilityListViewModel = ViewModelProviders.of(this).get(FacilityListViewModel.class);
        initViews(view);
        if (this.getArguments() != null) {
            facility_id_et.setEnabled(false);
        }
        return view;
    }

    private void initViews(View view) {

        facility_id_et = view.findViewById(R.id.facility_id_et);
        desc_et = view.findViewById(R.id.desc_et);
        facilityTypeRadioGroup = view.findViewById(R.id.typeRadioGroup);
        waitingAreaSpinner = view.findViewById(R.id.waitingAreaSpinner);
        deviceIdSpinner = view.findViewById(R.id.deviceIdSpinner);
        cancel_btn = view.findViewById(R.id.cancel_btn);
        addOrUpdateBtn = view.findViewById(R.id.addOrUpdateBtn);
        updateData();
        getWaitingAreaIdList();
        facilityTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.clinicTyperadioButton)
                {
                    waitingAreaSpinner.setEnabled(true);
                }
                else if(checkedId == R.id.waitingAreaTypeRadioButton){
                    waitingAreaSpinner.setEnabled(false);

                }
            }
        });
        addOrUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataFound) {
                    getData();
                    facilityAddViewModel.updateFacility(facility).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i("FacilityFragment", "update response message " + s);
                            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
                            if (s.contains("successfully"))
                                onAddOrUpdateClicked();

                        }
                    });
                } else {
                    facility = new Facility();
                    getData();
                    facilityAddViewModel.addNewFacility(facility).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i("FacilityFragment", "add response message " + s);
                            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                            if (s.contains("successfully"))
                                onAddOrUpdateClicked();

                        }
                    });

                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelOrBackButtonClicked();
            }
        });

    }

    private void getWaitingAreaIdList() {
facilityListViewModel.getFacilityDataForWaitingAreaList("").observe(this, new Observer<List<String>>() {
    @Override
    public void onChanged(List<String> waitAreaList) {
        if(waitAreaList.size()>0) {
            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_dropdown_item, waitAreaList);
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

            waitingAreaSpinner.setAdapter(adapter);

            if(facility!=null)
                if(facility.getWaitingAreaID()!=null) {
                    for (int i = 0; i < waitAreaList.size(); i++) {
                        if (waitAreaList.get(i).equals(facility.getWaitingAreaID())) {
                            waitingAreaSpinner.setSelection(i);
                        }
                    }
                }
        }
    }
});
    }

    private void onCancelOrBackButtonClicked() {

        if (clinicTypeCode != null) {
            if (clinicTypeCode.equals(EnumCode.ClinicTypeCode.CLINIC))
                navController.navigate(R.id.action_facilityAddFragment_to_clinicList);

        }

    }

    void onAddOrUpdateClicked() {
            navController.navigate(R.id.action_facilityAddFragment_to_clinicList);

    }

    @Override
    public void onResume() {
        super.onResume();

        if(facility.getCode()!=null){
            ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Update Provider");
        }else {

            ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Add Provider");
        }
    }
    private void updateData() {
        if (facility != null) {
            isDataFound = true;
            facility_id_et.setText(facility.getCode());
            desc_et.setText(facility.getDescription());
            if(facility.getType().equals(EnumCode.ClinicTypeCode.CLINIC.toString())) {
                facilityTypeRadioGroup.check(R.id.clinicTyperadioButton);
                waitingAreaSpinner.setEnabled(true);
            }
            else if(facility.getType().equals(EnumCode.ClinicTypeCode.WAITAREA.toString())) {
                facilityTypeRadioGroup.check(R.id.waitingAreaTypeRadioButton);
                waitingAreaSpinner.setEnabled(false);
            }
            addOrUpdateBtn.setHint(getResources().getString(R.string.update_txt));

        } else {
            isDataFound = false;
            facility_id_et.setText("");
            desc_et.setText("");
            addOrUpdateBtn.setHint(getResources().getString(R.string.add_txt));

        }
    }

    private void getData() {
        facility.setCode(facility_id_et.getText().toString());
        facility.setDescription(desc_et.getText().toString());
        int id = facilityTypeRadioGroup.getCheckedRadioButtonId();
        if(id == R.id.clinicTyperadioButton)
        facility.setType(EnumCode.ClinicTypeCode.CLINIC.toString());
        else
            facility.setType(EnumCode.ClinicTypeCode.WAITAREA.toString());

        facility.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
        if(id == facilityTypeRadioGroup.getCheckedRadioButtonId())
        facility.setWaitingAreaID(waitingAreaSpinner.getSelectedItem().toString());
    }




}
