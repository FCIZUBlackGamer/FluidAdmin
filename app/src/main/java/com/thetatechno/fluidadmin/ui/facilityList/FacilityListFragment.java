package com.thetatechno.fluidadmin.ui.facilityList;


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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.facility_model.FacilitiesResponse;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.facility_model.FacilityListModel;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacilityListFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener  {
    private RecyclerView facilityListClinicRecyclerView;
    private FacilityListAdapter facilityListAdapter;
    private List<Facility> facilityList;
    private FacilityListViewModel facilityListViewModel;
    private NavController navController;
    private FloatingActionButton addNewFacilityFab;
    private SwipeRefreshLayout facilitySwipeLayout;

    private final String ARG_CLINIC_TYPE = "clinic_type";
    final String TAG = FacilityListFragment.class.getSimpleName();
private ProgressBar loadFacilityProgressBar ;
    public FacilityListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facility_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        facilityListClinicRecyclerView = view.findViewById(R.id.facilityListClinicRecyclerView);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        addNewFacilityFab = view.findViewById(R.id.addNewFacilityFab);
        facilitySwipeLayout = view.findViewById(R.id.facilitySwipeLayout);
        loadFacilityProgressBar = view.findViewById(R.id.loadFacilityProgressBar);
        facilityListClinicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        facilityListViewModel = ViewModelProviders.of(this).get(FacilityListViewModel.class);
        setHasOptionsMenu(true);
        EspressoTestingIdlingResource.increment();
        loadFacilityProgressBar.setVisibility(View.VISIBLE);
        facilityListViewModel.getAllFacilities("").observe(getViewLifecycleOwner(), new Observer<FacilityListModel>() {
            @Override
            public void onChanged(FacilityListModel model) {
                EspressoTestingIdlingResource.increment();
                loadFacilityProgressBar.setVisibility(View.GONE);

                if (model.getResponse() != null) {
                    if (model.getResponse().getFacilities() != null) {

                        facilityList = model.getResponse().getFacilities();
                        facilityListAdapter = new FacilityListAdapter(navController, getContext(), facilityList, getActivity().getSupportFragmentManager());
                        facilityListClinicRecyclerView.setAdapter(facilityListAdapter);


                    } else if (model.getResponse().getStatus() != null) {
                        Snackbar.make(facilitySwipeLayout, model.getResponse().getStatus(), Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                facilityListViewModel.getAllFacilities("");
                            }
                        }).setAnchorView(addNewFacilityFab).show();
                    }
                } else {
                    Snackbar.make(facilitySwipeLayout, model.getErrorMessage(), Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            facilityListViewModel.getAllFacilities("");
                        }
                    }).setAnchorView(addNewFacilityFab).show();
                }
                EspressoTestingIdlingResource.decrement();
            }

        });
        EspressoTestingIdlingResource.decrement();
        addNewFacilityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EspressoTestingIdlingResource.increment();
                navController.navigate(R.id.action_facilityListFragment_to_facilityAddFragment);
                EspressoTestingIdlingResource.decrement();
            }
        });
        facilitySwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                facilitySwipeLayout.setRefreshing(true);
                facilityListViewModel.getAllFacilities("");
                facilitySwipeLayout.setRefreshing(false);
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
        facilityListAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        facilityListAdapter.getFilter().filter(newText);
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
