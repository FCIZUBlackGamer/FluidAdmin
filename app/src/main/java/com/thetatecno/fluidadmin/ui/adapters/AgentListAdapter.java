package com.thetatecno.fluidadmin.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.listeners.OnDeleteListener;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.ui.addorupdatestuff.AddOrUpdateAgentFragment;

import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

public class AgentListAdapter extends RecyclerView.Adapter<AgentListAdapter.AgentListViewHolder> {
    Context context;
    FragmentManager fragmentManager;
    OnDeleteListener listener;
    List<Staff> agentList;

    public AgentListAdapter(Context context, @Nullable List<Staff> agentList, FragmentManager fragmentManager) {
        this.agentList = agentList;
        this.context = context;
        this.fragmentManager = fragmentManager;
        if (context instanceof OnDeleteListener)
            listener = (OnDeleteListener) context;
        else
            listener = null;

    }

    @NonNull
    @Override
    public AgentListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(context));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );
        view = LayoutInflater.from(context).inflate(R.layout.agent_list_item, parent, false);
        return new AgentListAdapter.AgentListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AgentListViewHolder holder, final int position) {

        holder.fullNameTxt.setText(agentList.get(position).getFirstName() + " " + agentList.get(position).getFamilyName());
        holder.agentEmailTxt.setText(agentList.get(position).getEmail());
        holder.agentPhoneTxt.setText(agentList.get(position).getMobileNumber());
        Glide.with(context).load(agentList.get(position).getImageLink()).into(holder.personImg);
        holder.facilityTitleTxt.setText(R.string.clinics);
        NameAdapter nameAdapter = new NameAdapter(context, agentList.get(position).getFacilityList(), holder.pager);
        holder.pager.setAdapter(nameAdapter);

        new TabLayoutMediator(holder.tabLayout, holder.pager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                    }
                }).attach();


        holder.agentTextViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //will show popup agents_menu here
                //creating a popup agents_menu
                PopupMenu popup = new PopupMenu(context, holder.agentTextViewOptions);
                //inflating agents_menu from xml resource
                popup.inflate(R.menu.agents_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.linkToFacility:
                                //TODO: show dialog with facilities
                                break;
                            case R.id.edit:
                                //handle edit click
                                fragmentManager.beginTransaction()
                                        .replace(R.id.nav_host_fragment, AddOrUpdateAgentFragment.newInstance(agentList.get(position)))
                                        .commit();
                                break;
                            case R.id.delete:
                                //show confirmation dialog to delete item
                                if (listener != null)
                                    listener.onDeleteButtonClicked(agentList.get(position));
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
        return agentList.size();
    }

    class AgentListViewHolder extends RecyclerView.ViewHolder {

        ImageView personImg;
        TextView agentTextViewOptions, fullNameTxt, agentEmailTxt, agentPhoneTxt;
        ViewPager2 pager;
        TabLayout tabLayout;
        TextView facilityTitleTxt;

        public AgentListViewHolder(@NonNull View itemView) {
            super(itemView);
            personImg = itemView.findViewById(R.id.person_img);
            agentTextViewOptions = itemView.findViewById(R.id.agentTextViewOptions);
            fullNameTxt = itemView.findViewById(R.id.fullNameTxt);
            agentEmailTxt = itemView.findViewById(R.id.email_txt);
            agentPhoneTxt = itemView.findViewById(R.id.mobile_num_txt);
            pager = itemView.findViewById(R.id.photo_viewpager);
            tabLayout = itemView.findViewById(R.id.tab_layout);
            facilityTitleTxt = itemView.findViewById(R.id.facilityTitle);
        }
    }
}
