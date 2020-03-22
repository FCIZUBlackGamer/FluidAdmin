package com.thetatechno.fluidadmin.ui.clientList;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.CustomerList;
import com.thetatechno.fluidadmin.model.Person;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientListFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    List<Person> clientList;
    RecyclerView clientListRecyclerView;
    ClientListViewAdapter clientListViewAdapter;
    ClientListViewModel clientListViewModel;
SwipeRefreshLayout clientSwipeLayout;
SearchView searchView;
static final String TAG = ClientListFragment.class.getSimpleName();
    public ClientListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clientListRecyclerView = view.findViewById(R.id.clientListView);
        clientListViewModel = ViewModelProviders.of(this).get(ClientListViewModel.class);
        clientListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        clientSwipeLayout = view.findViewById(R.id.clientSwipeLayout);
        setHasOptionsMenu(true);
        EspressoTestingIdlingResource.increment();
        clientListViewModel.getAllClients("").observe(this, new Observer<CustomerList>() {
            @Override
            public void onChanged(CustomerList customerList) {
                if (customerList != null) {

                    if (customerList.getPersonList() != null) {
                        EspressoTestingIdlingResource.increment();
                        clientList = customerList.getPersonList();
                        clientListViewAdapter = new ClientListViewAdapter(getContext(),clientList,getActivity().getSupportFragmentManager());
                        clientListRecyclerView.setAdapter(clientListViewAdapter);
                        clientSwipeLayout.setRefreshing(false);
                        EspressoTestingIdlingResource.decrement();
                    } else {
                        Log.e(TAG, "clientList Is Null");
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
                clientListViewModel.getAllClients("");
                clientSwipeLayout.setRefreshing(false);
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

        inflater.inflate(R.menu.home,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
