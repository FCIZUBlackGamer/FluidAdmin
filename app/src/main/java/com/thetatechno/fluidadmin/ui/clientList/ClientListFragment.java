package com.thetatechno.fluidadmin.ui.clientList;


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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.ClientData;
import com.thetatechno.fluidadmin.model.ClientListModel;
import com.thetatechno.fluidadmin.model.ClientModelForRegister;
import com.thetatechno.fluidadmin.model.Person;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;

import java.util.List;

public class ClientListFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private List<ClientModelForRegister> clientList;
    private RecyclerView clientListRecyclerView;
    private ClientListViewAdapter clientListViewAdapter;
    private ClientListViewModel clientListViewModel;
    private SwipeRefreshLayout clientSwipeLayout;
    private FloatingActionButton bookAppointmentBtn;
    private SearchView searchView;
    private NavController navController;
    private static final String TAG = ClientListFragment.class.getSimpleName();

    public ClientListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_client_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        clientListRecyclerView = view.findViewById(R.id.clientListView);
        clientListViewModel = ViewModelProviders.of(this).get(ClientListViewModel.class);
        bookAppointmentBtn = view.findViewById(R.id.bookNewAppointmentBtn);
        clientListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        clientSwipeLayout = view.findViewById(R.id.clientSwipeLayout);
        setHasOptionsMenu(true);
        clientListViewModel.getAllClients();
        clientListViewModel.getClientData().observe(getViewLifecycleOwner(), new Observer<ClientListModel>() {
            @Override
            public void onChanged(ClientListModel clientData) {
                EspressoTestingIdlingResource.increment();
                if (clientData != null) {
                    if (clientData.getClientData() != null) {
                        EspressoTestingIdlingResource.increment();
                        clientList = clientData.getClientData().getPersonList();
                        clientListViewAdapter = new ClientListViewAdapter(getContext(), clientList,navController);
                        clientListRecyclerView.setAdapter(clientListViewAdapter);
                        EspressoTestingIdlingResource.decrement();
                    } else {
                        Snackbar.make(clientSwipeLayout,clientData.getFailureErrorMessage(),Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                clientListViewModel.getAllClients();
                            }
                        }).setAnchorView(bookAppointmentBtn).show();
                    }

                } else {
                    Log.e(TAG, "no data returns");
                }
                EspressoTestingIdlingResource.decrement();

            }
        });


        clientSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clientSwipeLayout.setRefreshing(true);
                clientListViewModel.getAllClients();
                clientSwipeLayout.setRefreshing(false);
            }
        });
        bookAppointmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_clientList_to_registerPage3);
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
        clientListViewAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        clientListViewAdapter.getFilter().filter(newText);
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
