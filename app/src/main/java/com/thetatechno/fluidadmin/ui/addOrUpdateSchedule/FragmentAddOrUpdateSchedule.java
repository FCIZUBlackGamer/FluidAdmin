package com.thetatechno.fluidadmin.ui.addOrUpdateSchedule;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.thetatechno.fluidadmin.databinding.AddScheduleLayoutBinding;
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.shedule.Schedule;
import com.thetatechno.fluidadmin.ui.HomeActivity;
import com.thetatechno.fluidadmin.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FragmentAddOrUpdateSchedule extends Fragment {
    private AddScheduleLayoutBinding binding;
    private AddOrUpdateScheduleViewModel addOrUpdateScheduleViewModel;
    private ArrayList<Staff> providerArrayList = new ArrayList<>();
    private ArrayList<Facility> facilityArrayList = new ArrayList<>();
    private ArrayList<Branch> branchesList = new ArrayList<>();
    private String providerId;
    private String facilityId;
    private String siteId;
    private NavController navController;
    Schedule schedule;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_schedule_layout, container, false);
        addOrUpdateScheduleViewModel = ViewModelProviders.of(this).get(AddOrUpdateScheduleViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        binding.dateFromTxt.setText(formattedDate);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null && getArguments().getSerializable(Constants.ARG_SCHEDULE) != null) {
            schedule = (Schedule) getArguments().getSerializable(Constants.ARG_SCHEDULE);
            binding.scheduleDescriptionTiet.setText(schedule.getDescription());
            binding.timeToTxt.setText(schedule.getEndTime());
            binding.timeFromTxt.setText(schedule.getStartTime());
            binding.dateFromTxt.setText(schedule.getStartDate());
            binding.dateToTxt.setText(schedule.getEndDate());
            displaySelectedDays();
            binding.addOrUpdateScheduleBtn.setText(R.string.update_txt);
            ((HomeActivity) requireActivity()).getSupportActionBar().setTitle(R.string.update_schedule_txt);

        } else {
            schedule = null;
            binding.addOrUpdateScheduleBtn.setText(R.string.add_txt);
            ((HomeActivity) requireActivity()).getSupportActionBar().setTitle(R.string.add_Schedule_txt);

        }
        addOrUpdateScheduleViewModel.getStaffData().observe(getViewLifecycleOwner(), staffData -> {
            if (staffData.getStaffData() != null) {
                providerArrayList = (ArrayList<Staff>) staffData.getStaffData().getStaffList();
                ArrayAdapter<Staff> staffArrayAdapter = new ArrayAdapter<Staff>(getContext(), R.layout.dropdown_menu_popup_item, providerArrayList);
                binding.providerAutoCompleteTextView.setAdapter(staffArrayAdapter);
                if (schedule != null) {
                    binding.providerAutoCompleteTextView.setText(schedule.getProviderName());
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
                if (schedule != null) {
                    binding.facilityAutoCompleteTextView.setText(schedule.getFacilitDescription());
                }
            }
        });
        getBranchesList();
        binding.providerAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                providerId = ((Staff) parent.getItemAtPosition(position)).getStaffId();

//                for (int i = 0; i < providerArrayList.size(); i++) {
//                    if (providerArrayList.get(i).getFirstName().concat(" "+providerArrayList.get(i).getFamilyName()).contains(binding.providerAutoCompleteTextView.getText())){
//                        providerId = providerArrayList.get(i).getStaffId();
//                        Log.e("providerId", providerId);
//                    }
//                }

            }
        });

        binding.facilityAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                facilityId = ((Facility) parent.getItemAtPosition(position)).getId();

//                for (int i = 0; i < facilityArrayList.size(); i++) {
//                    if (facilityArrayList.get(i).getDescription().contains(binding.facilityAutoCompleteTextView.getText())){
//                        facilityId = facilityArrayList.get(position).getId();
//                        Log.e("facilityId", facilityId);
//                    }
//                }

            }
        });
        binding.cardView2.setOnClickListener(v -> {
            showDatePicker(binding.dateFromTxt);
        });
        binding.cardView3.setOnClickListener(v -> {
            showTimePicker(binding.timeFromTxt);
        });
        binding.cardView.setOnClickListener(v -> {
            showDatePicker(binding.dateToTxt);
        });
        binding.cardView4.setOnClickListener(v -> showTimePicker(binding.timeToTxt));

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
                siteId = ((Branch) parent.getItemAtPosition(position)).getSiteId();
