package com.thetatechno.fluidadmin.ui.facilityList;


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
import com.thetatechno.fluidadmin.model.Facilities;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.utils.EnumCode;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacilityListFragment extends Fragment {
    RecyclerView facilityListClinicRecyclerView;
    FacilityListAdapter facilityListAdapter;
    List<Facility> facilityList;
    FacilityListViewModel facilityListViewModel;
    NavController navController;
    FloatingActionButton addNewFacilityFab;
    SwipeRefreshLayout facilitySwipeLayout;
    private  final String ARG_CLINIC_TYPE = "clinic_type";
    final String TAG = FacilityListFragment.class.getSimpleName();

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
        addNewFacilityFab = view.findViewById(R.id.fab);
        facilitySwipeLayout = view.findViewById(R.id.facilitySwipeLayout);
        facilityListClinicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        facilityListViewModel = ViewModelProviders.of(this).get(FacilityListViewModel.class);
        facilityListViewModel.getAllFacilities("").observe(this, new Observer<Facilities>() {
            @Override
            public void onChanged(Facilities facilities) {
                if (facilities != null) {
                    if (facilities.getFacilities() != null) {
                        facilityList = facilities.getFacilities();
                        facilityListAdapter = new FacilityListAdapter(navController, getContext(), facilityList, getActivity().getSupportFragmentManager());
                        facilityListClinicRecyclerView.setAdapter(facilityListAdapter);

                    } else {
                        Log.e(TAG, "facilityList Is Null");
                    }
                }else{
                        Log.e(TAG, "no data returns");
                    }
                }

        });
        addNewFacilityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                            bundle.putSerializable(ARG_CLINIC_TYPE, (Serializable) EnumCode.ClinicTypeCode.CLINIC);

                            navController.navigate(R.id.action_facilityListFragment_to_facilityAddFragment, bundle);
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
}
