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
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.session_model.SessionResponse;
import com.thetatechno.fluidadmin.model.shedule.Schedule;

public class SessionFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private SessionListAdapter sessionListAdapter;
    private NavController navController;
    private SessionListViewModel sessionListViewModel;
    FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.session_list_layout, container, false);
        sessionListViewModel = ViewModelProviders.of(this).get(SessionListViewModel.class);
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        recyclerView = view.findViewById(R.id.rec);
        floatingActionButton = view.findViewById(R.id.fab);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
            sessionListViewModel.getAllSessionsForSpecificSchedule("").observe(getViewLifecycleOwner(), new Observer<SessionResponse>() {
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

        floatingActionButton.setOnClickListener(v -> {
            navController.navigate(R.id.fragmentAddOrUpdateSesssion);
        });
    }
}
