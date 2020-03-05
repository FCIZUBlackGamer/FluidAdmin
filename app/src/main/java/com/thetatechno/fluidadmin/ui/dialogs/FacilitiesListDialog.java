package com.thetatechno.fluidadmin.ui.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnConfirmLinkToFacilityListener;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.model.FacilityCodes;

import java.util.ArrayList;
import java.util.List;

public class FacilitiesListDialog extends Dialog {

    List<Facility> appointmentList;
    Context context;
    Button linkBtn, cancelBtn;
    String staffId ;
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

        appointmentListRecyclerView = findViewById(R.id.facilityListView);
        alertDialogAdapter = new FacilityListDialogAdapter(context, appointmentList);
        appointmentListRecyclerView.setAdapter(alertDialogAdapter);
        appointmentListRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        linkBtn = findViewById(R.id.link_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
        linkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                FacilityCodes facilityCodes = new FacilityCodes();
                List<String> selectedFacility = new ArrayList<>();
                appointmentList = alertDialogAdapter.getFacilityList();
                for (Facility facility : appointmentList) {
                    if(facility.isSelected())
                    selectedFacility.add(facility.getCode());
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

    }
}
