package com.thetatechno.fluidadmin.ui.appointments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.databinding.AppointmentListItemBinding;
import com.thetatechno.fluidadmin.model.appointment_model.Appointment;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.PreferenceController;
import com.thetatechno.fluidadmin.utils.StringUtil;


import java.util.ArrayList;
import java.util.List;

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.AppointmentViewHolder> {
    private Context mContext;
    private ArrayList<Appointment> appointments = new ArrayList<>();

    public AppointmentListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }
    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AppointmentListItemBinding appointmentListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.appointment_list_item, parent, false);
        return new AppointmentViewHolder(appointmentListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        String languageName = PreferenceController.getInstance(mContext).get(PreferenceController.LANGUAGE);
        if (languageName.contains(Constants.ENGLISH)) {
            holder.appointmentListItemBinding.locationTxt.setText(StringUtil.toCamelCase(appointment.getFacilityEnName()));
            holder.appointmentListItemBinding.doctorNameTxt.setText(StringUtil.toCamelCase(appointment.getProviderEnName()));
            holder.appointmentListItemBinding.customerNameTxt.setText(StringUtil.toCamelCase(appointment.getCustomerEnName()));
        } else {
            holder.appointmentListItemBinding.locationTxt.setText(StringUtil.toCamelCase(appointment.getFacilityArName()));
            holder.appointmentListItemBinding.doctorNameTxt.setText(StringUtil.toCamelCase(appointment.getProviderArName()));
            holder.appointmentListItemBinding.customerNameTxt.setText(StringUtil.toCamelCase(appointment.getCustomerArName()));
        }
        holder.appointmentListItemBinding.scheduledTimeTxt.setText(StringUtil.displayTime(appointment.getScheduledTime()));
        holder.appointmentListItemBinding.expectedTimeTxt.setText(StringUtil.displayTime(appointment.getExpectedTime()));
        if (!appointment.getScheduledTime().isEmpty()) {
            holder.appointmentListItemBinding.dayTxt.setText(StringUtil.getDay(appointment.getScheduledTime()));
            holder.appointmentListItemBinding.monthTxt.setText(StringUtil.getMonth(appointment.getScheduledTime()));
            holder.appointmentListItemBinding.yearTxt.setText(StringUtil.getYear(appointment.getScheduledTime()));
        } else {
            holder.appointmentListItemBinding.dayTxt.setText(StringUtil.getDay(appointment.getArrivalTime()));
            holder.appointmentListItemBinding.monthTxt.setText(StringUtil.getMonth(appointment.getArrivalTime()));
            holder.appointmentListItemBinding.yearTxt.setText(StringUtil.getYear(appointment.getArrivalTime()));
        }
//        holder.appointmentListItemBinding.optionMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                PopupMenu popup = new PopupMenu(mContext, holder.appointmentListItemBinding.optionMenu);
//                popup.inflate(R.menu.option_menu);
//                popup.setOnMenuItemClickListener(item -> {
//                    switch (item.getItemId()) {
//                        case R.id.cancelAppointment:
//                            if(listener!=null)
//                            listener.onOpenCancelAppointmentDialog(appointment.getSltoId());
//                            break;
//                    }
//                    return false;
//                });
//                //displaying the popup
//                popup.show();
//            }
//        });


        if (!appointment.getProviderImagePath().isEmpty()) {
            Glide.with(mContext)
                    .load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + appointment.getProviderImagePath())
                    .circleCrop()
                    .placeholder(R.drawable.man)
                    .into(holder.appointmentListItemBinding.doctorImg);
        } else {

            holder.appointmentListItemBinding.doctorImg.setImageResource(R.drawable.man);
        }

    }

    @Override
    public int getItemCount() {
        if (appointments.size() > 0)
            return appointments.size();
        else
            return 0;
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {

        AppointmentListItemBinding appointmentListItemBinding;

        public AppointmentViewHolder(AppointmentListItemBinding appointmentListItemBinding) {
            super(appointmentListItemBinding.getRoot());
            this.appointmentListItemBinding = appointmentListItemBinding;

        }
    }
}
