package com.thetatechno.fluidadmin.ui.Schedule;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.model.shedule.Schedule;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;

import java.io.Serializable;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_CODE;
import static com.thetatechno.fluidadmin.utils.Constants.ARG_SCHEDULE;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder> {

    Context context;
    List<Schedule> scheduleList;
    OnDeleteListener listener;

    NavController navController;
    Bundle bundle;


    public ScheduleListAdapter(NavController navControlle, Context context, List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
        this.context = context;
        navController = navControlle;
        bundle = new Bundle();

        if (context instanceof OnDeleteListener)
            listener = (OnDeleteListener) context;
        else
            listener = null;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(context));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );
        view = LayoutInflater.from(context).inflate(R.layout.schedual_item_layout, parent, false);

        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleViewHolder holder, final int position) {


        try {

            if (position < scheduleList.size()) {
                holder.itemView.setVisibility(View.VISIBLE);
                holder.schedule_name_txt.setText(scheduleList.get(position).getDescription());
                holder.doctor_name_txt.setText(scheduleList.get(position).getProviderName());
                holder.locationTxt.setText(scheduleList.get(position).getFacilitDescription());
                holder.time_from_txt.setText(scheduleList.get(position).getStartDate());
                holder.time_to_txt.setText(scheduleList.get(position).getEndDate());

                if (!scheduleList.get(position).getImagePath().isEmpty()) {
                    Glide.with(context).load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + scheduleList.get(position).getImagePath())
                            .circleCrop()
                            .into(holder.doctorImg);
                } else {
                    if (!scheduleList.get(position).getSexCode().isEmpty()) {
                        if (scheduleList.get(position).getSexCode().equals(EnumCode.Gender.M.toString())) {
                            holder.doctorImg.setImageResource(R.drawable.man);
                        } else if (scheduleList.get(position).getSexCode().equals(EnumCode.Gender.F.toString())) {
                            holder.doctorImg.setImageResource(R.drawable.ic_girl);
                        }
                    } else {
                        holder.doctorImg.setImageResource(R.drawable.man);
                    }
                }

                holder.optionMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        PopupMenu popup = new PopupMenu(context, holder.optionMenu);
                        popup.inflate(R.menu.schedule_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.showSession:
                                        bundle.putSerializable(ARG_SCHEDULE, (Serializable) scheduleList.get(position));
                                        navController.navigate(R.id.action_scheduleFragment_to_sessionFragment, bundle);
                                        break;
                                    case R.id.editSchedule:

                                        break;
                                    case R.id.deleteSchedule:
                                        //handle delete click
                                        if (listener != null)
                                            listener.onDeleteButtonClicked(scheduleList.get(position));

                                        break;
                                }
                                return false;
                            }
                        });
                        //displaying the popup
                        popup.show();
                    }
                });
            } else if (position == scheduleList.size()) {
                holder.itemView.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    @Override
    public int getItemCount() {

        return scheduleList.size() + 1;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView optionMenu, schedule_name_txt, doctor_name_txt, locationTxt, time_from_txt, time_to_txt;
        ImageView doctorImg;


        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            optionMenu = itemView.findViewById(R.id.optionMenu);
            schedule_name_txt = itemView.findViewById(R.id.schedule_name_txt);
            locationTxt = itemView.findViewById(R.id.locationTxt);
            time_from_txt = itemView.findViewById(R.id.time_from_txt);
            time_to_txt = itemView.findViewById(R.id.time_to_txt);
            doctor_name_txt = itemView.findViewById(R.id.doctor_name_txt);
            doctorImg = itemView.findViewById(R.id.doctorImg);

        }
    }
}
