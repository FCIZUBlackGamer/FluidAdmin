package com.thetatechno.fluidadmin.ui.Session;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import com.thetatechno.fluidadmin.listeners.OnReloadDataListener;
import com.thetatechno.fluidadmin.model.session_model.SessionResponse;
import com.thetatechno.fluidadmin.model.session_model.SessionResponseModel;

public class SessionFragment extends Fragment implements SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener, OnReloadDataListener {
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
        sessionListViewModel.getAllSessions().observe(getViewLifecycleOwner(), new Observer<SessionResponseModel>() {
            @Override
            public void onChanged(SessionResponseModel model) {
                loadSessionsProgressBar.setVisibility(View.GONE);

                if (model.getSessionResponse() != null) {
                    if (model.getSessionResponse().getError().getErrorCode() == 0) {
                        if (model.getSessionResponse().getSessions() != null) {
                            sessionListAdapter = new SessionListAdapter(navController, getContext(), model.getSessionResponse().getSessions());
                            recyclerView.setAdapter(sessionListAdapter);
                        } else {
                            Toast.makeText(getContext(), R.string.no_sessions_found_txt, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getContext(), model.getSessionResponse().getError().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), model.getErrorMessage(), Toast.LENGTH_SHORT).show();

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

    @Override
    public void onReload() {
        loadSessionsProgressBar.setVisibility(View.VISIBLE);
        sessionListViewModel.getAllSessions();
    }
}
