package com.thetatechno.fluidadmin.ui.facilityList;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.ui.adapters.FacilityListAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacilityListWaitingAreaTypeFragment extends Fragment {


    RecyclerView facilityWaitingAreaTypeListRecyclerView;
    FloatingActionButton addNewFacilityFab;
    FacilityListAdapter facilityListAdapter;
    List<Facility> facilityList;
    FacilityListViewModel facilityListViewModel;
    NavController navController;

    public FacilityListWaitingAreaTypeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_facility_list_waiting_area_type, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        facilityWaitingAreaTypeListRecyclerView = view.findViewById(R.id.facilityWaitingAreaTypeListRecyclerView);
//        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
//        addNewFacilityFab = view.findViewById(R.id.fab);
//        facilityWaitingAreaTypeListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        facilityListViewModel = ViewModelProviders.of(this).get(FacilityListViewModel.class);
//        facilityListViewModel.getAllFacilities("",
//                PreferenceController.getInstance(getContext()).get(PreferenceController.LANGUAGE).toUpperCase(),
//                EnumCode.ClinicTypeCode.WAITAREA.toString()).observe(this, new Observer<Facilities>() {
//            @Override
//            public void onChanged(Facilities facilities) {
//                if (facilities != null) {
//                    if (facilities.getFacilities() != null) {
//                        facilityList = facilities.getFacilities();
//                        facilityListAdapter = new FacilityListAdapter(navController,getContext(),facilityList,getActivity().getSupportFragmentManager());
//                        facilityWaitingAreaTypeListRecyclerView.setAdapter(facilityListAdapter);
//                    }
//                }
//            }
//        });
//      addNewFacilityFab.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View v) {
//              navController.navigate(R.id.action_facilityListWaitingAreaTypeFragment_to_facilityAddFragment);
//          }
//      });
    }


}
