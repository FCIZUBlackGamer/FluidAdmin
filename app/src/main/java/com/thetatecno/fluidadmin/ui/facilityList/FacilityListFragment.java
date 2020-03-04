package com.thetatecno.fluidadmin.ui.facilityList;


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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.model.Facilities;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.ui.adapters.FacilityListAdapter;
import com.thetatecno.fluidadmin.utils.EnumCode;
import com.thetatecno.fluidadmin.utils.PreferenceController;

import java.io.Serializable;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacilityListClinicTypeFragment extends Fragment {
    RecyclerView facilityListClinicRecyclerView;
    FacilityListAdapter facilityListAdapter;
    List<Facility> facilityList;
    FacilityListViewModel facilityListViewModel;
    NavController navController;
    FloatingActionButton addNewFacilityFab;
    private  final String ARG_CLINIC_TYPE = "clinic_type";

    public FacilityListClinicTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facility_list_clinic_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        facilityListClinicRecyclerView = view.findViewById(R.id.facilityListClinicRecyclerView);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        addNewFacilityFab = view.findViewById(R.id.fab);
        facilityListClinicRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        facilityListViewModel = ViewModelProviders.of(this).get(FacilityListViewModel.class);
        facilityListViewModel.getFacilityDataForClinics("",
                PreferenceController.getInstance(getContext()).get(PreferenceController.LANGUAGE).toUpperCase()).observe(this, new Observer<Facilities>() {
            @Override
            public void onChanged(Facilities facilities) {
                if (facilities != null) {
                    if (facilities.getFacilities() != null) {
                        facilityList = facilities.getFacilities();
                        facilityListAdapter = new FacilityListAdapter(navController,getContext(),facilityList,getActivity().getSupportFragmentManager());
                        facilityListClinicRecyclerView.setAdapter(facilityListAdapter);
                    }
                }
            }
        });
        addNewFacilityFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                            bundle.putSerializable(ARG_CLINIC_TYPE, (Serializable) EnumCode.ClinicTypeCode.CLINIC);

                            navController.navigate(R.id.action_facilityListClinicTypeFragment_to_facilityAddFragment, bundle);
            }
        });
    }
}
