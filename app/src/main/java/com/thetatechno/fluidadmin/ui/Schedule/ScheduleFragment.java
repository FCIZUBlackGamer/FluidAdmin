package com.thetatechno.fluidadmin.ui.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.thetatechno.fluidadmin.model.shedule.ScheduleResponse;

public class ScheduleFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    ScheduleListAdapter scheduleListAdapter;
    ScheduleListViewModel scheduleListViewModel;
    NavController navController;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.schedule_list_layout, container, false);
        scheduleListViewModel = ViewModelProviders.of(this).get(ScheduleListViewModel.class);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        recyclerView = view.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        scheduleListViewModel.getAllSchedules().observe(getViewLifecycleOwner(), new Observer<ScheduleResponse>() {
            @Override
            public void onChanged(ScheduleResponse scheduleResponse) {
                if(scheduleResponse!=null) {
                    if(scheduleResponse.getError().getErrorCode() == 0) {
                        if(scheduleResponse.getSchedules() !=null) {
                            scheduleListAdapter = new ScheduleListAdapter(navController, getContext(), scheduleResponse.getSchedules());
                            recyclerView.setAdapter(scheduleListAdapter);
                        }
                        else {
                            Toast.makeText(getContext(),"No Schedule Found",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getContext(),scheduleResponse.getError().getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
