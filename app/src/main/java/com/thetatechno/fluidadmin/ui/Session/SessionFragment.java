package com.thetatechno.fluidadmin.ui.Session;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.session_model.SessionResponse;
import com.thetatechno.fluidadmin.model.shedule.Schedule;

public class SessionFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener  {
    private View view;
    private RecyclerView recyclerView;
    private SessionListAdapter sessionListAdapter;
    private NavController navController;
    private SessionListViewModel sessionListViewModel;
   private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout sessionSwipeRefreshLayout;
private ProgressBar loadSessionsProgressBar;
    private SearchView searchView;

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
        setHasOptionsMenu(true);
        floatingActionButton = view.findViewById(R.id.fab);
        sessionSwipeRefreshLayout = view.findViewById(R.id.sessionSwipeRefreshLayout);
        loadSessionsProgressBar = view.findViewById(R.id.loadSessionProgressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        loadSessionsProgressBar.setVisibility(View.VISIBLE);
            sessionListViewModel.getAllSessions().observe(getViewLifecycleOwner(), new Observer<SessionResponse>() {
                @Override
                public void onChanged(SessionResponse sessionResponse) {
                    loadSessionsProgressBar.setVisibility(View.GONE);

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
                    else {
                        Snackbar.make(sessionSwipeRefreshLayout,"Failed to load session",Snackbar.LENGTH_LONG).setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sessionListViewModel.getAllSessions();
                            }
                        }).setAnchorView(floatingActionButton).show();
                    }
                }
            });
        sessionSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sessionSwipeRefreshLayout.setRefreshing(true);
                sessionListViewModel.getAllSessions();
                sessionSwipeRefreshLayout.setRefreshing(false);
            }
        });

        floatingActionButton.setOnClickListener(v -> {
            navController.navigate(R.id.fragmentAddOrUpdateSesssion);
        });
    }

    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {

        return true;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        sessionListAdapter.getFilter().filter(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        sessionListAdapter.getFilter().filter(newText);
        return false;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.home, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Search");
        super.onCreateOptionsMenu(menu, inflater);
    }
}
