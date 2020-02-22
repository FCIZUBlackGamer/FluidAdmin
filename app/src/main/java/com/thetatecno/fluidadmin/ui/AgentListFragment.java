package com.thetatecno.fluidadmin.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thetatecno.fluidadmin.AgentListViewModel;
import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.model.StaffData;
import com.thetatecno.fluidadmin.ui.adapters.AgentListAdapter;
import com.thetatecno.fluidadmin.utils.App;
import com.thetatecno.fluidadmin.utils.EnumCode;
import com.thetatecno.fluidadmin.utils.PreferenceController;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgentListFragment extends Fragment {

    public AgentListFragment() {
        // Required empty public constructor
    }

    RecyclerView agentListRecyclerView;
    AgentListAdapter agentListAdapter;
    AgentListViewModel agentListViewModel;
    public static AgentListFragment newInstance() {

        Bundle args = new Bundle();

        AgentListFragment fragment = new AgentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agent_list, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        agentListViewModel = ViewModelProviders.of(this).get(AgentListViewModel.class);
        agentListViewModel.getStaffDataForAgents(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE),EnumCode.StaffTypeCode.DSPTCHR.toString()).observe(this, new Observer<StaffData>() {
            @Override
            public void onChanged(StaffData staffData) {

            }
        });

    }
}
