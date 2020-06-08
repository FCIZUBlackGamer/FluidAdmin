package com.thetatechno.fluidadmin.ui.Session;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.session_model.SessionResponse;
import com.thetatechno.fluidadmin.model.shedule.Schedule;

public class SessionFragment extends Fragment {
    private Schedule schedule;
    private View view;
    private RecyclerView recyclerView;
    private SessionListAdapter sessionListAdapter;
    private TextView schedule_name_txt, doctor_name_txt, locationTxt, time_from_txt, time_to_txt;
    private ImageView doctorImg;
    private NavController navController;
    private SessionListViewModel sessionListViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            schedule = (Schedule) getArguments().getSerializable("schedule");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.session_list_layout, container, false);
        sessionListViewModel = ViewModelProviders.of(this).get(SessionListViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
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
        schedule_name_txt.setText(schedule.getDescription());
        locationTxt.setText(schedule.getFacilitDescription());
        time_from_txt.setText(schedule.getStartTime());
        time_to_txt.setText(schedule.getEndTime());
        doctor_name_txt.setText(schedule.getProviderName());
        //TODO : add code for provider image
        sessionListViewModel.getAllSessionsForSpecificSchedule(schedule.getId()).observe(getViewLifecycleOwner(), new Observer<SessionResponse>() {
            @Override
            public void onChanged(SessionResponse sessionResponse) {
                if (sessionResponse != null) {
                    if (sessionResponse.getError().getErrorCode() == 0) {
                        if (sessionResponse.getSessions() != null) {
                            sessionListAdapter = new SessionListAdapter(navController, getContext(), sessionResponse.getSessions());
                            recyclerView.setAdapter(sessionListAdapter);
                        } else {
                            Toast.makeText(getContext(), "No sessions Found", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getContext(), sessionResponse.getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
