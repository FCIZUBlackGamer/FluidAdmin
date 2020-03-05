package com.thetatechno.fluidadmin.ui.clientList;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.CustomerList;
import com.thetatechno.fluidadmin.model.Person;
import com.thetatechno.fluidadmin.ui.adapters.ClientListViewAdapter;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientListFragment extends Fragment {

    List<Person> clientList;
    RecyclerView clientListRecyclerView;
    ClientListViewAdapter clientListViewAdapter;
    ClientListViewModel clientListViewModel;
SwipeRefreshLayout clientSwipeLayout;
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
        clientListViewModel.getAllClients("",PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase()).observe(this, new Observer<CustomerList>() {
            @Override
            public void onChanged(CustomerList customerList) {
                if (customerList != null) {
                    if (customerList.getPersonList() != null) {
                        clientList = customerList.getPersonList();
                        clientListViewAdapter = new ClientListViewAdapter(getContext(),clientList,getActivity().getSupportFragmentManager());
                        clientListRecyclerView.setAdapter(clientListViewAdapter);
                        clientSwipeLayout.setRefreshing(false);
                    } else {
                        Log.e("Staff List", "Is Null");
                    }
                } else {
                    Log.e("Staff", "Is Null");
                }
            }
        });
        clientSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                clientSwipeLayout.setRefreshing(true);
            }
        });
    }
}
