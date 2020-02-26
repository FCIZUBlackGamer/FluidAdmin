package com.thetatecno.fluidadmin.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.listeners.OnDeleteListener;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.utils.EnumCode;

import java.io.Serializable;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatecno.fluidadmin.utils.Constants.ARG_FACILITY;

public class FacilityListAdapter extends RecyclerView.Adapter<FacilityListAdapter.vHolder> {
    Context context;
    FragmentManager fragmentManager;
    List<Facility> facilityList;
    OnDeleteListener listener;

    NavController navController;
    Bundle bundle;

    public FacilityListAdapter(NavController navControlle, Context context, List<Facility> facilityList, FragmentManager fragmentManager) {
        this.facilityList = facilityList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        bundle = new Bundle();
        navController = navControlle;
        if (context instanceof OnDeleteListener)
            listener = (OnDeleteListener) context;
        else
            listener = null;

    }

    @NonNull
    @Override
    public vHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(context));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );
        view = LayoutInflater.from(context).inflate(R.layout.facilty_detail_list_item, parent, false);

        return new vHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final vHolder holder, final int position) {

        try {
            holder.facilityId_tv.setText(facilityList.get(position).getCode());
//            holder.deviceID_tv.setText(context.getResources().getText(R.string.deviceId )+ " "+facilityList.get(position).getDeviceId());
            holder.typeCode_tv.setText(facilityList.get(position).getType());
            if(facilityList.get(position).getWaitingAreaID()!=null) {
                holder.waId_tv.setText(facilityList.get(position).getWaitingAreaID());
                holder.waId_tv.setVisibility(View.VISIBLE);
            }
            else {
                holder.waId_tv.setVisibility(View.GONE);
            }
            holder.desc_tv.setText(facilityList.get(position).getDescription());

            holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //will show popup agents_menu here
                    //creating a popup agents_menu
                    PopupMenu popup = new PopupMenu(context, holder.textViewOptions);
                    //inflating agents_menu from xml resource
                    popup.inflate(R.menu.default_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    //handle edit click
                                    bundle.putSerializable(ARG_FACILITY, (Serializable) facilityList.get(position));
                                    bundle.putSerializable("type", (Serializable) EnumCode.UsageType.Facility);
                                    bundle.putSerializable("facilityList", (Serializable) facilityList);

                                    navController.navigate(R.id.facilityAddFragment, bundle);

                                    break;

                                case R.id.delete:
                                    // show delete dialog
                                    if (listener != null)
                                        listener.onDeleteButtonClicked(facilityList.get(position));
                                    break;

                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });

        }catch (Exception e){
            Sentry.capture(e);
        }

    }
  
    @Override
    public int getItemCount() {
        Log.e("Size Count", facilityList.size()+"");
        return facilityList.size();
    }

    public class vHolder extends RecyclerView.ViewHolder {
        TextView textViewOptions, facilityId_tv, typeCode_tv, desc_tv;
        TextView waId_tv;

        public vHolder(@NonNull View itemView) {
            super(itemView);
            textViewOptions = itemView.findViewById(R.id.textViewOptions);
            facilityId_tv = itemView.findViewById(R.id.facilityId_tv);
            typeCode_tv = itemView.findViewById(R.id.typeCode_tv);
            desc_tv = itemView.findViewById(R.id.desc_tv);
            waId_tv = itemView.findViewById(R.id.waId_tv);
        }
    }
}
