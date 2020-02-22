package com.thetatecno.fluidadmin.ui.addorupdatefacility;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.listeners.OnFragmentInteractionWithFacilityTypesListener;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.utils.App;
import com.thetatecno.fluidadmin.utils.EnumCode;
import com.thetatecno.fluidadmin.utils.PreferenceController;

import java.io.Serializable;
import java.util.List;

import static com.thetatecno.fluidadmin.utils.Constants.ARG_FACILITY;


public class FacilityAddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters
    private Facility facility;
    //facility_id_et, desc_et, type_et, waiting_area_id_et, device_id_et
    EditText facility_id_et, desc_et, type_et, waiting_area_id_et, device_id_et;
    Button cancel_btn, addOrUpdateBtn;
    boolean isDataFound;
    FacilityAddViewModel facilityAddViewModel;
    private OnFragmentInteractionWithFacilityTypesListener mListener;
    NavController navController;
    Bundle bundle;
    List<Facility> facilityList;

    public FacilityAddFragment() {
        // Required empty public constructor
    }


    public static FacilityAddFragment newInstance(Facility facility) {
        FacilityAddFragment fragment = new FacilityAddFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_FACILITY, facility);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            facility = (Facility) getArguments().getSerializable(ARG_FACILITY);
            facilityList = (List<Facility>) getArguments().getSerializable("facilityList");

        }

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                onCancelOrBackButtonClicked();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.facility_add_view, container, false);
        facilityAddViewModel = ViewModelProviders.of(this).get(FacilityAddViewModel.class);
        initViews(view);
        return view;
    }

    private void initViews(View view) {

        facility_id_et = view.findViewById(R.id.facility_id_et);
        desc_et = view.findViewById(R.id.desc_et);
        type_et = view.findViewById(R.id.type_et);
        waiting_area_id_et = view.findViewById(R.id.waiting_area_id_et);
        device_id_et = view.findViewById(R.id.device_id_et);
        cancel_btn = view.findViewById(R.id.cancel_btn);
        addOrUpdateBtn = view.findViewById(R.id.addOrUpdateBtn);
        updateData();
        addOrUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataFound) {
                    getData();
                    facilityAddViewModel.updateFacility(facility).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i("FacilityFragment", "update response message " + s);
                            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT);
                            if(s.contains("successfully"))
                                onAddOrUpdateClicked();

                        }
                    });
                } else {
                    facility = new Facility();
                    getData();
                    facilityAddViewModel.addNewFacility(facility).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i("FacilityFragment", "add response message " + s);
                            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                            if(s.contains("successfully"))
                                onAddOrUpdateClicked();

                        }
                    });

                }
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelOrBackButtonClicked();
            }
        });

    }

    private void onCancelOrBackButtonClicked() {
        mListener.onDisplayAddBtn();
        bundle = new Bundle();
        bundle.putSerializable("type", (Serializable) EnumCode.UsageType.Facility);
        bundle.putSerializable("facilityList", (Serializable) facilityList);
        navController.navigate(R.id.action_facilityAddFragment_to_mainFragment2, bundle);

    }

    void onAddOrUpdateClicked(){
        if(type_et.getText().toString().equals(EnumCode.ClinicTypeCode.CLINIC.toString())) {
            mListener.onAddOrUpdateFacility(EnumCode.ClinicTypeCode.CLINIC);
        }
        else if (type_et.getText().toString().equals(EnumCode.ClinicTypeCode.WAITAREA.toString()))
        {
            mListener.onAddOrUpdateFacility(EnumCode.ClinicTypeCode.WAITAREA);
        }
    }


    private void updateData() {
        if (facility != null) {
            isDataFound = true;
            facility_id_et.setText(facility.getCode());
            desc_et.setText(facility.getDescription());
            type_et.setText(facility.getType());
            waiting_area_id_et.setText(facility.getWaitingAreaID());
            device_id_et.setText(facility.getDeviceId());
            addOrUpdateBtn.setHint(getResources().getString(R.string.update_txt));
        } else {
            isDataFound = false;
            facility_id_et.setText("");
            desc_et.setText("");
            type_et.setText("");
            waiting_area_id_et.setText("");
            device_id_et.setText("");
            addOrUpdateBtn.setHint(getResources().getString(R.string.add_txt));

        }
    }

    private void getData() {
        facility.setCode(facility_id_et.getText().toString());
        facility.setDescription(desc_et.getText().toString());
        facility.setDeviceId(device_id_et.getText().toString());
        facility.setWaitingAreaID(waiting_area_id_et.getText().toString());
        facility.setType(type_et.getText().toString());
        facility.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());

    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionWithFacilityTypesListener) {
            mListener = (OnFragmentInteractionWithFacilityTypesListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionWithFacilityTypesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
