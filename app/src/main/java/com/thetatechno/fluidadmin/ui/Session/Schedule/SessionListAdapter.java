package com.thetatechno.fluidadmin.ui.Session.Schedule;

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
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.model.shedule.Schedule;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;

import java.io.Serializable;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_CODE;

public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.ScheduleViewHolder> {

    Context context;

    List<Session> sessionList;
    OnDeleteListener listener;

    NavController navController;
    Bundle bundle;


    public SessionListAdapter(NavController navControlle, Context context, List<Session> sessionList) {
        this.sessionList = sessionList;
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
        view = LayoutInflater.from(context).inflate(R.layout.session_item_layout, parent, false);

        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleViewHolder holder, final int position) {


        try {

            if (position < sessionList.size()) {
                holder.itemView.setVisibility(View.VISIBLE);
                holder.day_name_txt.setText(sessionList.get(position).getSessionDate());
                holder.time_from_txt.setText(sessionList.get(position).getScheduledStart());
                holder.time_to_txt.setText(sessionList.get(position).getScheduledEnd());

                holder.optionMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        PopupMenu popup = new PopupMenu(context, holder.optionMenu);
                        popup.inflate(R.menu.code_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.editCode:
                                        bundle.putSerializable(ARG_CODE, (Serializable) sessionList.get(position));
                                        bundle.putSerializable("type", (Serializable) EnumCode.UsageType.Code);
                                        bundle.putSerializable("codeList", (Serializable) sessionList);
                                        navController.navigate(R.id.codeAddFragment, bundle);

                                        break;
                                    case R.id.deleteCode:
                                        //handle delete click
                                        if (listener != null)
                                            listener.onDeleteButtonClicked(sessionList.get(position));

                                        break;
                                }
                                return false;
                            }
                        });
                        //displaying the popup
                        popup.show();
                    }
                });
            } else if (position == sessionList.size()) {
                holder.itemView.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    @Override
    public int getItemCount() {

        return sessionList.size() + 1;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView optionMenu, day_name_txt, time_from_txt, time_to_txt;
        ImageView doctorImg;


        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            optionMenu = itemView.findViewById(R.id.optionMenu);
            day_name_txt = itemView.findViewById(R.id.day_name_txt);
            time_from_txt = itemView.findViewById(R.id.time_from_txt);
            time_to_txt = itemView.findViewById(R.id.time_to_txt);
            doctorImg = itemView.findViewById(R.id.doctorImg);

        }
    }
}
