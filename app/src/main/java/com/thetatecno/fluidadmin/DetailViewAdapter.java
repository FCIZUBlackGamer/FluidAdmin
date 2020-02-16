package com.thetatecno.fluidadmin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.thetatecno.fluidadmin.model.Person;
import com.thetatecno.fluidadmin.model.Provider;
import com.thetatecno.fluidadmin.model.Staff;

import java.util.List;

enum UsageType {
    Agent,
    Provider,
    Facility,
    Person
}

public class DetailViewAdapter extends RecyclerView.Adapter<DetailViewAdapter.vHolder> {
    Context context;
    FragmentManager fragmentManager;
    List<Staff> agentList;
    List<Staff> providerList;
    List<Person> personList;
    UsageType usageType;


    DetailViewAdapter(Context context, UsageType usageType, @Nullable List<Staff> agentList,
                      @Nullable List<Staff> providerList,
                      @Nullable List<Person> personList, FragmentManager fragmentManager) {
        this.agentList = agentList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.providerList = providerList;
        this.personList = personList;
        this.usageType = usageType;
    }

    @NonNull
    @Override
    public vHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        usageType = checkWhoIsHere();
//        Log.e("State", usageType.toString());
        View view;
//        if (usageType == UsageType.Facility) {
//            view = LayoutInflater.from(context).inflate(R.layout.facilty_detail_list_item, parent, false);
//        } else {
//            view = LayoutInflater.from(context).inflate(R.layout.default_detail_list_item, parent, false);
//        }

        view = LayoutInflater.from(context).inflate(R.layout.default_detail_list_item, parent, false);
        return new vHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final vHolder holder, final int position) {

        if (usageType == UsageType.Agent) {
//            Log.e("State", "OJk");
            displayAgents(holder, position);
        } else if (usageType == UsageType.Provider) {
            displayProviders(holder, position);
        } else if (usageType == UsageType.Person) {
            displayPersons(holder, position);
        }

    }

    private void displayPersons(final vHolder holder, int position) {
        //region HideViews
        holder.tabLayout.setVisibility(View.GONE);
        holder.pager.setVisibility(View.GONE);
        holder.f_tv.setVisibility(View.GONE);
        //endregion

        holder.name_tv.setText(personList.get(position).getFirstName() + " " + personList.get(position).getFamilyName());
        holder.mail_tv.setText(personList.get(position).getEmail());
        holder.phone_tv.setText(personList.get(position).getMobileNumber());
        Glide.with(context).load(personList.get(position).getImageLink()).into(holder.personImg);

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
    }

    private void displayProviders(final vHolder holder, int position) {
        //region HideViews
        holder.tabLayout.setVisibility(View.GONE);
        holder.pager.setVisibility(View.GONE);
        holder.f_tv.setVisibility(View.VISIBLE);
        //endregion

        holder.name_tv.setText(providerList.get(position).getFirstName() + " " + providerList.get(position).getFamilyName());
        holder.mail_tv.setText(providerList.get(position).getEmail());
        holder.phone_tv.setText(providerList.get(position).getMobileNumber());
        try {
            Glide.with(context).load(providerList.get(position).getImageLink()).into(holder.personImg);
        }catch (Exception e){

        }
        holder.f_tv.setText(providerList.get(position).getSpeciality());

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
                            case R.id.add:
                                //handle add click
                                break;
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
    }

    private void displayAgents(final vHolder holder, int position) {

        //region ShowViews
        holder.tabLayout.setVisibility(View.VISIBLE);
        holder.pager.setVisibility(View.VISIBLE);
        holder.f_tv.setVisibility(View.VISIBLE);
        //endregion

        holder.name_tv.setText(agentList.get(position).getFirstName() + " " + agentList.get(position).getFamilyName());
        holder.mail_tv.setText(agentList.get(position).getEmail());
        holder.phone_tv.setText(agentList.get(position).getMobileNumber());
        Glide.with(context).load(agentList.get(position).getImageLink()).into(holder.personImg);
        holder.f_tv.setText(R.string.clinics);


//        Log.e("Size", agentList.get(position).getFacilityList().get(0).getDescription()+"");
        NameAdapter nameAdapter = new NameAdapter(context, agentList.get(position).getFacilityList(), holder.pager);
        holder.pager.setAdapter(nameAdapter);

        new TabLayoutMediator(holder.tabLayout, holder.pager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    }
                }).attach();


        holder.textViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //will show popup agents_menu here
                //creating a popup agents_menu
                PopupMenu popup = new PopupMenu(context, holder.textViewOptions);
                //inflating agents_menu from xml resource
                popup.inflate(R.menu.agents_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.add:
                                //handle add click
                                break;
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
    }

    @Override
    public int getItemCount() {
        if (usageType == UsageType.Agent) {
            return agentList.size();
        } else if (usageType == UsageType.Provider) {
            return providerList.size();
        } else if (usageType == UsageType.Person) {
            return personList.size();
        }
        return 0;
    }

    public class vHolder extends RecyclerView.ViewHolder {
        ImageView personImg;
        TextView textViewOptions, name_tv, mail_tv, phone_tv;
        ViewPager2 pager;
        PagerAdapter adapter;
        TabLayout tabLayout;
        AppCompatTextView tvTitle;
        TextView f_tv;

        public vHolder(@NonNull View itemView) {
            super(itemView);
            personImg = itemView.findViewById(R.id.person_img);
            textViewOptions = itemView.findViewById(R.id.textViewOptions);
            name_tv = itemView.findViewById(R.id.facilityId_tv);
            mail_tv = itemView.findViewById(R.id.deviceID_tv);
            phone_tv = itemView.findViewById(R.id.typeCode_tv);
            pager = itemView.findViewById(R.id.photo_viewpager);
            tabLayout = itemView.findViewById(R.id.tab_layout);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            f_tv = itemView.findViewById(R.id.desc_tv);
        }
    }
}
