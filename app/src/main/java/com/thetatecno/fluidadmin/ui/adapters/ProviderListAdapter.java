package com.thetatecno.fluidadmin.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.listeners.OnDeleteListener;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.ui.addorupdatestuff.AddOrUpdateProviderFragment;
import com.thetatecno.fluidadmin.utils.EnumCode;

import java.io.Serializable;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatecno.fluidadmin.utils.Constants.ARG_CODE;
import static com.thetatecno.fluidadmin.utils.Constants.ARG_FACILITY;
import static com.thetatecno.fluidadmin.utils.Constants.ARG_STAFF;

public class ProviderListAdapter extends RecyclerView.Adapter<ProviderListAdapter.ProviderListViewHolder> {

    Context context;
    FragmentManager fragmentManager;
    OnDeleteListener listener;
    List<Staff> providerList;
    NavController navController;
    Bundle bundle;

    public ProviderListAdapter(NavController navControlle, Context context, List<Staff> providerList, FragmentManager fragmentManager) {
        this.context = context;
        this.providerList = providerList;
        navController = navControlle;
        bundle = new Bundle();
        this.fragmentManager = fragmentManager;
        if (context instanceof OnDeleteListener)
            listener = (OnDeleteListener) context;
        else
            listener = null;

    }

    @NonNull
    @Override
    public ProviderListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(context));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );
        view = LayoutInflater.from(context).inflate(R.layout.provider_item_list, parent, false);
        return new ProviderListAdapter.ProviderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProviderListViewHolder holder, final int position) {

        if (!providerList.get(position).getFirstName().isEmpty() || !providerList.get(position).getFamilyName().isEmpty()) {
            holder.fullNameTxt.setText(providerList.get(position).getFirstName() + " " + providerList.get(position).getFamilyName());
            holder.fullNameTxt.setVisibility(View.VISIBLE);
        } else {
            holder.fullNameTxt.setVisibility(View.GONE);

        }
        if (!providerList.get(position).getEmail().isEmpty()) {
            holder.mailTxt.setText(providerList.get(position).getEmail());
            holder.mailTxt.setVisibility(View.VISIBLE);
        } else {
            holder.mailTxt.setVisibility(View.GONE);

        }
        if (!providerList.get(position).getMobileNumber().isEmpty()) {

            holder.phoneTxt.setText(providerList.get(position).getMobileNumber());
            holder.phoneTxt.setVisibility(View.VISIBLE);

        } else {
            holder.phoneTxt.setVisibility(View.GONE);

        }
        if (providerList.get(position).getImageLink() != null && !providerList.get(position).getImageLink().isEmpty()) {

            Glide.with(context).load(providerList.get(position).getImageLink()).into(holder.personImg);
        }
        else{
            if(!providerList.get(position).getGender().isEmpty()) {
                if (providerList.get(position).getGender().equals(EnumCode.Gender.M.toString())) {
                    holder.personImg.setImageResource(R.drawable.man);
                } else if(providerList.get(position).getGender().equals(EnumCode.Gender.F.toString())){
                    holder.personImg.setImageResource(R.drawable.ic_girl);
                }
            }
            else {
                holder.personImg.setImageResource(R.drawable.man);
            }
        }

        holder.specialityTxt.setText(providerList.get(position).getSpeciality());

        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //will show popup agents_menu here
                //creating a popup agents_menu
                PopupMenu popup = new PopupMenu(context, holder.textViewOptions);
                //inflating agents_menu from xml resource
                popup.inflate(R.menu.providers_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.edit:
                                //handle edit click
                                bundle.putSerializable(ARG_STAFF, (Serializable) providerList.get(position));
                                bundle.putSerializable("providerList", (Serializable) providerList);
                                navController.navigate(R.id.addOrUpdateProviderFragment, bundle);

                                break;
                            case R.id.delete:
                                //handle delete click
                                if (listener != null)
                                    listener.onDeleteButtonClicked(providerList.get(position));
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return providerList.size();
    }

    class ProviderListViewHolder extends RecyclerView.ViewHolder {
        ImageView personImg;
        TextView textViewOptions, fullNameTxt, mailTxt, phoneTxt;
        TextView specialityTxt;

        public ProviderListViewHolder(@NonNull View itemView) {
            super(itemView);
            personImg = itemView.findViewById(R.id.person_img);
            textViewOptions = itemView.findViewById(R.id.providerTxtViewOption);
            fullNameTxt = itemView.findViewById(R.id.fullNameTxt);
            mailTxt = itemView.findViewById(R.id.email_txt);
            phoneTxt = itemView.findViewById(R.id.mobile_num_txt);
            specialityTxt = itemView.findViewById(R.id.speciality_txt);
        }
    }
}
