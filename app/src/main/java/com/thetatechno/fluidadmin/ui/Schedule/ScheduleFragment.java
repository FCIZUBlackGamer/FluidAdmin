package com.thetatechno.fluidadmin.ui.Schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.shedule.ScheduleResponse;

public class ScheduleFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    ScheduleListAdapter scheduleListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.schedule_list_layout, container, false);
        recyclerView = view.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
