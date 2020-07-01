package com.thetatechno.fluidadmin.ui.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.shedule.ScheduleResponse;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment  implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private View view;
    private RecyclerView recyclerView;
    private ScheduleListAdapter scheduleListAdapter;
    private ScheduleListViewModel scheduleListViewModel;
    private NavController navController;
    private FloatingActionButton floatingActionButton;
    private ProgressBar loadScheduleProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.schedule_list_layout, container, false);
        scheduleListViewModel = ViewModelProviders.of(this).get(ScheduleListViewModel.class);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        recyclerView = view.findViewById(R.id.rec);
        loadScheduleProgressBar = view.findViewById(R.id.loadScheduleProgressBar);
        swipeRefreshLayout = view.findViewById(R.id.scheduleSwipeRefreshLayout);
        setHasOptionsMenu(true);
        floatingActionButton = view.findViewById(R.id.fab);
        loadScheduleProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        scheduleListViewModel.getAllSchedules().observe(getViewLifecycleOwner(), new Observer<ScheduleResponse>() {
            @Override
            public void onChanged(ScheduleResponse scheduleResponse) {
                loadScheduleProgressBar.setVisibility(View.GONE);
                if (scheduleResponse != null) {
                    if (scheduleResponse.getError().getErrorCode() == 0) {
                        if (scheduleResponse.getSchedules() != null) {
                            scheduleListAdapter = new ScheduleListAdapter(navController, getContext(), scheduleResponse.getSchedules());
                            recyclerView.setAdapter(scheduleListAdapter);
                        } else {
                            Toast.makeText(getContext(), scheduleResponse.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), scheduleResponse.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(),"", Toast.LENGTH_SHORT).show();

                }


            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                scheduleListViewModel.getAllSchedules();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        floatingActionButton.setOnClickListener(v -> {
            navController.navigate(R.id.action_scheduleFragment_to_fragmentAddOrUpdateSchedule);
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
        scheduleListAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        scheduleListAdapter.getFilter().filter(newText);
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