//                for (int i = 0; i < branchesList.size(); i++) {
//                    if (branchesList.get(i).getDescription().contains(binding.siteAutoCompleteTextView.getText())){
//                        siteId = branchesList.get(position).getSiteId();
//                        Log.e("SiteId", siteId);
//                    }
//                }

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
                        if (schedule != null)
                            for (Branch branch : branchesList) {
                                if (branch.getSiteId().equals(schedule.getSiteId())) {
                                    binding.siteAutoCompleteTextView.setText(branch.getDescription());
                                    break;
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

    private void showTimePicker(TextView txt) {

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
                txt.setText(hourSelected + ":" + minuteSelected);

            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void CollectDate() {
        if (schedule == null) {
            schedule = new Schedule();
            schedule.setDescription(binding.scheduleDescriptionTiet.getText().toString());
            schedule.setProviderId(providerId);
            schedule.setFacilityId(facilityId);
            schedule.setEndDate(binding.dateToTxt.getText().toString());
            schedule.setStartDate(binding.dateFromTxt.getText().toString());
            schedule.setStartTime(binding.timeFromTxt.getText().toString());
            schedule.setEndTime(binding.timeToTxt.getText().toString());
            schedule.setWorkingDays(getSelectedDays());
            schedule.setSiteId(siteId);
            addOrUpdateScheduleViewModel.addSchedule(schedule).observe(this, response -> {
                if (response != null && response.getError() != null) {
                    Toast.makeText(requireActivity(), response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    if (response.getError().getErrorCode() == 0) {
                        onCancelOrBackButtonPressed();
                    } else {
                        schedule = null;
                    }
                } else {
                    Toast.makeText(requireActivity(), "Error, Failed to add schedule ", Toast.LENGTH_SHORT).show();
                    schedule = null;
                }
            });

        } else {
            schedule.setDescription(binding.scheduleDescriptionTiet.getText().toString());
            if (providerId != null)
                schedule.setProviderId(providerId);
            if (facilityId != null)
                schedule.setFacilityId(facilityId);
            schedule.setEndDate(binding.dateToTxt.getText().toString());
            schedule.setStartDate(binding.dateFromTxt.getText().toString());
            schedule.setStartTime(binding.timeFromTxt.getText().toString());
            schedule.setEndTime(binding.timeToTxt.getText().toString());
            schedule.setWorkingDays(getSelectedDays());
            schedule.setSiteId(siteId);
            addOrUpdateScheduleViewModel.updateSchedule(schedule).observe(this, response -> {
                //Handle Error Message
                Toast.makeText(requireActivity(), response.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                if (response.getError().getErrorCode() == 0) {
                    onCancelOrBackButtonPressed();
                }
            });
        }
    }

    private String getSelectedDays() {
        String daysChar = "";
        if (binding.SAT.isChecked())
            daysChar += "SAT";
        if (binding.SUN.isChecked())
            daysChar += "SUN";
        if (binding.MON.isChecked())
            daysChar += "MON";
        if (binding.TUE.isChecked())
            daysChar += "TUE";
        if (binding.WED.isChecked())
            daysChar += "WED";
        if (binding.THU.isChecked())
            daysChar += "THU";
        if (binding.FRI.isChecked())
            daysChar += "FRI";

        return daysChar;
    }

    private void displaySelectedDays() {

        String[] selectedDays = schedule.getWorkingDays().split("(?<=\\G...)");
        for (int i = 0; i < selectedDays.length; i++) {
            switch (selectedDays[i]) {
                case "SAT":
                    binding.SAT.setChecked(true);
                    break;
                case "SUN":
                    binding.SUN.setChecked(true);
                    break;
                case "MON":
                    binding.MON.setChecked(true);
                    break;
                case "TUE":
                    binding.TUE.setChecked(true);
                    break;
                case "WED":
                    binding.WED.setChecked(true);
                    break;

                case "THU":
                    binding.THU.setChecked(true);
                    break;
                case "FRI":
                    binding.FRI.setChecked(true);
                    break;


            }
        }
    }
}
