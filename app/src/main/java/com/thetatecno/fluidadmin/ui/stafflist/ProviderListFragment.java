package com.thetatecno.fluidadmin.ui.stafflist;


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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.model.StaffData;
import com.thetatecno.fluidadmin.ui.adapters.ProviderListAdapter;
import com.thetatecno.fluidadmin.utils.App;
import com.thetatecno.fluidadmin.utils.Constants;
import com.thetatecno.fluidadmin.utils.EnumCode;
import com.thetatecno.fluidadmin.utils.PreferenceController;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProviderListFragment extends Fragment {
    List<Staff> providerList;
    RecyclerView providerListRecyclerView;
    FloatingActionButton addNewProviderFab;
    ProviderListAdapter providerListAdapter;
    StaffListViewModel providerListViewModel;
    NavController navController;

    public ProviderListFragment() {
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
        providerListRecyclerView = view.findViewById(R.id.providerRecyclerView);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        addNewProviderFab = view.findViewById(R.id.fab);
        providerListViewModel = ViewModelProviders.of(this).get(StaffListViewModel.class);
        providerListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        providerListViewModel.getStaffData(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase(), EnumCode.StaffTypeCode.PRVDR.toString()).observe(this, new Observer<StaffData>() {
            @Override
            public void onChanged(StaffData staffData) {
                if (staffData != null) {
                    if (staffData.getStaffList() != null) {
                        providerList = staffData.getStaffList();
                        providerListAdapter = new ProviderListAdapter(navController, getContext(), providerList, getActivity().getSupportFragmentManager());
                        providerListRecyclerView.setAdapter(providerListAdapter);
                    } else {
                        Log.e("Staff List", "Is Null");
                    }
                } else {
                    Log.e("Staff", "Is Null");
                }
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
}
