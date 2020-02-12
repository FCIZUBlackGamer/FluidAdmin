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
    List<Agent> agentList;
    List<Provider> providerList;
    List<Person> personList;
    List<Facility> facilityList;
    UsageType usageType;
    DetailViewAdapter detailViewAdapter;
    FacilityDetailViewAdapter facilityDetailViewAdapter;

    public MainFragment setTypeAndData(UsageType type, @Nullable List<Agent> agentList,
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

    private UsageType checkWhoIsHere(@Nullable List<Agent> agentList,
                                     @Nullable List<Provider> providerList,
                                     @Nullable List<Person> personList,
                                     @Nullable List<Facility> facilityList) {
        if (usageType == UsageType.Agent) {
            this.agentList = agentList;
        } else if (usageType == UsageType.Provider) {
            this.providerList = providerList;
        } else if (usageType == UsageType.Person) {
            this.personList = personList;
        } else if (usageType == UsageType.Facility) {
            this.facilityList = facilityList;
        }

        return null;
    }
}
