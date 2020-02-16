package com.thetatecno.fluidadmin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thetatecno.fluidadmin.model.Facility;

import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

public class FacilityDetailViewAdapter extends RecyclerView.Adapter<FacilityDetailViewAdapter.vHolder> {
    Context context;
    FragmentManager fragmentManager;
    List<Facility> facilityList;


    FacilityDetailViewAdapter(Context context, List<Facility> facilityList, FragmentManager fragmentManager) {
        this.facilityList = facilityList;
        this.context = context;
        this.fragmentManager = fragmentManager;
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
            holder.facilityId_tv.setText(context.getResources().getText(R.string.facilty_id) + " "+facilityList.get(position).getCode());
            holder.deviceID_tv.setText(context.getResources().getText(R.string.deviceId )+ " "+facilityList.get(position).getDeviceId());
            holder.typeCode_tv.setText(context.getResources().getText(R.string.type) + " "+facilityList.get(position).getType());
            holder.waId_tv.setText(context.getResources().getText(R.string.waitingAreaID) + " "+facilityList.get(position).getWaitingAreaID());
            holder.desc_tv.setText(context.getResources().getText(R.string.decsription) + "\n"+facilityList.get(position).getDescription());

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
                                    break;
                                case R.id.delete:
                                    //handle delete click
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
        TextView textViewOptions, facilityId_tv, deviceID_tv, typeCode_tv, desc_tv;
        TextView waId_tv;

        public vHolder(@NonNull View itemView) {
            super(itemView);
            textViewOptions = itemView.findViewById(R.id.textViewOptions);
            facilityId_tv = itemView.findViewById(R.id.facilityId_tv);
            deviceID_tv = itemView.findViewById(R.id.deviceID_tv);
            typeCode_tv = itemView.findViewById(R.id.typeCode_tv);
            desc_tv = itemView.findViewById(R.id.desc_tv);
            waId_tv = itemView.findViewById(R.id.waId_tv);
        }
    }
}
