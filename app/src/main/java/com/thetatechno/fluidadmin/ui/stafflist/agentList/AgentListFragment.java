package com.thetatechno.fluidadmin.ui.stafflist.agentList;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.model.StaffData;
import com.thetatechno.fluidadmin.ui.stafflist.StaffListViewModel;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgentListFragment extends Fragment {
    NavController navController;
    List<Staff> agentList;
    final String TAG = AgentListFragment.class.getSimpleName();
    RecyclerView agentListRecyclerView;
    FloatingActionButton addNewAgentFab;
    AgentListAdapter agentListAdapter;
    StaffListViewModel staffListViewModel;
    SwipeRefreshLayout agentSwipeLayout;

    public AgentListFragment() {
        // Required empty public constructor
    }

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
        Log.i(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        agentListRecyclerView = view.findViewById(R.id.agentsListRecyclerView);
        addNewAgentFab = view.findViewById(R.id.fab);
        staffListViewModel = ViewModelProviders.of(this).get(StaffListViewModel.class);
        agentSwipeLayout = view.findViewById(R.id.agentSwipeLayout);
        agentListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if (agentList == null) {
            staffListViewModel.getStaffData(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.DSPTCHR.toString()).observe(this, new Observer<StaffData>() {
                @Override
                public void onChanged(StaffData staffData) {
                    if (staffData != null) {
                        if (staffData.getStaffList() != null) {
                            agentList = staffData.getStaffList();
                            agentListAdapter = new AgentListAdapter(navController, getContext(), agentList, getActivity().getSupportFragmentManager());
                            agentListRecyclerView.setAdapter(agentListAdapter);
                        } else {
                            Log.e(TAG, "agentList Is Null");
                        }
                    }else{
                        Log.e(TAG, "no data returns");
                    }
                }
            });
        } else {
            agentListAdapter = new AgentListAdapter(navController, getContext(), agentList, getActivity().getSupportFragmentManager());
            agentListRecyclerView.setAdapter(agentListAdapter);

        }
        agentSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                agentSwipeLayout.setRefreshing(true);
                staffListViewModel.getStaffData(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.DSPTCHR.toString());
                agentSwipeLayout.setRefreshing(false);

            }
        });
        addNewAgentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_agentListFragment_to_addOrUpdateAgentFragment);

            }
        });

    }
}
