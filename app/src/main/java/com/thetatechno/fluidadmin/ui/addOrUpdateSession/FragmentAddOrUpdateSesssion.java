package com.thetatechno.fluidadmin.ui.addOrUpdateSession;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.ui.addOrUpdateSchedule.AddOrUpdateScheduleViewModel;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentAddOrUpdateSesssion extends Fragment {
    private AddSessionLayoutBinding binding;
    private AddOrUpdateScheduleViewModel addOrUpdateScheduleViewModel;
    private AddOrUpdateSessionViewModel addOrUpdateSessionViewModel;
    private ArrayList<Staff> providerArrayList = new ArrayList<>();
    private ArrayList<Facility> facilityArrayList = new ArrayList<>();
    private ArrayList<Branch> branchesList = new ArrayList<>();
    private String providerId;
    private String facilityId;
    private String siteId;
    private NavController navController;
    Session session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_session_layout, container, false);
        addOrUpdateScheduleViewModel = ViewModelProviders.of(this).get(AddOrUpdateScheduleViewModel.class);
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
        } else {
            session = null;
        }
        addOrUpdateScheduleViewModel.getStaffData().observe(getViewLifecycleOwner(), staffData -> {
            if (staffData.getStaffData() != null) {
                providerArrayList = (ArrayList<Staff>) staffData.getStaffData().getStaffList();
                ArrayAdapter<Staff> staffArrayAdapter = new ArrayAdapter<Staff>(getContext(), R.layout.dropdown_menu_popup_item, providerArrayList);
                binding.providerAutoCompleteTextView.setAdapter(staffArrayAdapter);
                if (session != null) {
                    binding.providerAutoCompleteTextView.setText(session.getProviderName());
                }
            } else {
                Toast.makeText(getContext(), staffData.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        addOrUpdateScheduleViewModel.getAllFacilities().observe(getViewLifecycleOwner(), staffData -> {
            if (staffData != null) {
                facilityArrayList = (ArrayList<Facility>) staffData.getFacilities();
                ArrayAdapter<Facility> facilityArrayAdapter = new ArrayAdapter<Facility>(getContext(), R.layout.dropdown_menu_popup_item, facilityArrayList);
                binding.facilityAutoCompleteTextView.setAdapter(facilityArrayAdapter);
                if (session != null) {
                    binding.facilityAutoCompleteTextView.setText(session.getFacilitDescription());
                }
            }
        });
        getBranchesList();
        binding.providerAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                providerId = providerArrayList.get(position).getStaffId();
            }
        });
        binding.facilityAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                facilityId = facilityArrayList.get(position).getId();

            }
        });
        binding.selectDateimg.setOnClickListener(v -> {
            showDatePicker(binding.dateTxt, binding.timeFromTxt, binding.timeToTxt);
        });

        binding.addOrUpdateScheduleBtn.setOnClickListener(v -> {
            CollectDate();
        });

        binding.cancelBtn.setOnClickListener(v -> {
            OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
                @Override
                public void handleOnBackPressed() {
                    onCancelOrBackButtonPressed();
                }
            };

            requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        });
        binding.siteAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                siteId = branchesList.get(position).getSiteId();
            }
        });

    }

    private void onCancelOrBackButtonPressed() {
        navController.popBackStack();
    }

    private void getBranchesList() {
        addOrUpdateScheduleViewModel.getAllBranches().observe(getViewLifecycleOwner(), new Observer<BranchesResponse>() {
            @Override
            public void onChanged(BranchesResponse branchesResponse) {
                if (branchesResponse != null) {
                    if (branchesResponse.getBranchList() != null) {
                        branchesList = (ArrayList<Branch>) branchesResponse.getBranchList();
                        ArrayAdapter<Branch> branchArrayAdapter = new ArrayAdapter<Branch>(getContext(), R.layout.dropdown_menu_popup_item, branchesList);
                        binding.siteAutoCompleteTextView.setAdapter(branchArrayAdapter);
                        if (session != null) {
                            binding.siteAutoCompleteTextView.setText(session.getSiteId());
                        }
                    }
                }
            }
        });
    }

    private void showDatePicker(TextView date, TextView stime, TextView etime) {
        final Calendar newCalendar = Calendar.getInstance();
        @SuppressLint("SetTextI18n")
        DatePickerDialog StartTime = new DatePickerDialog(getContext(), R.style.DialogTheme, (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            showTimePicker(stime, etime, true);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }

    private void showTimePicker(TextView stime, TextView etime, boolean start) {

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
                if (start) {
                    stime.setText(hourSelected + ":" + minuteSelected);
                    showTimePicker(stime, etime, false);
                } else {
                    etime.setText(hourSelected + ":" + minuteSelected);
                }


            }
        }, hour, minute, true);
        if (start) {
            mTimePicker.setTitle("Time From");
        } else {
            mTimePicker.setTitle("Time To");
        }

        mTimePicker.show();
    }

    private void CollectDate() {
        if (session == null) {
            session = new Session();
            if (providerId != null)
                session.setProviderId(providerId);
            if (facilityId != null)
                session.setFacilityId(facilityId);
            session.setScheduledStart(binding.timeFromTxt.getText().toString());
            session.setScheduledEnd(binding.timeToTxt.getText().toString());
            session.setSessionDate(binding.dateTxt.getText().toString());
            session.setSiteId(siteId);
            session.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
            addOrUpdateSessionViewModel.addSession(session).observe(this, (APIResponse response) -> {
                if (response != null) {
                    Toast.makeText(requireActivity(), response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    if (response.getError().getErrorCode() == 0) {
                        onCancelOrBackButtonPressed();
                    }
                }
            });
        } else {
            if (providerId != null)
                session.setProviderId(providerId);
            if (facilityId != null)
                session.setFacilityId(facilityId);
            session.setScheduledStart(binding.timeFromTxt.getText().toString());
            session.setScheduledEnd(binding.timeToTxt.getText().toString());
            session.setSessionDate(binding.dateTxt.getText().toString());
            if(siteId!=null)
            session.setSiteId(siteId);
            session.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());

            addOrUpdateSessionViewModel.updateSession(session).observe(this, response -> {
                if (response != null) {
                    Toast.makeText(requireActivity(), response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    if (response.getError().getErrorCode() == 0) {
                        onCancelOrBackButtonPressed();
                    }
                }
            });
        }
    }
}
