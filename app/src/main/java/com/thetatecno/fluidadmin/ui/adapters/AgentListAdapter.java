package com.thetatecno.fluidadmin.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.listeners.OnDeleteListener;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.ui.addorupdatestuff.AddOrUpdateAgentFragment;

import java.io.Serializable;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatecno.fluidadmin.utils.Constants.ARG_CODE;
import static com.thetatecno.fluidadmin.utils.Constants.ARG_STAFF;

public class AgentListAdapter extends RecyclerView.Adapter<AgentListAdapter.AgentListViewHolder> {
    Context context;
    FragmentManager fragmentManager;
    OnDeleteListener listener;
    List<Staff> agentList;
    NavController navController;
    Bundle bundle;

    public AgentListAdapter(NavController navControlle, Context context, @Nullable List<Staff> agentList, FragmentManager fragmentManager) {
        this.agentList = agentList;
        this.context = context;
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
        holder.idTxt.setText(agentList.get(position).getStaffId());
        if (!agentList.get(position).getFirstName().isEmpty() || !agentList.get(position).getFamilyName().isEmpty()) {
            holder.fullNameTxt.setText(agentList.get(position).getFirstName() + " " + agentList.get(position).getFamilyName());
            holder.fullNameTxt.setVisibility(View.VISIBLE);
        } else {
            holder.fullNameTxt.setVisibility(View.GONE);
        }
        if (!agentList.get(position).getEmail().isEmpty()) {
            holder.agentEmailTxt.setText(agentList.get(position).getEmail());
            holder.agentEmailTxt.setVisibility(View.VISIBLE);

        } else
            holder.agentEmailTxt.setVisibility(View.GONE);

        if (!agentList.get(position).getMobileNumber().isEmpty()) {
            holder.agentPhoneTxt.setText(agentList.get(position).getMobileNumber());
            holder.agentPhoneTxt.setVisibility(View.VISIBLE);
        } else {
            holder.agentPhoneTxt.setVisibility(View.GONE);
        }
        Glide.with(context).load(agentList.get(position).getImageLink()).into(holder.personImg);
        holder.facilityTitleTxt.setText(R.string.clinics);
        FacilitiesForAgentListAdapter facilitiesForAgentListAdapter = new FacilitiesForAgentListAdapter(context, agentList.get(position).getFacilityList());
        holder.pager.setAdapter(facilitiesForAgentListAdapter);

        holder.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.previousBtn.setVisibility(View.VISIBLE);
                if (holder.pager.getCurrentItem() < agentList.get(position).getFacilityList().size()) {
                    holder.pager.setCurrentItem(holder.pager.getCurrentItem() + 1);
                    if (holder.pager.getCurrentItem() == agentList.get(position).getFacilityList().size()-1){
                        holder.nextBtn.setVisibility(View.GONE);
                    }
                }
            }
        });

        holder.previousBtn.setVisibility(View.GONE);

        holder.previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.nextBtn.setVisibility(View.VISIBLE);
                if (holder.pager.getCurrentItem() > 0) {
                    holder.pager.setCurrentItem(holder.pager.getCurrentItem() - 1);
                    if (holder.pager.getCurrentItem() == 0) {
                        holder.previousBtn.setVisibility(View.GONE);
                    }
                }
            }
        });

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
                                bundle.putSerializable(ARG_STAFF, (Serializable) agentList.get(position));
                                bundle.putSerializable("agentList", (Serializable) agentList);
                                navController.navigate(R.id.addOrUpdateAgentFragment, bundle);
//                                fragmentManager.beginTransaction()
//                                        .replace(R.id.nav_host_fragment, AddOrUpdateAgentFragment.newInstance(agentList.get(position)))
//                                        .commit();
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
        TextView idTxt;
        ViewPager2 pager;
        TextView facilityTitleTxt;
        Button previousBtn, nextBtn;

        public AgentListViewHolder(@NonNull View itemView) {
            super(itemView);
            personImg = itemView.findViewById(R.id.person_img);
            agentTextViewOptions = itemView.findViewById(R.id.agentTextViewOptions);
            fullNameTxt = itemView.findViewById(R.id.fullNameTxt);
            idTxt = itemView.findViewById(R.id.idTxt);
            agentEmailTxt = itemView.findViewById(R.id.email_txt);
            agentPhoneTxt = itemView.findViewById(R.id.mobile_num_txt);
            pager = itemView.findViewById(R.id.photo_viewpager);
            previousBtn = itemView.findViewById(R.id.previous_btn);
            nextBtn = itemView.findViewById(R.id.next_btn);
            facilityTitleTxt = itemView.findViewById(R.id.facilityTitle);
        }
    }
}
