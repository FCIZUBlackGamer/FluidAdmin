package com.thetatechno.fluidadmin.ui.stafflist.providerList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_STAFF;

public class ProviderListAdapter extends RecyclerView.Adapter<ProviderListAdapter.ProviderListViewHolder>  implements Filterable {

    private Context context;
    private FragmentManager fragmentManager;
    private  OnDeleteListener listener;
    private  List<Staff> providerList;
    private NavController navController;
    private  Bundle bundle;
    private  List<Staff> filteredProviderList;

    public ProviderListAdapter(NavController navController, Context context, List<Staff> providerList, FragmentManager fragmentManager) {
        this.context = context;
        this.providerList = providerList;
        this.filteredProviderList = providerList;
        Gson gson = new Gson();
        Log.e("Images", gson.toJson(providerList));
        this.navController = navController;
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
if(position <filteredProviderList.size()) {
    holder.itemView.setVisibility(View.VISIBLE);

    if (!filteredProviderList.get(position).getFirstName().isEmpty() || !filteredProviderList.get(position).getFamilyName().isEmpty()) {
        holder.fullNameTxt.setText(filteredProviderList.get(position).getFirstName() + " " + filteredProviderList.get(position).getFamilyName());
        holder.fullNameTxt.setVisibility(View.VISIBLE);
    } else {
        holder.fullNameTxt.setVisibility(View.GONE);

    }
    holder.providerIdTxt.setText(filteredProviderList.get(position).getStaffId());
    if (!filteredProviderList.get(position).getEmail().isEmpty()) {
        holder.mailTxt.setText(filteredProviderList.get(position).getEmail());
        holder.mailTxt.setVisibility(View.VISIBLE);
    } else {
        holder.mailTxt.setVisibility(View.GONE);

    }
    if (!filteredProviderList.get(position).getMobileNumber().isEmpty()) {

        holder.phoneTxt.setText(filteredProviderList.get(position).getMobileNumber());
        holder.phoneTxt.setVisibility(View.VISIBLE);

    } else {
        holder.phoneTxt.setVisibility(View.GONE);

    }
    if (filteredProviderList.get(position).getImageLink() != null && !filteredProviderList.get(position).getImageLink().isEmpty()) {

        Glide.with(context).load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + filteredProviderList.get(position).getImageLink())
                .circleCrop()
                .into(holder.personImg);
    } else {
        if (!filteredProviderList.get(position).getGender().isEmpty()) {
            if (filteredProviderList.get(position).getGender().equals(EnumCode.Gender.M.toString())) {
                holder.personImg.setImageResource(R.drawable.man);
            } else if (filteredProviderList.get(position).getGender().equals(EnumCode.Gender.F.toString())) {
                holder.personImg.setImageResource(R.drawable.ic_girl);
            }
        } else {
            holder.personImg.setImageResource(R.drawable.man);
        }
    }

    holder.specialityTxt.setText(filteredProviderList.get(position).getSpeciality());

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

                        case R.id.editProvider:
                            //handle edit click
                            bundle.putSerializable(ARG_STAFF, (Serializable) filteredProviderList.get(position));
                            navController.navigate(R.id.addOrUpdateProviderFragment, bundle);
                            break;
                        case R.id.deleteProvider:
                            //handle delete click
                            if (listener != null)
                                listener.onDeleteButtonClicked(filteredProviderList.get(position));
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
else if(position == filteredProviderList.size()){
    holder.itemView.setVisibility(View.INVISIBLE);
}
    }

    @Override
    public int getItemCount() {
        return filteredProviderList.size()+1;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredProviderList= providerList;
                } else {
                    List<Staff> filteredList = new ArrayList<>();
                    for (Staff provider : providerList) {
                        if (provider.getFirstName().contains(charSequenceString) || provider.getFamilyName().contains(charSequenceString) || provider.getFirstName().equalsIgnoreCase(charSequenceString) || provider.getFamilyName().equalsIgnoreCase(charSequenceString)) {
                            filteredList.add(provider);
                        }
                        filteredProviderList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredProviderList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredProviderList = (List<Staff>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class ProviderListViewHolder extends RecyclerView.ViewHolder {
        ImageView personImg;
        TextView textViewOptions, fullNameTxt, mailTxt, phoneTxt;
        TextView specialityTxt;
        TextView providerIdTxt;

        public ProviderListViewHolder(@NonNull View itemView) {
            super(itemView);
            personImg = itemView.findViewById(R.id.person_img);
            textViewOptions = itemView.findViewById(R.id.providerTxtViewOption);
            fullNameTxt = itemView.findViewById(R.id.fullNameTxt);
            mailTxt = itemView.findViewById(R.id.email_txt);
            phoneTxt = itemView.findViewById(R.id.mobile_num_txt);
            specialityTxt = itemView.findViewById(R.id.speciality_txt);
            providerIdTxt = itemView.findViewById(R.id.providerId);
        }
    }
}
