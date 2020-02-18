package com.thetatecno.fluidadmin.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.model.Code;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.model.Person;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.ui.adapters.AgentListAdapter;
import com.thetatecno.fluidadmin.ui.adapters.ClientListViewAdapter;
import com.thetatecno.fluidadmin.ui.adapters.CodeListAdapter;
import com.thetatecno.fluidadmin.ui.adapters.FacilityDetailViewAdapter;
import com.thetatecno.fluidadmin.ui.adapters.ProviderListAdapter;
import com.thetatecno.fluidadmin.utils.EnumCode;

import java.util.List;

public class MainFragment extends Fragment {

    RecyclerView detailsRecycle;
    static List<Staff> agentList = null;
    static List<Staff> providerList = null;
    static List<Person> personList = null;
    static List<Facility> facilityList = null;
    static List<Code> codeList;
    static EnumCode.UsageType usageType;
    ClientListViewAdapter clientListViewAdapter;
    AgentListAdapter agentListAdapter;
    ProviderListAdapter providerListAdapter;
    FacilityDetailViewAdapter facilityDetailViewAdapter;
    CodeListAdapter codeListAdapter;

    public static MainFragment setTypeAndData(EnumCode.UsageType type, @Nullable List<Staff> agentList,
                                              @Nullable List<Staff> providerList,
                                              @Nullable List<Person> personList,
                                              @Nullable List<Facility> facilityList,
                                              @Nullable List<Code> codeList
    ) {
        usageType = type;
        checkWhoIsHere(agentList, providerList, personList, facilityList, codeList);
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

        if (usageType == EnumCode.UsageType.Facility) {
            facilityDetailViewAdapter = new FacilityDetailViewAdapter(getActivity(), facilityList, getActivity().getSupportFragmentManager());
            detailsRecycle.setAdapter(facilityDetailViewAdapter);
        } else if (usageType == EnumCode.UsageType.Code) {
            codeListAdapter = new CodeListAdapter(getActivity(), codeList, getActivity().getSupportFragmentManager());
            detailsRecycle.setAdapter(codeListAdapter);
        } else if (usageType == EnumCode.UsageType.Agent) {
            agentListAdapter = new AgentListAdapter(getActivity(), agentList, getActivity().getSupportFragmentManager());
            detailsRecycle.setAdapter(agentListAdapter);

        } else if(usageType == EnumCode.UsageType.Provider) {
            providerListAdapter = new ProviderListAdapter(getActivity(),providerList,getActivity().getSupportFragmentManager());
            detailsRecycle.setAdapter(providerListAdapter);
        }
        else {
            clientListViewAdapter = new ClientListViewAdapter(getActivity(), personList, getActivity().getSupportFragmentManager());
            detailsRecycle.setAdapter(clientListViewAdapter);
        }


    }

    private static EnumCode.UsageType checkWhoIsHere(@Nullable List<Staff> agentList1,
                                                     @Nullable List<Staff> providerList1,
                                                     @Nullable List<Person> personList1,
                                                     @Nullable List<Facility> facilityList1,
                                                     @Nullable List<Code> codeList1) {
        if (usageType == EnumCode.UsageType.Agent) {
            agentList = agentList1;
            providerList = null;
            personList = null;
            facilityList = null;
            codeList = null;
        } else if (usageType == EnumCode.UsageType.Provider) {
            providerList = providerList1;
            agentList = null;
            personList = null;
            facilityList = null;
            codeList = null;

        } else if (usageType == EnumCode.UsageType.Person) {
            personList = personList1;
            agentList = null;
            providerList = null;
            facilityList = null;
            codeList = null;

        } else if (usageType == EnumCode.UsageType.Facility) {
            facilityList = facilityList1;
            agentList = null;
            providerList = null;
            personList = null;
            codeList = null;

        } else if (usageType == EnumCode.UsageType.Code) {
            codeList = codeList1;
            agentList = null;
            providerList = null;
            personList = null;
            facilityList = null;
        }

        return null;
    }

}
