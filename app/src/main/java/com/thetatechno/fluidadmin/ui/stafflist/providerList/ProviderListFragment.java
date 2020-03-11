package com.thetatechno.fluidadmin.ui.stafflist.providerList;


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
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProviderListFragment extends Fragment {
   private List<Staff> providerList;
    private RecyclerView providerListRecyclerView;
    private FloatingActionButton addNewProviderFab;
    private ProviderListAdapter providerListAdapter;
    private StaffListViewModel providerListViewModel;
    private NavController navController;
    private SwipeRefreshLayout providerSwipeLayout;
    static final private String TAG = ProviderListFragment.class.getSimpleName();
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
        providerSwipeLayout = view.findViewById(R.id.providerSwipeLayout);
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
                        Log.e(TAG, "provider List Is Null");
                    }
                }else{
                    Log.e(TAG, "no data returns");
                }
            }
        });
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
}
