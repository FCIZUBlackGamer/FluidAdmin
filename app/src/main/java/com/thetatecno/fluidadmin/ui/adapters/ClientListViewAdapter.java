package com.thetatecno.fluidadmin.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.listeners.OnDeleteListener;
import com.thetatecno.fluidadmin.model.Person;
import com.thetatecno.fluidadmin.utils.EnumCode;

import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;


public class ClientListViewAdapter extends RecyclerView.Adapter<ClientListViewAdapter.vHolder> {
    Context context;
    FragmentManager fragmentManager;
    List<Person> personList;
    OnDeleteListener listener;


    public ClientListViewAdapter(Context context, @Nullable List<Person> personList, FragmentManager fragmentManager) {

        this.context = context;
        this.fragmentManager = fragmentManager;
        this.personList = personList;
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
        view = LayoutInflater.from(context).inflate(R.layout.client_list_item, parent, false);
        return new vHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final vHolder holder, final int position) {

        try {
            if(!personList.get(position).getId().isEmpty()) {
                holder.idTxt.setText(personList.get(position).getId());
                holder.idTxt.setVisibility(View.VISIBLE);
            }
            else {

                holder.idTxt.setVisibility(View.GONE);
            }
            if(!personList.get(position).getFirstName().isEmpty() || ! personList.get(position).getFamilyName().isEmpty()) {
                holder.fullNameTxt.setText(personList.get(position).getFirstName() + " " + personList.get(position).getFamilyName());
                holder.fullNameTxt.setVisibility(View.VISIBLE);
            }
            else {
                holder.fullNameTxt.setVisibility(View.GONE);
            }
            if(!personList.get(position).getEmail().isEmpty()) {
                holder.mailTxt.setText(personList.get(position).getEmail());
                holder.mailTxt.setVisibility(View.VISIBLE);
            }
            else {
                holder.mailTxt.setVisibility(View.GONE);
            }
            if(!personList.get(position).getMobileNumber().isEmpty()) {
                holder.phoneTxt.setText(personList.get(position).getMobileNumber());
                holder.phoneTxt.setVisibility(View.VISIBLE);
            }
            else {
                holder.phoneTxt.setVisibility(View.GONE);
            }

            if (!personList.get(position).getImageLink().isEmpty())
                Glide.with(context).load(personList.get(position).getImageLink()).into(holder.personImg);
            else{
                if(!personList.get(position).getGender().isEmpty()) {
                    if (personList.get(position).getGender().equals(EnumCode.Gender.M.toString())) {
                        holder.personImg.setImageResource(R.drawable.man);
                    } else if(personList.get(position).getGender().equals(EnumCode.Gender.F.toString())){
                        holder.personImg.setImageResource(R.drawable.ic_girl);
                    }
                }
                else {
                    holder.personImg.setImageResource(R.drawable.man);
                }
            }
        } catch (Exception e) {
            Sentry.capture(e);
        }


    }


    @Override
    public int getItemCount() {
        if(personList!=null && personList.size()>0)
        return personList.size();
        else return 0;
    }

    public class vHolder extends RecyclerView.ViewHolder {
        ImageView personImg;
        TextView fullNameTxt, mailTxt, phoneTxt;
        TextView idTxt;


        public vHolder(@NonNull View itemView) {
            super(itemView);
            personImg = itemView.findViewById(R.id.clientImg);
            fullNameTxt = itemView.findViewById(R.id.fullNameTxt);
            mailTxt = itemView.findViewById(R.id.email_txt);
            phoneTxt = itemView.findViewById(R.id.mobile_num_txt);
            idTxt = itemView.findViewById(R.id.idTxt);

        }
    }
}
