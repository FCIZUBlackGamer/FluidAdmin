package com.thetatechno.fluidadmin.ui.stafflist.agentList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.facility_model.Facility;

import java.util.List;

public class FacilitiesForAgentListAdapter extends RecyclerView.Adapter<FacilitiesForAgentListAdapter.ViewHolder> {

    private List<Facility> mData;
    private LayoutInflater mInflater;


    public FacilitiesForAgentListAdapter(Context context, List<Facility> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String facility = mData.get(position).getDescription();
        holder.myTextView.setText(facility);
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        else
            return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvTitle);


        }
    }

}