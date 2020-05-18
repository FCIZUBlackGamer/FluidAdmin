package com.thetatechno.fluidadmin.ui.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;

import java.util.ArrayList;
import java.util.List;

public class FacilityListDialogAdapter extends RecyclerView.Adapter<FacilityListDialogAdapter.myViewHolder> implements Filterable {
    Context mContext;
    List<Facility> facilityList;
    List<Facility> filteredFacilityList;

    public FacilityListDialogAdapter(Context mContext, List<Facility> facilityList) {
        this.mContext = mContext;
        this.facilityList = facilityList;
        this.filteredFacilityList = facilityList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.add_to_facility_list_item, parent, false);
        return new myViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        final Facility facility = filteredFacilityList.get(position);
        holder.FacilityNameTxt.setText(facility.getDescription());
        if (facility.isSelected())
            holder.facilityCheckBox.setChecked(true);
        else {
            holder.facilityCheckBox.setChecked(false);
        }
        holder.facilityCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filteredFacilityList.get(position).setSelected(holder.facilityCheckBox.isChecked());
                facility.setSelected(holder.facilityCheckBox.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return filteredFacilityList.size();
    }

    public List<Facility> getFacilityList() {
        return facilityList;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                EspressoTestingIdlingResource.increment();
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredFacilityList= facilityList;
                } else {
                    List<Facility> filteredList = new ArrayList<>();
                    for (Facility facility : facilityList) {
                        if (facility.getDescription().contains(charSequenceString)) {
                            filteredList.add(facility);
                        }
                        filteredFacilityList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredFacilityList;
                EspressoTestingIdlingResource.decrement();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredFacilityList = (List<Facility>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        CheckBox facilityCheckBox;
        TextView FacilityNameTxt;

        public myViewHolder(View itemView) {
            super(itemView);
            FacilityNameTxt = itemView.findViewById(R.id.FacilityNameTxt);
            facilityCheckBox = itemView.findViewById(R.id.facilityCheckBox);

        }


    }
}
