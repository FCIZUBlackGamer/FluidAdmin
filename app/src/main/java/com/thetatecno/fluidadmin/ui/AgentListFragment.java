package com.thetatecno.fluidadmin.ui;


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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.model.StaffData;
import com.thetatecno.fluidadmin.ui.adapters.AgentListAdapter;
import com.thetatecno.fluidadmin.utils.App;
import com.thetatecno.fluidadmin.utils.EnumCode;
import com.thetatecno.fluidadmin.utils.PreferenceController;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgentListFragment extends Fragment {
    NavController navController;
    List<Staff> agentList ;
    final String TAG = AgentListFragment.class.getSimpleName();
    public AgentListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"onCreate");
    }

    RecyclerView agentListRecyclerView;
    AgentListAdapter agentListAdapter;
    StaffListViewModel staffListViewModel;
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
    public void onPause() {
        super.onPause();
        Log.i(TAG,"onPause");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG,"onStart");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"onStop");

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(TAG,"onSaveInstanceState");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,"onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        agentListRecyclerView = view.findViewById(R.id.agentsListRecyclerView);
        staffListViewModel = ViewModelProviders.of(this).get(StaffListViewModel.class);
        agentListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        staffListViewModel.getStaffData(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(),EnumCode.StaffTypeCode.DSPTCHR.toString()).observe(this, new Observer<StaffData>() {
            @Override
            public void onChanged(StaffData staffData) {
                if (staffData != null) {
                    if (staffData.getStaffList() != null) {
                      agentList = staffData.getStaffList();
                      agentListAdapter = new AgentListAdapter(navController,getContext(),agentList,getActivity().getSupportFragmentManager());
                      agentListRecyclerView.setAdapter(agentListAdapter);
                    } else {
                        Log.e("Staff List", "Is Null");
                    }
                } else {
                    Log.e("Staff", "Is Null");
                }
            }
        });

    }
}
