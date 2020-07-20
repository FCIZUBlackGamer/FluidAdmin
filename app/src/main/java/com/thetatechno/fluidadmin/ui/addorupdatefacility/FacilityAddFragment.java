package com.thetatechno.fluidadmin.ui.addorupdatefacility;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioGroup;
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
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponseModel;
import com.thetatechno.fluidadmin.model.device_model.Device;
import com.thetatechno.fluidadmin.model.device_model.DeviceListModel;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.ui.HomeActivity;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.ArrayList;
import java.util.List;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_FACILITY;


public class FacilityAddFragment extends Fragment {
    private Facility facility;
    private TextInputEditText facilityIdEditTxt, facilityDescriptionEditTxt;
    private TextInputLayout facilityIdEditTxtInputLayout, facilityDescriptionEditTxtInputLayout, branchEditTExtInputLayout;
    private TextInputLayout facilityWaitingAreaLayout, facilityDeviceLayout, branches;
    private AutoCompleteTextView facilityWaitingAreaTextView, facilityDeviceTextView, branchesTextView;
    private Button cancelBtn, addOrUpdateBtn;
    private boolean isDataFound;
    private FacilityAddViewModel facilityAddViewModel;
    private NavController navController;
    private RadioGroup facilityTypeRadioGroup;
    private final static String TAG = "FacilityAddFragment";
    private ArrayAdapter<Facility> waitingAreaListAdapter;
    private List<Facility> waitAreaDescriptionList = new ArrayList<>();
    private ArrayList<Device> devicesDescriptionList = new ArrayList<>() ;
    private List<Branch> branchesList;
    private String addOrUpdateMessage, addNewFacilityMessage;
    private String selectedBranchId;
    private String idTxt, descriptionTxt, facilityTypeTxt;
    private String idValidateMessage, descriptionValidateMessage, facilityTypeValidateMessage, siteValidateMessage;

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
        View view = inflater.inflate(R.layout.facility_add_view, container, false);
        facilityAddViewModel = ViewModelProviders.of(this).get(FacilityAddViewModel.class);
        initViews(view);
        getWaitingAreaDescriptionList();
        getDevicesList();
        updateUiWithFacilityData();
        getBranchesList();
        facilityTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.clinicTyperadioButton) {
                    updateDropdownListInUiWhenRoomTypeSelected();

                } else if (checkedId == R.id.waitingAreaTypeRadioButton) {
                    updateDropdownListInUiWhenWaitingAreaTypeSelected();

                }

            }
        });
        branchesTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedBranchId =( (Branch) parent.getItemAtPosition(position)).getSiteId();
                facility.setSiteId(selectedBranchId);

            }
        });
        facilityIdEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                facilityIdEditTxtInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        facilityDescriptionEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                facilityDescriptionEditTxtInputLayout.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {

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
                            facilityAddViewModel.updateFacility(facility, facilityDeviceTextView.getText().toString(), facilityWaitingAreaTextView.getText().toString());
                    } else {
                        if (addNewFacilityMessage == null)
                            addNewFacility();
                        else
                            facilityAddViewModel.addNewFacility(facility, facilityDeviceTextView.getText().toString(), facilityWaitingAreaTextView.getText().toString());

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

    private void getBranchesList() {
        facilityAddViewModel.getAllBranches().observe(getViewLifecycleOwner(), new Observer<BranchesResponseModel>() {
            @Override
            public void onChanged(BranchesResponseModel model) {

                    if (model.getBranchesResponse() != null) {
                        branchesList = model.getBranchesResponse().getBranchList();
                        ArrayAdapter<Branch> branchArrayAdapter = new ArrayAdapter<Branch>(getContext(), R.layout.dropdown_menu_popup_item, branchesList);
                        branchesTextView.setAdapter(branchArrayAdapter);
                        if (facility != null)
                            branchesTextView.setText(facility.getSiteDescription(),false);
                    }
                    else {
                        Toast.makeText(getContext(),model.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }

                }

        });
    }

    private void addNewFacility() {

        facilityAddViewModel.addNewFacility(facility, facilityDeviceTextView.getText().toString(), facilityWaitingAreaTextView.getText().toString()).observe(getActivity(), new Observer<String>() {
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
        facilityWaitingAreaLayout = view.findViewById(R.id.waitingAreaLayout);
        facilityDeviceLayout = view.findViewById(R.id.deviceLayout);
        facilityDeviceTextView = view.findViewById(R.id.deviceAutoCompleteTextView);
        facilityWaitingAreaTextView = view.findViewById(R.id.waitingAreaAutoCompleteTextView);
        branchEditTExtInputLayout = view.findViewById(R.id.branchLayout);
        branchesTextView = view.findViewById(R.id.branchAutoCompleteTextView);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        addOrUpdateBtn = view.findViewById(R.id.addOrUpdateFacilityBtn);

    }

    private void getWaitingAreaDescriptionList() {
        facilityAddViewModel.getFacilityDataForWaitingAreaList("").observe(getActivity(), new Observer<List<Facility>>() {
            @Override
            public void onChanged(List<Facility> waitAreaList) {
                EspressoTestingIdlingResource.increment();
                if (waitAreaList.size() > 0) {
                    waitAreaDescriptionList = waitAreaList;
                    waitingAreaListAdapter =
                            new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, waitAreaDescriptionList);
                    facilityWaitingAreaTextView.setAdapter(waitingAreaListAdapter);
                    if (facility.getWaitingAreaId() != null) {
                        for (int i = 0; i < waitAreaDescriptionList.size(); i++) {
                            if (waitAreaDescriptionList.get(i).getDescription().equals(facility.getWaitingAreaDescription())) {
                                facilityWaitingAreaTextView.setText(facility.getWaitingAreaDescription(), false);

                            }
                        }
                    }
                }
                EspressoTestingIdlingResource.decrement();

            }
        });
    }

    private void getDevicesList() {
        facilityAddViewModel.getDevicesList().observe(getActivity(), new Observer<DeviceListModel>() {
            @Override
            public void onChanged(DeviceListModel model) {
                if (model.getDeviceListData()!=null) {
                    if(model.getDeviceListData().getDeviceList()!=null) {
                        devicesDescriptionList = (ArrayList<Device>) model.getDeviceListData().getDeviceList();
                        ArrayAdapter<Device> adapter =
                                new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, devicesDescriptionList);
                        facilityDeviceTextView.setAdapter(adapter);
                        if (facility.getDeviceId() != null) {
                            for (int i = 0; i < devicesDescriptionList.size(); i++) {
                                if (devicesDescriptionList.get(i).equals(facility.getDeviceDescription())) {
                                    facilityDeviceTextView.setText(facility.getDeviceDescription(), false);
                                }
                            }
                        }
                    }
                }
                else {
                    Toast.makeText(getContext(),model.getErrorMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void onCancelOrBackButtonClicked() {
        navController.popBackStack();

    }

    private void onAddOrUpdateClicked() {
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
                updateDropdownListInUiWhenRoomTypeSelected();

            } else if (facility.getType().equals(EnumCode.ClinicTypeCode.WAITAREA.toString())) {
                facilityTypeRadioGroup.check(R.id.waitingAreaTypeRadioButton);
                updateDropdownListInUiWhenWaitingAreaTypeSelected();
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

    private void updateDropdownListInUiWhenRoomTypeSelected() {
        facilityDeviceLayout.setEnabled(false);
        facilityWaitingAreaLayout.setEnabled(true);
        facilityDeviceTextView.setText("");

    }

    private void updateDropdownListInUiWhenWaitingAreaTypeSelected() {
        facilityWaitingAreaLayout.setEnabled(false);
        facilityDeviceLayout.setEnabled(true);
        facilityWaitingAreaTextView.setText("");

    }

    private void updateSpinnersInUiWhenAddingNewFacility() {
        facilityWaitingAreaLayout.setEnabled(false);
        facilityDeviceLayout.setEnabled(false);

    }

    private void updateTitle(String message) {
        ((HomeActivity) requireActivity()).getSupportActionBar().setTitle(message);

    }

    private void updateFacility() {
        EspressoTestingIdlingResource.increment();
        facilityAddViewModel.updateFacility(facility, facilityDeviceTextView.getText().toString(), facilityWaitingAreaTextView.getText().toString()).observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                addOrUpdateMessage = s;
                Log.i(TAG, "update response message " + addOrUpdateMessage);
                if (!addOrUpdateMessage.isEmpty())
                    if (addOrUpdateMessage.contains("success")) {
                        EspressoTestingIdlingResource.increment();
                        Toast.makeText(getActivity(), addOrUpdateMessage, Toast.LENGTH_SHORT).show();
                        onAddOrUpdateClicked();
                        EspressoTestingIdlingResource.decrement();
                    } else {
                        Toast.makeText(getActivity(), addOrUpdateMessage, Toast.LENGTH_SHORT).show();
                    }

            }
        });
        EspressoTestingIdlingResource.decrement();
    }

    private boolean isDataValid() {
        if (isIdValid(idTxt) && isDescriptionValid(descriptionTxt) && isFacilityTypeSelected(facilityTypeTxt) && isSiteSelected())
            return true;
        else
            return false;
    }

    private boolean isSiteSelected() {
        facilityTypeValidateMessage = facilityAddViewModel.validateSite(branchesTextView.getText().toString());
        if (facilityTypeValidateMessage.isEmpty())
            return true;
        else {
            Toast.makeText(getContext(), facilityTypeValidateMessage, Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private boolean isIdValid(String id) {
        idValidateMessage = facilityAddViewModel.validateId(id);
        if (idValidateMessage.isEmpty()) {
            facilityIdEditTxtInputLayout.setErrorEnabled(false);

            return true;
        } else {
            facilityIdEditTxtInputLayout.setError(idValidateMessage);
            facilityIdEditTxtInputLayout.setErrorEnabled(true);
            return false;
        }

    }

    private boolean isDescriptionValid(String description) {
        descriptionValidateMessage = facilityAddViewModel.validateDescription(description);
        if (descriptionValidateMessage.isEmpty()) {
            facilityDescriptionEditTxtInputLayout.setErrorEnabled(false);
            return true;
        } else {
            facilityDescriptionEditTxtInputLayout.setError(descriptionValidateMessage);
            facilityDescriptionEditTxtInputLayout.setErrorEnabled(true);
            return false;
        }

    }

    private boolean isFacilityTypeSelected(String facilityype) {
        facilityTypeValidateMessage = facilityAddViewModel.validateFacilityType(facilityype);
        if (facilityTypeValidateMessage.isEmpty())
            return true;
        else {
            Toast.makeText(getContext(), facilityTypeValidateMessage, Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
