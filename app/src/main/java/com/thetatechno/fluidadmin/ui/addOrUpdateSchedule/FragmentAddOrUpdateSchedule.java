package com.thetatechno.fluidadmin.ui.addOrUpdateSchedule;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.databinding.AddScheduleLayoutBinding;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.shedule.Schedule;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentAddOrUpdateSchedule extends Fragment {
    AddScheduleLayoutBinding binding;
    AddOrUpdateScheduleViewModel addOrUpdateScheduleViewModel;
    ArrayList<Staff> providerArrayList = new ArrayList<>();
    ArrayList<Facility> facilityArrayList = new ArrayList<>();
    private String providerId;
    String facilityId;
    NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.add_schedule_layout, container, false);
        addOrUpdateScheduleViewModel = ViewModelProviders.of(this).get(AddOrUpdateScheduleViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        addOrUpdateScheduleViewModel.getStaffData().observe(getViewLifecycleOwner(), staffData -> {
            if (staffData != null) {
                providerArrayList = (ArrayList<Staff>) staffData.getStaffList();
                ArrayAdapter<Staff> staffArrayAdapter = new ArrayAdapter<Staff>(getContext(), R.layout.dropdown_menu_popup_item, providerArrayList);
                binding.providerAutoCompleteTextView.setAdapter(staffArrayAdapter);
            }
        });
        addOrUpdateScheduleViewModel.getAllFacilities().observe(getViewLifecycleOwner(), staffData -> {
            if (staffData != null) {
                facilityArrayList = (ArrayList<Facility>) staffData.getFacilities();
                ArrayAdapter<Facility> facilityArrayAdapter = new ArrayAdapter<Facility>(getContext(), R.layout.dropdown_menu_popup_item, facilityArrayList);
                binding.facilityAutoCompleteTextView.setAdapter(facilityArrayAdapter);
            }
        });
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
        binding.selectDateFromimg.setOnClickListener(v -> {
            showDatePicker(binding.dateFromTxt, binding.timeFromTxt);
        });

        binding.selectDateToimg.setOnClickListener(v -> {
            showDatePicker(binding.dateToTxt, binding.timeToTxt);
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

    }

    private void onCancelOrBackButtonPressed() {
        navController.popBackStack();
    }

    private void showDatePicker(TextView date, TextView time) {
        final Calendar newCalendar = Calendar.getInstance();
        @SuppressLint("SetTextI18n")
        DatePickerDialog StartTime = new DatePickerDialog(getContext(), R.style.DialogTheme, (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
            showTimePicker(time);
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
        Schedule schedule = new Schedule();
        schedule.setDescription(binding.scheduleDescriptionTiet.getText().toString());
        schedule.setProviderId(providerId);
        schedule.setFacilityId(facilityId);
        schedule.setEndDate(binding.dateToTxt.getText().toString());
        schedule.setStartDate(binding.dateFromTxt.getText().toString());
        schedule.setStartTime(binding.timeFromTxt.getText().toString());
        schedule.setEndTime(binding.timeToTxt.getText().toString());
        schedule.setWorkingDays(getSelectedDays());
        addOrUpdateScheduleViewModel.addSchedule(schedule).observe(this, error -> {
            //Handle Error Message
            Toast.makeText(requireActivity(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            if (error.getErrorCode() == 0) {
                onCancelOrBackButtonPressed();
            }
        });
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
}
