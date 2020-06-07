package com.thetatechno.fluidadmin.ui.appointments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.databinding.FragmentAppointmentsBinding;
import com.thetatechno.fluidadmin.model.appointment_model.Appointment;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.model.StaffData;
import com.thetatechno.fluidadmin.model.appointment_model.AppointmentListData;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class Appointments extends Fragment {
    AppointmentsViewModel appointmentsViewModel;
    FragmentAppointmentsBinding binding;
    ArrayList<Staff> providerList = new ArrayList<>();
    ArrayList<Appointment> appointmentsList = new ArrayList<>();
    AppointmentListAdapter appointmentListAdapter;
    String date="";
    String providerId = "";
    DatePickerDialog StartTime;
    NavController navController;

    public Appointments() {
    }

    private Observer<AppointmentListData> appointmentsObserver = new Observer<AppointmentListData>() {
        @Override
        public void onChanged(AppointmentListData appointmentListData) {
            if (appointmentListData != null) {
                appointmentsList = (ArrayList<Appointment>) appointmentListData.getAppointments();
                appointmentListAdapter.setAppointments(appointmentsList);
                appointmentListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_appointments, container, false);
        appointmentsViewModel = new ViewModelProvider(this).get(AppointmentsViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        appointmentListAdapter = new AppointmentListAdapter(getContext());
        binding.appointmentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.appointmentRecyclerView.setAdapter(appointmentListAdapter);
        appointmentsViewModel.getStaffData().observe(getViewLifecycleOwner(), new Observer<StaffData>() {
            @Override
            public void onChanged(StaffData staffData) {
                if (staffData != null) {
                    if (staffData.getStaffList() != null) {
                        providerList = (ArrayList<Staff>) staffData.getStaffList();
                        ArrayAdapter<Staff> staffArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, providerList);
                        binding.providerList.setAdapter(staffArrayAdapter);
                    }
                }
            }
        });
        // TODO : observe on appointment method from view model
        binding.providerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                providerId = providerList.get(position).getStaffId();
                appointmentsViewModel.getAppointments(providerId,date);
            }
        });
        binding.dateEditTxt.setText(appointmentsViewModel.getTodayDateInFormat());
        binding.dateEditTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                } else {
                    // Hide your calender here
                    if (StartTime != null && StartTime.isShowing())
                        StartTime.cancel();
                }
            }
        });
        binding.dateEditTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();

            }

        });
        binding.addNewAppointmentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_appointments_to_selectSpecialityAndProviderAndDisplayCalender);
            }
        });
        appointmentsViewModel.getAppointments(providerId,date).observe(getViewLifecycleOwner(), appointmentsObserver);

        return binding.getRoot();
    }

    private void showDatePicker() {
        final Calendar newCalendar = Calendar.getInstance();
        StartTime = new DatePickerDialog(getContext(), R.style.DialogTheme, (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
            appointmentsViewModel.getAppointments(providerId,date);
            binding.dateEditTxt.setText(date);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }

}
