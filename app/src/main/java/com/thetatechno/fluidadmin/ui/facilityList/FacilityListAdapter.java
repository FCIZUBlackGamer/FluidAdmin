package com.thetatechno.fluidadmin.ui.facilityList;

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
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.utils.EnumCode;

import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_FACILITY;

public class FacilityListAdapter extends RecyclerView.Adapter<FacilityListAdapter.vHolder> implements Filterable {
    private Context context;
    private List<Facility> facilityList;
    private List<Facility> filteredFacilityList;

    OnDeleteListener listener;
    NavController navController;
    Bundle bundle;
    public FacilityListAdapter(NavController navController, Context context, List<Facility> facilityList, FragmentManager fragmentManager) {
        this.facilityList = facilityList;
        this.filteredFacilityList = facilityList;
        this.context = context;
        bundle = new Bundle();
        this.navController = navController;
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

      if(position < filteredFacilityList.size())
        try {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.facilityIdTxt.setText(filteredFacilityList.get(position).getId());
            if(filteredFacilityList.get(position).getType().equals(EnumCode.ClinicTypeCode.CLINIC.toString()) && filteredFacilityList.get(position).getWaitingAreaId()!=null) {
                holder.waitingAreaOrDeviceDescriptionTxt.setText(filteredFacilityList.get(position).getWaitingAreaDescription());
                holder.waitingAreaOrDeviceDescriptionTxt.setVisibility(View.VISIBLE);

            }
            else if(filteredFacilityList.get(position).getType().equals(EnumCode.ClinicTypeCode.WAITAREA.toString()) && filteredFacilityList.get(position).getDeviceId()!=null)
            {
                holder.waitingAreaOrDeviceDescriptionTxt.setText(filteredFacilityList.get(position).getDeviceDescription());
                holder.waitingAreaOrDeviceDescriptionTxt.setVisibility(View.VISIBLE);
            }
            else {
                holder.waitingAreaOrDeviceDescriptionTxt.setVisibility(View.GONE);
            }
            holder.facilityTextViewOptions.setText(context.getResources().getString(R.string.options_menu));
            holder.facilityDescriptionTxt.setText(filteredFacilityList.get(position).getDescription());
            if(filteredFacilityList.get(position).getType().equals(EnumCode.ClinicTypeCode.CLINIC.toString())){
                holder.facilityImgView.setImageResource(R.drawable.ic_clinic);
            }
            else if (filteredFacilityList.get(position).getType().equals(EnumCode.ClinicTypeCode.WAITAREA.toString())){
                holder.facilityImgView.setImageResource(R.drawable.ic_chair);

            }

            holder.branchTxt.setText(filteredFacilityList.get(position).getSiteDescription());
            holder.facilityTextViewOptions.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //will show popup agents_menu here
                    //creating a popup agents_menu
                    PopupMenu popup = new PopupMenu(context, holder.facilityTextViewOptions);
                    //inflating agents_menu from xml resource
                    popup.inflate(R.menu.facility_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.editFacility:
                                    //handle edit click
                                    bundle.putSerializable(ARG_FACILITY, filteredFacilityList.get(position));
                                    navController.navigate(R.id.facilityAddFragment, bundle);

                                    break;

                                case R.id.deleteFacility:
                                    // show delete dialog
                                    if (listener != null)
                                        listener.onDeleteButtonClicked(filteredFacilityList.get(position));
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

      else if(position== filteredFacilityList.size()) {

          holder.itemView.setVisibility(View.INVISIBLE);

      }
    }

  
    @Override
    public int getItemCount() {
        return filteredFacilityList.size()+1;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredFacilityList= facilityList;
                } else {
                    List<Facility> filteredList = new ArrayList<>();
                    for (Facility facility : facilityList) {
                        if (facility.getDescription().contains(charSequenceString) || facility.getDescription().equalsIgnoreCase(charSequenceString)) {
                            filteredList.add(facility);
                        }
                        filteredFacilityList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredFacilityList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredFacilityList = (List<Facility>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class vHolder extends RecyclerView.ViewHolder {
        TextView facilityTextViewOptions, facilityIdTxt, facilityDescriptionTxt,branchTxt;
        TextView waitingAreaOrDeviceDescriptionTxt;
        ImageView facilityImgView;

        public vHolder(@NonNull View itemView) {
            super(itemView);
            facilityTextViewOptions = itemView.findViewById(R.id.facilityTextViewOptions);
            facilityIdTxt = itemView.findViewById(R.id.facilityIdTxt);
            facilityDescriptionTxt = itemView.findViewById(R.id.facilityDescriptionTxt);
            waitingAreaOrDeviceDescriptionTxt = itemView.findViewById(R.id.waitingAreaOrDeviceDescriptionTxt);
            facilityImgView = itemView.findViewById(R.id.facilityImgView);
            branchTxt = itemView.findViewById(R.id.branchTxt);
        }
    }
}
