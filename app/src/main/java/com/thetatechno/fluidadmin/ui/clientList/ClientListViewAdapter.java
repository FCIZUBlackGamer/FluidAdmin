package com.thetatechno.fluidadmin.ui.clientList;

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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.model.ClientModelForRegister;
import com.thetatechno.fluidadmin.model.Person;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;


public class ClientListViewAdapter extends RecyclerView.Adapter<ClientListViewAdapter.vHolder> implements Filterable {
    private static final String ARG_CLIENT_ID = "clientId";
    private Context context;
    private List<ClientModelForRegister> personList;
    private List<ClientModelForRegister> filteredClientList;
    private OnDeleteListener listener;
    private NavController navController;


    public ClientListViewAdapter(Context context, @Nullable List<ClientModelForRegister> personList, NavController navController) {
        Gson gson = new Gson();
        Log.e("Images", gson.toJson(personList));

        this.context = context;
        this.personList = personList;
        this.filteredClientList = personList;
        if (context instanceof OnDeleteListener)
            listener = (OnDeleteListener) context;
        else
            listener = null;
        this.navController = navController;
    }

    @NonNull
    @Override
    public vHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(context));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );
        view = LayoutInflater.from(context).inflate(R.layout.client_list_item, parent, false);
        return new vHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final vHolder holder, final int position) {

        try {
            if (!filteredClientList.get(position).getClientId().isEmpty()) {
                holder.idTxt.setText(filteredClientList.get(position).getClientId());
                holder.idTxt.setVisibility(View.VISIBLE);
            } else {

                holder.idTxt.setVisibility(View.GONE);
            }
            if (!filteredClientList.get(position).getFirstName().isEmpty() || !filteredClientList.get(position).getFamilyName().isEmpty()) {
                holder.fullNameTxt.setText(filteredClientList.get(position).getFirstName() + " " + filteredClientList.get(position).getFamilyName());
                holder.fullNameTxt.setVisibility(View.VISIBLE);
            } else {
                holder.fullNameTxt.setVisibility(View.GONE);
            }
            if (!filteredClientList.get(position).getEmail().isEmpty()) {
                holder.mailTxt.setText(filteredClientList.get(position).getEmail());
                holder.mailTxt.setVisibility(View.VISIBLE);
            } else {
                holder.mailTxt.setVisibility(View.GONE);
            }
            if (!filteredClientList.get(position).getMobile().isEmpty()) {
                holder.phoneTxt.setText(filteredClientList.get(position).getMobile());
                holder.phoneTxt.setVisibility(View.VISIBLE);
            } else {

                holder.phoneTxt.setVisibility(View.GONE);
            }

            if (!filteredClientList.get(position).getImageFile().isEmpty()) {
                Glide.with(context).load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + filteredClientList.get(position).getImageFile())
                        .circleCrop()
                        .placeholder(R.drawable.man)
                        .into(holder.personImg);
            } else {
                if (!filteredClientList.get(position).getSexCode().isEmpty()) {
                    if (filteredClientList.get(position).getSexCode().equals(EnumCode.Gender.M.toString())) {
                        holder.personImg.setImageResource(R.drawable.man);
                    } else if (filteredClientList.get(position).getSexCode().equals(EnumCode.Gender.F.toString())) {
                        holder.personImg.setImageResource(R.drawable.ic_girl);
                    }
                } else {
                    holder.personImg.setImageResource(R.drawable.man);
                }
            }

            holder.clientOptionMenuTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, holder.clientOptionMenuTxt);
                    //inflating agents_menu from xml resource
                    popup.inflate(R.menu.client_menu);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {

                                case R.id.bookAppointment:
                                    //handle edit click
                                    Bundle bundle = new Bundle();
                                    bundle.putString(ARG_CLIENT_ID, filteredClientList.get(position).getClientId());
                                    navController.navigate(R.id.action_clientList_to_selectSpecialityAndProviderAndDisplayCalender, bundle);
                                    break;
                                case R.id.deleteBranch:
                                    //handle delete click
                                    if (listener != null)
                                        listener.onDeleteButtonClicked(filteredClientList.get(position));
                                    break;
                            }
                            return false;
                        }
                    });
                    popup.show();
                }
            });
        } catch (Exception e) {
            Sentry.capture(e);
        }


    }


    @Override
    public int getItemCount() {
        if (filteredClientList != null && filteredClientList.size() > 0)
            return filteredClientList.size();
        else return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredClientList = personList;
                } else {
                    List<ClientModelForRegister> filteredList = new ArrayList<>();
                    for (ClientModelForRegister person : personList) {
                        if (StringUtil.isSearchResultExist(person.getFirstName(),charSequenceString) ||StringUtil.isSearchResultExist(person.getFamilyName(),charSequenceString) ) {
                            filteredList.add(person);
                        }
                        filteredClientList = filteredList;
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredClientList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredClientList = (List<ClientModelForRegister>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public class vHolder extends RecyclerView.ViewHolder {
        ImageView personImg;
        TextView fullNameTxt, mailTxt, phoneTxt;
        TextView idTxt, clientOptionMenuTxt;


        public vHolder(@NonNull View itemView) {
            super(itemView);
            personImg = itemView.findViewById(R.id.clientImg);
            fullNameTxt = itemView.findViewById(R.id.clientFullNameTxt);
            mailTxt = itemView.findViewById(R.id.client_email_txt);
            phoneTxt = itemView.findViewById(R.id.client_phone_txt);
            idTxt = itemView.findViewById(R.id.clientIdTxt);
            clientOptionMenuTxt = itemView.findViewById(R.id.clientTxtViewOptions);

        }
    }
}
