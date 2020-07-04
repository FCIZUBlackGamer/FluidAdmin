package com.thetatechno.fluidadmin.ui.addOrUpdateSession;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.databinding.AddSessionLayoutBinding;
import com.thetatechno.fluidadmin.model.APIResponse;
import com.thetatechno.fluidadmin.model.Error;
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.ui.HomeActivity;
import com.thetatechno.fluidadmin.ui.addOrUpdateSchedule.AddOrUpdateScheduleViewModel;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentAddOrUpdateSesssion extends Fragment implements TextWatcher {
    private AddSessionLayoutBinding binding;
    private AddOrUpdateSessionViewModel addOrUpdateSessionViewModel;
    private ArrayList<Staff> providerArrayList = new ArrayList<>();
    private ArrayList<Facility> facilityArrayList = new ArrayList<>();
    private ArrayList<Branch> branchesList = new ArrayList<>();
    private String providerId;
    private String facilityId;
    private String siteId = "";
    private Error addOrUpdateResponse;
    private NavController navController;
    Session session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_session_layout, container, false);
        addOrUpdateSessionViewModel = ViewModelProviders.of(this).get(AddOrUpdateSessionViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null && getArguments().getSerializable(Constants.ARG_SESSION) != null) {
            session = (Session) getArguments().getSerializable(Constants.ARG_SESSION);
            binding.timeToTxt.setText(session.getScheduledEnd());
            binding.timeFromTxt.setText(session.getScheduledStart());
            binding.dateTxt.setText(session.getSessionDate());
            binding.addOrUpdateScheduleBtn.setText(R.string.update_txt);
            binding.providerAutoCompleteTextView.setEnabled(false);
            binding.facilityAutoCompleteTextView.setEnabled(false);
            binding.siteAutoCompleteTextView.setEnabled(false);
            binding.dateTxt.setEnabled(false);
            updateTitle(R.string.update_session);
        } else {
            session = null;
            binding.addOrUpdateScheduleBtn.setText(R.string.add_txt);
            updateTitle(R.string.add_session);
        }
        addOrUpdateSessionViewModel.getStaffData().observe(getViewLifecycleOwner(), staffData -> {
            if (staffData.getStaffData() != null) {
                providerArrayList = (ArrayList<Staff>) staffData.getStaffData().getStaffList();
                ArrayAdapter<Staff> staffArrayAdapter = new ArrayAdapter<Staff>(getContext(), R.layout.dropdown_menu_popup_item, providerArrayList);
                binding.providerAutoCompleteTextView.setAdapter(staffArrayAdapter);
                if (session != null) {
                    binding.providerAutoCompleteTextView.setText(session.getProviderName(), false);
                }
            } else {
                Toast.makeText(getContext(), staffData.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        addOrUpdateSessionViewModel.getFacilities(siteId).observe(getViewLifecycleOwner(), staffData -> {
            if (staffData != null) {
                facilityArrayList = (ArrayList<Facility>) staffData.getFacilities();
                ArrayAdapter<Facility> facilityArrayAdapter = new ArrayAdapter<Facility>(getContext(), R.layout.dropdown_menu_popup_item, facilityArrayList);
                binding.facilityAutoCompleteTextView.setAdapter(facilityArrayAdapter);
                if (session != null) {
                    binding.facilityAutoCompleteTextView.setText(session.getFacilitDescription(), false);
                }
            }
        });
        getBranchesList();
        binding.providerAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                providerId = ((Staff) parent.getItemAtPosition(position)).getStaffId();
            }
        });
        binding.facilityAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                facilityId = ((Facility) parent.getItemAtPosition(position)).getId();

            }
        });
        binding.cardView2.setOnClickListener(v -> {
            showDatePicker(binding.dateTxt);
        });

        binding.cardView3.setOnClickListener(v -> {
            showTimePicker(binding.timeFromTxt);
        });

        binding.cardView4.setOnClickListener(v -> {
            showTimePicker(binding.timeToTxt);
        });

        binding.addOrUpdateScheduleBtn.setOnClickListener(v -> {
            if (isValidData())
                getDataFromUIAndAddOrUpdateSession();
        });

        binding.cancelBtn.setOnClickListener(v -> {
            onCancelOrBackButtonPressed();
        });
        binding.siteAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                siteId = ((Branch) parent.getItemAtPosition(position)).getSiteId();
            }
        });

    }

    private void onCancelOrBackButtonPressed() {
        navController.popBackStack();
    }

    private void getBranchesList() {
        addOrUpdateSessionViewModel.getAllBranches().observe(getViewLifecycleOwner(), new Observer<BranchesResponse>() {
            @Override
            public void onChanged(BranchesResponse branchesResponse) {
                if (branchesResponse != null) {
                    if (branchesResponse.getBranchList() != null) {
                        branchesList = (ArrayList<Branch>) branchesResponse.getBranchList();
                        ArrayAdapter<Branch> branchArrayAdapter = new ArrayAdapter<Branch>(getContext(), R.layout.dropdown_menu_popup_item, branchesList);
                        binding.siteAutoCompleteTextView.setAdapter(branchArrayAdapter);
                        if (session != null) {
                            for (Branch branch : branchesList) {
                                if (branch.getSiteId().equals(session.getSiteId()))
                                    binding.siteAutoCompleteTextView.setText(branch.getDescription(), false);
                            }
                        }
                    }
                }
            }
        });
    }

    private void showDatePicker(TextView date) {
        final Calendar newCalendar = Calendar.getInstance();
        @SuppressLint("SetTextI18n")
        DatePickerDialog StartTime = new DatePickerDialog(getContext(), R.style.DialogTheme, (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }

    private void showTimePicker(TextView stime) {

        final Calendar newCalendar = Calendar.getInstance();
        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker = new TimePickerDialog(requireActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hourSelected;
                String minuteSelected;
                if (selectedHour < 10) {
                    hourSelected = "0" + selectedHour;

                } else {
                    hourSelected = selectedHour + "";
                }
                if (selectedMinute < 10) {
                    minuteSelected = "0" + selectedMinute;
                } else {
                    minuteSelected = selectedMinute + "";
                }
                stime.setText(String.format("%s:%s", hourSelected, minuteSelected));

            }
        }, hour, minute, false);
        mTimePicker.show();
    }

    private void getDataFromUIAndAddOrUpdateSession() {
        if (session == null) {
            session = new Session();
            addDataToSessionObject();
            if (addOrUpdateResponse == null)
                addSession();
            else
                addOrUpdateSessionViewModel.addSession(session);

        } else {
            addDataToSessionObject();
            if (addOrUpdateResponse == null)
                updateSession();
            else
                addOrUpdateSessionViewModel.updateSession(session);

        }
    }

    private void addDataToSessionObject() {

        if (providerId != null)
            session.setProviderId(providerId);
        if (facilityId != null)
            session.setFacilityId(facilityId);
        session.setScheduledStart(binding.timeFromTxt.getText().toString());
        session.setScheduledEnd(binding.timeToTxt.getText().toString());
        session.setSessionDate(binding.dateTxt.getText().toString());
        session.setSiteId(siteId);
        session.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }

    private void updateTitle(int resourceId) {
        ((HomeActivity) requireActivity()).getSupportActionBar().setTitle(resourceId);

    }

    private void addSession() {
        addOrUpdateSessionViewModel.addSession(session).observe(this, (APIResponse response) -> {
            if (response != null) {
                Toast.makeText(requireActivity(), response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                if (response.getError().getErrorCode() == 0) {
                    onCancelOrBackButtonPressed();
                } else {
                    session = null;
                }
            } else {
                session = null;
            }
        });
    }

    private void updateSession() {
        addOrUpdateSessionViewModel.updateSession(session).observe(this, response -> {
            if (response != null) {

                Toast.makeText(requireActivity(), response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                if (response.getError().getErrorCode() == 0) {
                    onCancelOrBackButtonPressed();
                }
            } else {
                session = null;
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        binding.facilityLayout.setErrorEnabled(false);
        binding.providerLayout.setErrorEnabled(false);
        binding.siteLayout.setErrorEnabled(false);

    }


    private boolean isValidProviderName() {
        String validateProviderNameMessage = addOrUpdateSessionViewModel.validateProviderName(binding.providerAutoCompleteTextView.getText().toString());
        if (validateProviderNameMessage.isEmpty()) {
            binding.providerLayout.setErrorEnabled(false);

            return true;
        } else {
            binding.providerLayout.setErrorEnabled(true);
            binding.providerLayout.setError(validateProviderNameMessage);
            return false;
        }
    }

    private boolean isValidSiteDescription() {
        String validateSiteMessage = addOrUpdateSessionViewModel.validateSite(binding.siteAutoCompleteTextView.getText().toString());
        if (validateSiteMessage.isEmpty()) {
            binding.siteLayout.setErrorEnabled(false);
            return true;
        } else {
            binding.siteLayout.setErrorEnabled(true);
            binding.siteLayout.setError(validateSiteMessage);
            return false;
        }
    }

    private boolean isValidFacility() {
        String validateFacilityMessage = addOrUpdateSessionViewModel.validateFacilityType(binding.facilityAutoCompleteTextView.getText().toString());
        if (validateFacilityMessage.isEmpty()) {
            binding.facilityLayout.setErrorEnabled(false);
            return true;
        } else {
            binding.facilityLayout.setErrorEnabled(true);
            binding.facilityLayout.setError(validateFacilityMessage);
            return false;
        }
    }

    private boolean isValidStartDate() {
        String validateStartDateMessage = addOrUpdateSessionViewModel.validateStartDate(binding.dateTxt.getText().toString());
        if (validateStartDateMessage.isEmpty()) {
            return true;
        } else {
            Toast.makeText(getContext(), validateStartDateMessage, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isValidStartTime() {
        String validateStartTimeMessage = addOrUpdateSessionViewModel.validateStartTime(binding.timeFromTxt.getText().toString());
        if (validateStartTimeMessage.isEmpty()) {
            return true;
        } else {
            Toast.makeText(getContext(), validateStartTimeMessage, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isValidEndTime() {
        String validateEndTimeMessage = addOrUpdateSessionViewModel.validateEndTime(binding.timeToTxt.getText().toString(), binding.timeFromTxt.getText().toString());
        if (validateEndTimeMessage.isEmpty()) {
            return true;
        } else {
            Toast.makeText(getContext(), validateEndTimeMessage, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean isValidData() {
        if (isValidProviderName() && isValidSiteDescription() && isValidFacility() && isValidStartDate()
                && isValidStartTime() && isValidEndTime())
            return true;
        else
            return false;
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
