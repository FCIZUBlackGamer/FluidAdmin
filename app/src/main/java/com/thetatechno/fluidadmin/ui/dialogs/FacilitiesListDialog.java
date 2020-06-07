package com.thetatechno.fluidadmin.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnConfirmLinkToFacilityListener;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.facility_model.FacilityCodes;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;

import java.util.ArrayList;
import java.util.List;

public class FacilitiesListDialog extends Dialog {

    List<Facility> appointmentList;
    Context context;
    TextView linkBtn, cancelBtn;
    String staffId ;
    SearchView searchForSpecificFacilityView;
    OnConfirmLinkToFacilityListener onConfirmLinkToFacilityListener;
    public FacilityListDialogAdapter alertDialogAdapter;
    RecyclerView appointmentListRecyclerView;

    public FacilitiesListDialog(@NonNull Context context, List<Facility> appointmentList,String staffId) {
        super(context);
        this.context = context;
        if (context instanceof OnConfirmLinkToFacilityListener)
            onConfirmLinkToFacilityListener = (OnConfirmLinkToFacilityListener) context;
        this.appointmentList = appointmentList;
        this.staffId = staffId;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.link_to_facilities_dialog);
         EspressoTestingIdlingResource.increment();
        appointmentListRecyclerView = findViewById(R.id.facilityListView);
        alertDialogAdapter = new FacilityListDialogAdapter(context, appointmentList);
        searchForSpecificFacilityView = findViewById(R.id.searchForSpecificFacility);
        appointmentListRecyclerView.setAdapter(alertDialogAdapter);
        appointmentListRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        EspressoTestingIdlingResource.decrement();
        linkBtn = findViewById(R.id.link_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FacilityCodes facilityCodes = new FacilityCodes();
                List<String> selectedFacility = new ArrayList<>();
                appointmentList = alertDialogAdapter.getFacilityList();
                for (Facility facility : appointmentList) {
                    if(facility.isSelected())
                    selectedFacility.add(facility.getId());
                }
                facilityCodes.setSelectedFacilities(selectedFacility);
                onConfirmLinkToFacilityListener.onConfirmLinkToFacility(staffId,facilityCodes);

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        searchForSpecificFacilityView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                EspressoTestingIdlingResource.increment();
                alertDialogAdapter.getFilter().filter(query);
                EspressoTestingIdlingResource.decrement();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                EspressoTestingIdlingResource.increment();
                alertDialogAdapter.getFilter().filter(newText);
                EspressoTestingIdlingResource.decrement();

                return false;
            }
        });

    }
}
