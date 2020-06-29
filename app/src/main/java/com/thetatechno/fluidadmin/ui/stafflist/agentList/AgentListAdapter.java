package com.thetatechno.fluidadmin.ui.stafflist.agentList;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.listeners.OnLinkToFacilityClickedListener;
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;

import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_STAFF;

public class AgentListAdapter extends RecyclerView.Adapter<AgentListAdapter.AgentListViewHolder> implements Filterable {
    private Context context;
    private OnDeleteListener listener;
    private OnLinkToFacilityClickedListener onLinkToFacilityClickedListener;
    private List<Staff> agentList;
    private List<Staff> filteredAgentList;
    private NavController navController;
    private Bundle bundle;

    public AgentListAdapter(NavController navControlle, Context context, @Nullable List<Staff> agentList, FragmentManager fragmentManager) {
        this.agentList = agentList;
        this.filteredAgentList = agentList;
        this.context = context;
        navController = navControlle;
        bundle = new Bundle();
        if (context instanceof OnDeleteListener) {
            listener = (OnDeleteListener) context;
            onLinkToFacilityClickedListener = (OnLinkToFacilityClickedListener) context;
        } else {
            listener = null;
            onLinkToFacilityClickedListener = null;
        }

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final AgentListViewHolder holder, final int position) {
        if(position < filteredAgentList.size()) {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.idTxt.setText(filteredAgentList.get(position).getStaffId());
            if (!filteredAgentList.get(position).getFirstName().isEmpty() || !filteredAgentList.get(position).getFamilyName().isEmpty()) {
                holder.fullNameTxt.setText(filteredAgentList.get(position).getFirstName() + " " + filteredAgentList.get(position).getFamilyName());
                holder.fullNameTxt.setVisibility(View.VISIBLE);
            } else {
                holder.fullNameTxt.setVisibility(View.GONE);
            }
            if (!filteredAgentList.get(position).getEmail().isEmpty()) {
                holder.agentEmailTxt.setText(agentList.get(position).getEmail().toLowerCase());
                holder.agentEmailTxt.setVisibility(View.VISIBLE);

            } else
                holder.agentEmailTxt.setVisibility(View.GONE);

            if (!filteredAgentList.get(position).getMobileNumber().isEmpty()) {
                holder.agentPhoneTxt.setText(filteredAgentList.get(position).getMobileNumber());
                holder.agentPhoneTxt.setVisibility(View.VISIBLE);
            } else {
                holder.agentPhoneTxt.setVisibility(View.GONE);
            }
            if (!filteredAgentList.get(position).getImageLink().isEmpty()) {
                Glide.with(context).load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + filteredAgentList.get(position).getImageLink())
                        .circleCrop()
                        .placeholder(R.drawable.man)
                        .into(holder.personImg);
            } else {
                if (!filteredAgentList.get(position).getGender().isEmpty()) {
                    if (filteredAgentList.get(position).getGender().equals(EnumCode.Gender.M.toString())) {
                        holder.personImg.setImageResource(R.drawable.man);
                    } else if (filteredAgentList.get(position).getGender().equals(EnumCode.Gender.F.toString())) {
                        holder.personImg.setImageResource(R.drawable.ic_girl);
                    }
                } else {
                    holder.personImg.setImageResource(R.drawable.man);
                }
            }
            if (filteredAgentList.get(position).getFacilityList() != null) {
                FacilitiesForAgentListAdapter facilitiesForAgentListAdapter = new FacilitiesForAgentListAdapter(context, filteredAgentList.get(position).getFacilityList());
                holder.pager.setAdapter(facilitiesForAgentListAdapter);
                holder.nextBtn.setVisibility(View.VISIBLE);
                holder.pager.setVisibility(View.VISIBLE);
            } else {

                holder.nextBtn.setVisibility(View.INVISIBLE);
                holder.pager.setVisibility(View.INVISIBLE);

            }


            holder.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageScrolled(int vposition, float positionOffset, int positionOffsetPixels) {
                    super.onPageScrolled(vposition, positionOffset, positionOffsetPixels);
                }

                @Override
                public void onPageSelected(int vposition) {
                    super.onPageSelected(vposition);

                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    super.onPageScrollStateChanged(state);
                    Log.e("position", agentList.get(position).toString());
                    if (holder.pager.getCurrentItem() == agentList.get(position).getFacilityList().size() - 1) {
                        holder.nextBtn.setVisibility(View.GONE);
                        holder.previousBtn.setVisibility(View.VISIBLE);
                    } else if (holder.pager.getCurrentItem() > 0 && holder.pager.getCurrentItem() < agentList.get(position).getFacilityList().size()) {
                        holder.previousBtn.setVisibility(View.VISIBLE);
                        holder.nextBtn.setVisibility(View.VISIBLE);

                    } else if (holder.pager.getCurrentItem() == 0) {
                        holder.previousBtn.setVisibility(View.GONE);
                        holder.nextBtn.setVisibility(View.VISIBLE);
                    }
                }
            });
            holder.nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.previousBtn.setVisibility(View.VISIBLE);
                    if (holder.pager.getCurrentItem() < agentList.get(position).getFacilityList().size()) {
                        holder.pager.setCurrentItem(holder.pager.getCurrentItem() + 1);
                        if (holder.pager.getCurrentItem() == agentList.get(position).getFacilityList().size() - 1) {
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
                                    if (onLinkToFacilityClickedListener != null) {
                                        EspressoTestingIdlingResource.increment();
                                        onLinkToFacilityClickedListener.onShowDialogLinkToFacility(filteredAgentList.get(position));

                                    }

                                    break;
                                case R.id.editAgent:
                                    //handle edit click
                                    bundle.putSerializable(ARG_STAFF, filteredAgentList.get(position));
                                    navController.navigate(R.id.addOrUpdateAgentFragment, bundle);

                                    break;
                                case R.id.deleteAgent:
                                    //show confirmation dialog to delete item
                                    if (listener != null)
                                        listener.onDeleteButtonClicked(filteredAgentList.get(position));
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
        else if(position == filteredAgentList.size()){
            holder.itemView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return filteredAgentList.size()+1;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredAgentList= agentList;
                } else {
                    List<Staff> filteredList = new ArrayList<>();
                    for (Staff agent : agentList) {
                        if (agent.getFirstName().contains(charSequenceString) || agent.getFamilyName().contains(charSequenceString) || agent.getFirstName().equalsIgnoreCase(charSequenceString) || agent.getFamilyName().equalsIgnoreCase(charSequenceString)) {
                            filteredList.add(agent);
                        }
                        filteredAgentList = filteredList;
                    }

                }
                FilterResults results = new FilterResults();
                results.values = filteredAgentList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredAgentList = (List<Staff>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class AgentListViewHolder extends RecyclerView.ViewHolder {

        ImageView personImg;
        TextView agentTextViewOptions, fullNameTxt, agentEmailTxt, agentPhoneTxt;
        TextView idTxt;
        ViewPager2 pager;
        ImageButton previousBtn, nextBtn;

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
        }
    }
}
