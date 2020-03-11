package com.thetatechno.fluidadmin.ui.facilityList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.utils.EnumCode;

import java.io.Serializable;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_FACILITY;

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
            holder.facilityIdTxt.setText(facilityList.get(position).getCode());
//            holder.deviceID_tv.setText(context.getResources().getText(R.string.deviceId )+ " "+facilityList.get(position).getDeviceId());
            if(facilityList.get(position).getWaitingAreaID()!=null) {
                holder.waitingAreaIdTxt.setText(facilityList.get(position).getWaitingAreaID());
                holder.waitingAreaIdTxt.setVisibility(View.VISIBLE);

            }
            else {
                holder.waitingAreaIdTxt.setVisibility(View.GONE);
            }
            holder.facilityDescriptionTxt.setText(facilityList.get(position).getDescription());
            if(facilityList.get(position).getType().equals(EnumCode.ClinicTypeCode.CLINIC.toString())){
                holder.facilityImgView.setImageResource(R.drawable.ic_clinic);
            }
            else if (facilityList.get(position).getType().equals(EnumCode.ClinicTypeCode.WAITAREA.toString())){
                holder.facilityImgView.setImageResource(R.drawable.ic_chair);

            }
            holder.facilityTextViewOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //will show popup agents_menu here
                    //creating a popup agents_menu
                    PopupMenu popup = new PopupMenu(context, holder.facilityTextViewOptions);
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
        return facilityList.size();
    }

    public class vHolder extends RecyclerView.ViewHolder {
        TextView facilityTextViewOptions, facilityIdTxt, facilityDescriptionTxt;
        TextView waitingAreaIdTxt;
        ImageView facilityImgView;

        public vHolder(@NonNull View itemView) {
            super(itemView);
            facilityTextViewOptions = itemView.findViewById(R.id.facilityTextViewOptions);
            facilityIdTxt = itemView.findViewById(R.id.facilityIdTxt);
            facilityDescriptionTxt = itemView.findViewById(R.id.facilityDescriptionTxt);
            waitingAreaIdTxt = itemView.findViewById(R.id.waitingAreaIdTxt);
            facilityImgView = itemView.findViewById(R.id.facilityImgView);
        }
    }
}
