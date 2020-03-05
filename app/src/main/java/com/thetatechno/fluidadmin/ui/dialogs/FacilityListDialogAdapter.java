package com.thetatechno.fluidadmin.ui.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.Facility;

import java.util.List;

public class FacilityListDialogAdapter extends RecyclerView.Adapter<FacilityListDialogAdapter.myViewHolder> {
    Context mContext;
    List<Facility> facilityList;

    public FacilityListDialogAdapter(Context mContext, List<Facility> facilityList) {
        this.mContext = mContext;
        this.facilityList = facilityList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.add_to_facility_list_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder holder, final int position) {
        final Facility facility = facilityList.get(position);
        holder.FacilityNameTxt.setText(facility.getDescription());
        if (facility.isSelected())
            holder.facilityCheckBox.setChecked(true);
        else {
            holder.facilityCheckBox.setChecked(false);
        }
        holder.facilityCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facilityList.get(position).setSelected(holder.facilityCheckBox.isChecked());
                facility.setSelected(holder.facilityCheckBox.isChecked());
            }
        });

    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }

    public List<Facility> getFacilityList() {
        return facilityList;
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
