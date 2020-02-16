package com.thetatecno.fluidadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.model.Person;
import com.thetatecno.fluidadmin.model.Provider;
import com.thetatecno.fluidadmin.model.Staff;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    RecyclerView detailsRecycle;
    static List<Staff> agentList = null;
    static List<Staff> providerList = null;
    static List<Person> personList = null;
    static List<Facility> facilityList = null;
    static UsageType usageType;
    DetailViewAdapter detailViewAdapter;
    FacilityDetailViewAdapter facilityDetailViewAdapter;

    public static MainFragment setTypeAndData(UsageType type, @Nullable List<Staff> agentList,
                                              @Nullable List<Staff> providerList,
                                              @Nullable List<Person> personList,
                                              @Nullable List<Facility> facilityList
    ) {
        usageType = type;
        checkWhoIsHere(agentList, providerList, personList, facilityList);
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        detailsRecycle = view.findViewById(R.id.detailsList);
        detailsRecycle.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (usageType == UsageType.Facility) {
            facilityDetailViewAdapter = new FacilityDetailViewAdapter(getActivity(), facilityList, getActivity().getSupportFragmentManager());
            detailsRecycle.setAdapter(facilityDetailViewAdapter);
        } else {
            detailViewAdapter = new DetailViewAdapter(getActivity(), usageType, agentList, providerList, personList, getActivity().getSupportFragmentManager());
            detailsRecycle.setAdapter(detailViewAdapter);
        }

    }

    private static UsageType checkWhoIsHere(@Nullable List<Staff> agentList1,
                                            @Nullable List<Staff> providerList1,
                                            @Nullable List<Person> personList1,
                                            @Nullable List<Facility> facilityList1) {
        if (usageType == UsageType.Agent) {
            agentList = agentList1;
            providerList = null;
            personList = null;
            facilityList = null;
        } else if (usageType == UsageType.Provider) {
            providerList = providerList1;
            agentList = null;
            personList = null;
            facilityList = null;
        } else if (usageType == UsageType.Person) {
            personList = personList1;
            agentList = null;
            providerList = null;
            facilityList = null;
        } else if (usageType == UsageType.Facility) {
            facilityList = facilityList1;
            agentList = null;
            providerList = null;
            personList = null;
        }

        return null;
    }
}
