package com.thetatechno.fluidadmin.ui.Session.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thetatechno.fluidadmin.R;

public class SessionFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    SessionListAdapter sessionListAdapter;
    TextView schedule_name_txt, doctor_name_txt, locationTxt, time_from_txt, time_to_txt;
    ImageView doctorImg;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.session_list_layout, container, false);
        recyclerView = view.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        schedule_name_txt = view.findViewById(R.id.schedule_name_txt);
        locationTxt = view.findViewById(R.id.locationTxt);
        time_from_txt = view.findViewById(R.id.time_from_txt);
        time_to_txt = view.findViewById(R.id.time_to_txt);
        doctor_name_txt = view.findViewById(R.id.doctor_name_txt);
        doctorImg = view.findViewById(R.id.doctorImg);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
