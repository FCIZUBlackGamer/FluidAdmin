package com.thetatechno.fluidadmin.ui.timeSlotList;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;


import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnClickedCommunicator;
import com.thetatechno.fluidadmin.model.time_slot_model.TimeSlot;

import java.util.List;

public class timeSlotListAdapter extends BaseAdapter {
    private Context mContext;
    private List<TimeSlot> timeSlotList;
    private TextView textView;
    private OnClickedCommunicator onClickedCommunicator;

    // Constructor
    public timeSlotListAdapter(Context c, List<TimeSlot> timeSlotList) {
        this.mContext = c;
        this.timeSlotList = timeSlotList;
        onClickedCommunicator = (OnClickedCommunicator) mContext;
    }

    public int getCount() {
        return timeSlotList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = inflater.inflate(R.layout.date_item, null);

        } else {
            view = convertView;
        }
        textView = view.findViewById(R.id.textView);
        if (timeSlotList.get(position).isSelected()){
            textView.setBackground(mContext.getResources().getDrawable(R.drawable.date_available_style));
            textView = textView.findViewById(R.id.textView);
            textView.setTextColor(mContext.getColor(R.color.colorWhite));
        }
        else {
            textView.setBackground(mContext.getResources().getDrawable(R.drawable.date_not_available_style));
            textView.setTextColor(mContext.getColor(R.color.colorPrimary));
        }
        textView.setText(timeSlotList.get(position).getTime());

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickedCommunicator != null)
                  onClickedCommunicator.onClick(timeSlotList.get(position));

                    for(TimeSlot timeSlot : timeSlotList){
                        if(timeSlot.isSelected()){
                            timeSlot.setSelected(false);
                        }
                    }
                timeSlotList.get(position).setSelected(true);
                notifyDataSetChanged();

            }
        });

        return view;
    }
    public void updateList(List<TimeSlot> timeSlotList){

        this.timeSlotList = timeSlotList;
        this.notifyDataSetChanged();
    }

}
