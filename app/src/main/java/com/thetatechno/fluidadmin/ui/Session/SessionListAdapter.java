package com.thetatechno.fluidadmin.ui.Session;

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
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_CODE;
import static com.thetatechno.fluidadmin.utils.Constants.ARG_SESSION;
import static com.thetatechno.fluidadmin.utils.Constants.ARG_STAFF;

public class SessionListAdapter extends RecyclerView.Adapter<SessionListAdapter.ScheduleViewHolder> implements Filterable {

    Context context;
    List<Session> sessionList;
    List<Session> filteredsessionList;
    OnDeleteListener listener;
    NavController navController;
    Bundle bundle;

    public SessionListAdapter(NavController navControlle, Context context, List<Session> sessionList) {
        this.sessionList = sessionList;
        this.filteredsessionList = sessionList;
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
//        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(context));
//        Sentry.getContext().setUser(
//                new UserBuilder().setUsername("theta").build()
//        );
        view = LayoutInflater.from(context).inflate(R.layout.session_item_layout, parent, false);

        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScheduleViewHolder holder, final int position) {


        try {

            if (position < filteredsessionList.size()) {
                holder.itemView.setVisibility(View.VISIBLE);
                holder.day_name_txt.setText(filteredsessionList.get(position).getSessionDate());
                holder.time_from_txt.setText(filteredsessionList.get(position).getScheduledStart());
                holder.time_to_txt.setText(filteredsessionList.get(position).getScheduledEnd());
                holder.facilityNameTxtView.setText(filteredsessionList.get(position).getFacilitDescription());
                holder.providerNameTxtView.setText(filteredsessionList.get(position).getProviderName());
                holder.optionMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popup = new PopupMenu(context, holder.optionMenu);
                        popup.inflate(R.menu.session_menu);
                        popup.setOnMenuItemClickListener((PopupMenu.OnMenuItemClickListener) item -> {
                            switch (item.getItemId()) {
                                case R.id.editSession:
                                    bundle.putSerializable(ARG_SESSION, (Serializable) filteredsessionList.get(position));
                                    navController.navigate(R.id.action_sessionFragment_to_fragmentAddOrUpdateSesssion, bundle);
                                    break;
                                case R.id.deleteSession:
                                    if (listener != null)
                                        listener.onDeleteButtonClicked(filteredsessionList.get(position));

                                    break;
                            }
                            return false;
                        });
                        popup.show();
                    }
                });
            } else if (position == filteredsessionList.size()) {
                holder.itemView.setVisibility(View.INVISIBLE);
            }

        } catch (Exception e) {
            e.printStackTrace();
//            Sentry.capture(e);
        }

    }

    @Override
    public int getItemCount() {

        return filteredsessionList.size() + 1;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredsessionList= sessionList;
                } else {
                    List<Session> filteredList = new ArrayList<>();
                    for (Session session : sessionList) {
                        if (StringUtil.isSearchResultExist(session.getProviderName(),charSequenceString) || StringUtil.isSearchResultExist(session.getFacilitDescription(),charSequenceString) ) {
                            filteredList.add(session);
                        }
                        filteredsessionList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredsessionList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredsessionList = (List<Session>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {
        TextView optionMenu, day_name_txt, time_from_txt, time_to_txt;
        TextView providerNameTxtView, facilityNameTxtView;
        ImageView doctorImg;


        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            optionMenu = itemView.findViewById(R.id.optionMenu);
            day_name_txt = itemView.findViewById(R.id.day_name_txt);
            time_from_txt = itemView.findViewById(R.id.time_from_txt);
            time_to_txt = itemView.findViewById(R.id.time_to_txt);
            doctorImg = itemView.findViewById(R.id.doctorImg);
            providerNameTxtView = itemView.findViewById(R.id.provider_name_txt_view);
            facilityNameTxtView = itemView.findViewById(R.id.FacilityNameTxt);

        }
    }
}
