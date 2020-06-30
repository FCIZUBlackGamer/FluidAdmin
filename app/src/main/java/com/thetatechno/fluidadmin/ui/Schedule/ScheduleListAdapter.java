package com.thetatechno.fluidadmin.ui.Schedule;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.shedule.Schedule;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_CODE;
import static com.thetatechno.fluidadmin.utils.Constants.ARG_SCHEDULE;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ScheduleViewHolder> implements Filterable {

    private Context context;
    private List<Schedule> scheduleList;
    private OnDeleteListener listener;
    private List<Schedule> filteredScheduleList;
    private NavController navController;
    private Bundle bundle;


    ScheduleListAdapter(NavController navControlle, Context context, List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
        this.filteredScheduleList = scheduleList;

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

            if (position < filteredScheduleList.size()) {
                holder.itemView.setVisibility(View.VISIBLE);
                holder.schedule_name_txt.setText(filteredScheduleList.get(position).getDescription());
                holder.doctor_name_txt.setText(filteredScheduleList.get(position).getProviderName());
                holder.locationTxt.setText(filteredScheduleList.get(position).getFacilitDescription());
                holder.time_from_txt.setText(filteredScheduleList.get(position).getStartTime());
                holder.time_to_txt.setText(filteredScheduleList.get(position).getEndTime());
                holder.dateFromTxtView.setText(filteredScheduleList.get(position).getStartDate());
                holder.dateToTxtView.setText(filteredScheduleList.get(position).getEndDate());
                if (!scheduleList.get(position).getImagePath().isEmpty()) {
                    Glide.with(context).load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + scheduleList.get(position).getImagePath())
                            .circleCrop()
                            .placeholder(R.drawable.man)
                            .into(holder.doctorImg);
                } else {
                    if (!filteredScheduleList.get(position).getSexCode().isEmpty()) {
                        if (filteredScheduleList.get(position).getSexCode().equals(EnumCode.Gender.M.toString())) {
                            holder.doctorImg.setImageResource(R.drawable.man);
                        } else if (filteredScheduleList.get(position).getSexCode().equals(EnumCode.Gender.F.toString())) {
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
                                        bundle.putSerializable(ARG_SCHEDULE, (Serializable) filteredScheduleList.get(position));
                                        navController.navigate(R.id.action_scheduleFragment_to_showSessions, bundle);
                                        break;
                                    case R.id.editSchedule:
                                        bundle.putSerializable(ARG_SCHEDULE, (Serializable) filteredScheduleList.get(position));
                                        navController.navigate(R.id.action_scheduleFragment_to_fragmentAddOrUpdateSchedule, bundle);
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
                List<String> selectedDays = new ArrayList<String>();
                int len = filteredScheduleList.get(position).getWorkingDays().length();
                for (int i = 0; i < len; i += 3) {
                    selectedDays.add(filteredScheduleList.get(position).getWorkingDays().substring(i, Math.min(len, i + 3)));
                }
//                String[] selectedDays = scheduleList.get(position).getWorkingDays().split("(?<=\\G...)");

                for (int i = 0; i < selectedDays.size(); i++) {
                    switch (selectedDays.get(i)) {
                        case "SAT":
                            holder.satChip.setChecked(true);
                            break;
                        case "SUN":
                            holder.sundayChip.setChecked(true);
                            break;
                        case "MON":
                            holder.mondayChip.setChecked(true);
                            break;
                        case "TUE":
                            holder.tuesdayChip.setChecked(true);
                            break;
                        case "WED":
                            holder.WednesdayChip.setChecked(true);
                            break;

                        case "THU":
                            holder.thursdayChuip.setChecked(true);
                            break;
                        case "FRI":
                            holder.fridayChip.setChecked(true);
                            break;

                    }
                }

            } else if (position == filteredScheduleList.size()) {
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredScheduleList = scheduleList;
                } else {
                    List<Schedule> filteredList = new ArrayList<>();
                    for (Schedule schedule : scheduleList) {
                        if (StringUtil.isSearchResultExist(schedule.getDescription(), charSequenceString)
                                || StringUtil.isSearchResultExist(schedule.getFacilitDescription(), charSequenceString)
                                || StringUtil.isSearchResultExist(schedule.getProviderName(), charSequenceString)
                        ) {
                            filteredList.add(schedule);
                        }
                        filteredScheduleList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredScheduleList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredScheduleList = (List<Schedule>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView optionMenu, schedule_name_txt, doctor_name_txt, locationTxt, time_from_txt, time_to_txt, dateToTxtView, dateFromTxtView;
        ImageView doctorImg;
        Chip satChip, sundayChip, mondayChip, tuesdayChip, WednesdayChip, thursdayChuip, fridayChip;
        CardView daysCardView;


        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            optionMenu = itemView.findViewById(R.id.scheduleOptionMenu);
            schedule_name_txt = itemView.findViewById(R.id.schedule_name_txt);
            locationTxt = itemView.findViewById(R.id.locationTxt);
            time_from_txt = itemView.findViewById(R.id.time_from_txt);
            time_to_txt = itemView.findViewById(R.id.time_to_txt);
            doctor_name_txt = itemView.findViewById(R.id.doctor_name_txt);
            doctorImg = itemView.findViewById(R.id.doctorImg);
            dateToTxtView = itemView.findViewById(R.id.date_to_txt_view);
            dateFromTxtView = itemView.findViewById(R.id.date_from_txt_view);
            satChip = itemView.findViewById(R.id.SAT);
            sundayChip = itemView.findViewById(R.id.SUN);
            mondayChip = itemView.findViewById(R.id.MON);
            tuesdayChip = itemView.findViewById(R.id.TUE);
            WednesdayChip = itemView.findViewById(R.id.WED);
            thursdayChuip = itemView.findViewById(R.id.THU);
            fridayChip = itemView.findViewById(R.id.FRI);
            daysCardView = itemView.findViewById(R.id.daysCardV);
        }
    }

}
