package com.thetatecno.fluidadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thetatecno.fluidadmin.model.Agent;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.model.Person;
import com.thetatecno.fluidadmin.model.Provider;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    RecyclerView detailsRecycle;
    static List<Agent> agentList;
    static List<Provider> providerList;
    static List<Person> personList;
    static List<Facility> facilityList;
    static UsageType usageType;
    DetailViewAdapter detailViewAdapter;
    FacilityDetailViewAdapter facilityDetailViewAdapter;

    public static MainFragment setTypeAndData(UsageType type, @Nullable List<Agent> agentList,
                                       @Nullable List<Provider> providerList,
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
        } else {
            detailViewAdapter = new DetailViewAdapter(getActivity(), agentList, providerList, personList, getActivity().getSupportFragmentManager());
        }

        detailsRecycle.setAdapter(detailViewAdapter);
    }

    private static UsageType checkWhoIsHere(@Nullable List<Agent> agentList1,
                                     @Nullable List<Provider> providerList1,
                                     @Nullable List<Person> personList1,
                                     @Nullable List<Facility> facilityList1) {
        if (usageType == UsageType.Agent) {
            agentList = agentList1;
        } else if (usageType == UsageType.Provider) {
            providerList = providerList1;
        } else if (usageType == UsageType.Person) {
            personList = personList1;
        } else if (usageType == UsageType.Facility) {
            facilityList = facilityList1;
        }

        return null;
    }
}
