package com.thetatechno.fluidadmin.ui.showSession;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.databinding.OnRebindCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.bumptech.glide.Glide;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnReloadDataListener;
import com.thetatechno.fluidadmin.model.session_model.SessionResponse;
import com.thetatechno.fluidadmin.model.shedule.Schedule;
import com.thetatechno.fluidadmin.ui.Schedule.ChipAdapter;
import com.thetatechno.fluidadmin.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ShowSessions extends Fragment implements OnReloadDataListener {
    private Schedule schedule;
    private View view;
    private RecyclerView sessionRecyclerView, workingDaysRecyclerView;
    private SessionsAdapter sessionListAdapter;
    private TextView schedule_name_txt, doctor_name_txt, locationTxt, time_from_txt, time_to_txt,workingDaysTitleTxtView;
    private ImageView doctorImg;
    private NavController navController;
    private ProgressBar loadSessionsForSpecificScheduleProgressBar;
    private ShowSessionsViewModel sessionListViewModel;
    CardView layout;

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
        view = inflater.inflate(R.layout.fragment_show_sessions, container, false);
        sessionListViewModel = ViewModelProviders.of(this).get(ShowSessionsViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        sessionRecyclerView = view.findViewById(R.id.sessionRec);
        workingDaysRecyclerView = view.findViewById(R.id.rec);
        layout = view.findViewById(R.id.layout);
        sessionRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        workingDaysRecyclerView.setLayoutManager(ChipsLayoutManager.newBuilder(requireActivity()).setOrientation(ChipsLayoutManager.HORIZONTAL).build());
        schedule_name_txt = view.findViewById(R.id.schedule_name_txt);
        loadSessionsForSpecificScheduleProgressBar = view.findViewById(R.id.loadSessionsForSpecificScheduleProgressBar);
        locationTxt = view.findViewById(R.id.locationTxt);
        time_from_txt = view.findViewById(R.id.time_from_txt);
        time_to_txt = view.findViewById(R.id.time_to_txt);
        doctor_name_txt = view.findViewById(R.id.doctor_name_txt);
        doctorImg = view.findViewById(R.id.doctorImg);
        workingDaysTitleTxtView = view.findViewById(R.id.workingDaysTitleTxtView);
        view.findViewById(R.id.scheduleOptionMenu).setVisibility(View.INVISIBLE);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        layout.setVisibility(View.VISIBLE);
        schedule_name_txt.setText(schedule.getDescription());
        locationTxt.setText(schedule.getFacilitDescription());
        time_from_txt.setText(schedule.getStartTime());
        time_to_txt.setText(schedule.getEndTime());
        doctor_name_txt.setText(schedule.getProviderName());
        if (!schedule.getImagePath().isEmpty()) {
            Glide.with(getContext()).load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + schedule.getImagePath())
                    .circleCrop()
                    .placeholder(R.drawable.man)
                    .into(doctorImg);
        }
        loadSessionsForSpecificScheduleProgressBar.setVisibility(View.VISIBLE);
        if(schedule.getWorkingDays()!=null){
            List<String> selectedDays = new ArrayList<String>();
            int len = schedule.getWorkingDays().length();
            for (int i = 0; i < len; i += 3) {
                selectedDays.add(schedule.getWorkingDays().substring(i, Math.min(len, i + 3)));
            }
            ChipAdapter chipAdapter = new ChipAdapter(requireActivity(), selectedDays);
            workingDaysRecyclerView.setAdapter(chipAdapter);
        }
        else {
            workingDaysRecyclerView.setVisibility(View.GONE);
            workingDaysTitleTxtView.setVisibility(View.GONE);
        }
        sessionListViewModel.getAllSessionsRelatedToSchedule(schedule.getId()).observe(getViewLifecycleOwner(), new Observer<SessionResponse>() {
            @Override
            public void onChanged(SessionResponse sessionResponse) {
                loadSessionsForSpecificScheduleProgressBar.setVisibility(View.GONE);

                if (sessionResponse != null) {
                    if (sessionResponse.getError().getErrorCode() == 0) {
                        if (sessionResponse.getSessions() != null) {
                            sessionListAdapter = new SessionsAdapter(navController, getContext(), sessionResponse.getSessions());
                            sessionRecyclerView.setAdapter(sessionListAdapter);
//                            ViewCompat.setNestedScrollingEnabled(sessionRecyclerView, false);
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

    @Override
    public void onReload() {
            loadSessionsForSpecificScheduleProgressBar.setVisibility(View.VISIBLE);
            sessionListViewModel.getAllSessionsRelatedToSchedule(schedule.getId());

    }
}
