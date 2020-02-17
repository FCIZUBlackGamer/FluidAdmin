package com.thetatecno.fluidadmin.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.model.Facility;


public class FacilityAddFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FACILITY = "facility";

    // TODO: Rename and change types of parameters
    private Facility facility;
    //facility_id_et, desc_et, type_et, waiting_area_id_et, device_id_et
    EditText facility_id_et, desc_et, type_et, waiting_area_id_et, device_id_et;
    Button cancel_btn, addOrUpdateBtn;
    boolean isDataFound;
    FacilityAddViewModel facilityAddViewModel;
    private OnFragmentInteractionListener mListener;

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

        }
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
                        }
                    });
                } else {
                    facility = new Facility();
                    getData();
                    facilityAddViewModel.addNewFacility(facility).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i("FacilityFragment", "add response message " + s);

                        }
                    });

                }
            }
        });

    }


    private void updateData() {
        if (facility != null) {
            isDataFound = true;
            facility_id_et.setText(facility.getCode());
            desc_et.setText(facility.getDescription());
            type_et.setText(facility.getType());
            waiting_area_id_et.setText(facility.getWaitingAreaID());
            device_id_et.setText(facility.getDeviceId());
            addOrUpdateBtn.setText(getResources().getString(R.string.update_txt));
        } else {
            isDataFound = false;
            facility_id_et.setText("");
            desc_et.setText("");
            type_et.setText("");
            waiting_area_id_et.setText("");
            device_id_et.setText("");
            addOrUpdateBtn.setText(getResources().getString(R.string.add_txt));

        }
    }

    private void getData() {
        facility.setCode(facility_id_et.getText().toString());
        facility.setDescription(desc_et.getText().toString());
        facility.setDeviceId(device_id_et.getText().toString());
        facility.setWaitingAreaID(waiting_area_id_et.getText().toString());
        facility.setType(type_et.getText().toString());
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
