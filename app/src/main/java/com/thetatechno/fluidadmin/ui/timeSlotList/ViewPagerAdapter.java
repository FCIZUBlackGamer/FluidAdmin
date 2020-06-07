package com.thetatechno.fluidadmin.ui.timeSlotList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.thetatechno.fluidadmin.model.appointment_model.AppointmentDayDetails;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    List<AppointmentDayDetails> appointmentDayDetailsArrayList = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fm, ArrayList<AppointmentDayDetails> mCurrentLocationList) {
        super(fm);
        this.appointmentDayDetailsArrayList = mCurrentLocationList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = DisplayAppointmentSlotsInSpecificDay.newInstance(appointmentDayDetailsArrayList.get(position).getDate(),appointmentDayDetailsArrayList.get(position).getProviderId(), appointmentDayDetailsArrayList.get(position).getSessionCode());
        return fragment;
    }

    @Override
    public int getItemCount() {
        return appointmentDayDetailsArrayList.size();
    }


}
