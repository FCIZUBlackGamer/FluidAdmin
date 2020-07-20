package com.thetatechno.fluidadmin.ui.Schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.utils.WeekDays;

import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ScheduleViewHolder> {

    private List<String> list;
    Context context;


    public ChipAdapter(Context context, List<String> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(context));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );
        view = LayoutInflater.from(context).inflate(R.layout.chip_item, parent, false);

        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleViewHolder holder, final int position) {


        try {

            if (position < list.size()) {
                holder.day.setText(WeekDays.getDay(list.get(position)));
            } else if (position == list.size()) {
                holder.itemView.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    @Override
    public int getItemCount() {

        return list.size() + 1;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        Chip day;


        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);

        }
    }

}
