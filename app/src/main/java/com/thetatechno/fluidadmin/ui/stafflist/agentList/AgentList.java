package com.thetatechno.fluidadmin.ui.stafflist.agentList;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.staff_model.Staff;
import com.thetatechno.fluidadmin.model.staff_model.StaffData;
import com.thetatechno.fluidadmin.model.staff_model.StaffListModel;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.ui.stafflist.StaffListViewModel;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AgentList extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener   {
    private NavController navController;
    private List<Staff> agentList;
    private final String TAG = AgentList.class.getSimpleName();
    private RecyclerView agentListRecyclerView;
    private FloatingActionButton addNewAgentFab;
    private AgentListAdapter agentListAdapter;
    private StaffListViewModel staffListViewModel;
    private SwipeRefreshLayout agentSwipeLayout;
    private ProgressBar loadAgentsProgressBar;

    public AgentList() {
        // Required empty public constructor
    }

    public static AgentList newInstance() {
        AgentList fragment = new AgentList();
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
        addNewAgentFab = view.findViewById(R.id.addAgentFab);
        staffListViewModel = ViewModelProviders.of(this).get(StaffListViewModel.class);
        agentSwipeLayout = view.findViewById(R.id.agentSwipeLayout);
        loadAgentsProgressBar = view.findViewById(R.id.loadAgentsProgressBar);
        setHasOptionsMenu(true);
        agentListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if (agentList == null) {
           EspressoTestingIdlingResource.increment();
            loadAgentsProgressBar.setVisibility(View.VISIBLE);
            staffListViewModel.getStaffData(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.DSPTCHR.toString()).observe(getViewLifecycleOwner(), new Observer<StaffListModel>() {
                @Override
                public void onChanged(StaffListModel staffData) {
                    loadAgentsProgressBar.setVisibility(View.GONE);

                    if (staffData.getStaffData() != null) {
                            EspressoTestingIdlingResource.increment();
                            agentList = staffData.getStaffData().getStaffList();
                            agentListAdapter = new AgentListAdapter(navController, getContext(), agentList, getActivity().getSupportFragmentManager());
                            agentListRecyclerView.setAdapter(agentListAdapter);
                            EspressoTestingIdlingResource.decrement();
                        } else {
                            Toast.makeText(getContext(),staffData.getErrorMessage(),Toast.LENGTH_SHORT).show();
                        }

                }
            });
            EspressoTestingIdlingResource.decrement();

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
                navController.navigate(R.id.action_agentList_to_addOrUpdateAgentFragment);

            }
        });

    }
    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {

        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        agentListAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        agentListAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.home, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
