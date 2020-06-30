package com.thetatechno.fluidadmin.ui.stafflist.providerList;


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
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProviderList extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {
    private List<Staff> providerList;
    private RecyclerView providerListRecyclerView;
    private FloatingActionButton addNewProviderFab;
    private ProviderListAdapter providerListAdapter;
    private StaffListViewModel providerListViewModel;
    private NavController navController;
    private SwipeRefreshLayout providerSwipeLayout;
    private SearchView searchView;
    private ProgressBar loadProvidersProgressBar;
    static final private String TAG = ProviderList.class.getSimpleName();

    public ProviderList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_provider_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        providerListRecyclerView = view.findViewById(R.id.providerRecyclerView);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        providerSwipeLayout = view.findViewById(R.id.providerSwipeLayout);
        addNewProviderFab = view.findViewById(R.id.addProviderFab);
        loadProvidersProgressBar = view.findViewById(R.id.loadProvidersProgressBar);
        providerListViewModel = ViewModelProviders.of(this).get(StaffListViewModel.class);
        providerListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        EspressoTestingIdlingResource.increment();
        loadProvidersProgressBar.setVisibility(View.VISIBLE);
        providerListViewModel.getStaffData(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.PRVDR.toString()).observe(getViewLifecycleOwner(), new Observer<StaffListModel>() {
            @Override
            public void onChanged(StaffListModel staffData) {
                loadProvidersProgressBar.setVisibility(View.GONE);
                if (staffData.getStaffData() != null) {
                        EspressoTestingIdlingResource.increment();
                        providerList = staffData.getStaffData().getStaffList();
                        providerListAdapter = new ProviderListAdapter(navController, getContext(), providerList, getActivity().getSupportFragmentManager());
                        providerListRecyclerView.setAdapter(providerListAdapter);
                        EspressoTestingIdlingResource.decrement();

                } else {
                    Toast.makeText(getContext(),staffData.getErrorMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
        EspressoTestingIdlingResource.decrement();

        providerSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                providerSwipeLayout.setRefreshing(true);
                providerListViewModel.getStaffData(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.PRVDR.toString());
                providerSwipeLayout.setRefreshing(false);

            }
        });

        addNewProviderFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.ARG_STAFF, null);
                navController.navigate(R.id.action_providerListFragment_to_addOrUpdateProviderFragment, bundle);
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
        providerListAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        providerListAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.home, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
