package com.thetatechno.fluidadmin.ui.timeSlotList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.databinding.FragmentAppointmentSlotsBinding;
import com.thetatechno.fluidadmin.model.time_slot_model.TimeSlot;
import com.thetatechno.fluidadmin.model.time_slot_model.TimeSlotListData;

import java.util.ArrayList;
import java.util.List;


public class DisplayAppointmentSlotsInSpecificDay extends Fragment {
    private TimeSlotListViewModel timeSlotListViewModel;
    private GridView gridView;
    private TextView dateTextView;
    private String bookDate, providerId, sessionCode;
    private timeSlotListAdapter timeSlotListAdapter;
    FragmentAppointmentSlotsBinding binding;
    private static String ARG_PROVIDER_ID = "providerList";
    private static String ARG_BOOK_DATE = "bookDate";
    private static String ARG_SESSION_CODE = "sessionCode";
    private List<TimeSlot> timeSlotList = new ArrayList<>();
private static String TAG = DisplayAppointmentSlotsInSpecificDay.class.getSimpleName();
    public DisplayAppointmentSlotsInSpecificDay() {
        // Required empty public constructor
    }

    public static DisplayAppointmentSlotsInSpecificDay newInstance(String bookDate, String providerId, String sessionCode) {

        Bundle args = new Bundle();
        args.putString(ARG_BOOK_DATE, bookDate);
        args.putString(ARG_PROVIDER_ID, providerId);
        args.putString(ARG_SESSION_CODE, sessionCode);
        DisplayAppointmentSlotsInSpecificDay fragment = new DisplayAppointmentSlotsInSpecificDay();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            sessionCode = getArguments().getString(ARG_SESSION_CODE);
            providerId = getArguments().getString(ARG_PROVIDER_ID);
            bookDate = getArguments().getString(ARG_BOOK_DATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_appointment_slots, container, false);
        gridView = binding.gridview;
        dateTextView = binding.dateTxtView;
        dateTextView.setText(bookDate);
        Log.i(TAG,"onCreateView method");
        timeSlotListViewModel = new ViewModelProvider(this).get(TimeSlotListViewModel.class);
        timeSlotListAdapter = new timeSlotListAdapter(getContext(), timeSlotList);
        gridView.setAdapter(timeSlotListAdapter);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart method");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
        gridView.scheduleLayoutAnimation();
        if(timeSlotList.size()>0)
        timeSlotListViewModel.getAvailableTimeSlots(bookDate,sessionCode,providerId);
        else
            getAvailableTimeSlots();

    }

    private void getAvailableTimeSlots() {
        timeSlotListViewModel.getAvailableTimeSlots(bookDate, sessionCode, providerId).observe(getViewLifecycleOwner(), new Observer<TimeSlotListData>() {
            @Override
            public void onChanged(TimeSlotListData timeSlotsData) {
                if (timeSlotsData != null) {
                    timeSlotList = timeSlotsData.getTimeSlots();

                    updateGridView();
                }
            }
        });
    }
    private void updateGridView() {
        timeSlotListAdapter.updateList(timeSlotList);
        timeSlotListAdapter.notifyDataSetChanged();
        gridView.smoothScrollToPosition(gridView.getFirstVisiblePosition());
    }
}
