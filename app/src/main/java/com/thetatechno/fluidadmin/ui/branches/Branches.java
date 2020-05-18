package com.thetatechno.fluidadmin.ui.branches;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.model.StaffData;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.branches_model.BranchesResponse;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.ui.stafflist.StaffListViewModel;
import com.thetatechno.fluidadmin.ui.stafflist.agentList.AgentListAdapter;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.ArrayList;
import java.util.List;


public class Branches extends Fragment {
    private SwipeRefreshLayout branchesSwipeLayout;
    private List<Branch> branchesList;
    private BranchesAdapter branchesAdapter;
    private BranchesViewModel branchesViewModel;
    private RecyclerView branchesRecyclerView;
   private FloatingActionButton addNewBranchBtn;
   private NavController navController;
private static final String TAG = Branches.class.getSimpleName();
    public Branches() {
        // Required empty public constructor
    }


    public static Branches newInstance() {
        Branches fragment = new Branches();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        branchesViewModel = new ViewModelProvider(this).get(BranchesViewModel.class);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        return inflater.inflate(R.layout.fragment_branches, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addNewBranchBtn = view.findViewById(R.id.addNewBranchBtn);
        branchesRecyclerView = view.findViewById(R.id.branchesRecyclerView);
        branchesSwipeLayout = view.findViewById(R.id.branchesSwipeLayout);

        addNewBranchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_branches_to_addOrUpdateBranch);
            }
        });
        setHasOptionsMenu(true);
        branchesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

            EspressoTestingIdlingResource.increment();
            branchesViewModel.getAllBranches().observe(getViewLifecycleOwner(), new Observer<BranchesResponse>() {
                @Override
                public void onChanged(BranchesResponse branchesResponse) {
                    if (branchesResponse != null) {
                        if (branchesResponse.getBranchList()!= null) {
                            EspressoTestingIdlingResource.increment();
                            branchesList = branchesResponse.getBranchList();
                            branchesAdapter = new BranchesAdapter(navController, getContext(),branchesList);
                            branchesRecyclerView.setAdapter(branchesAdapter);
                            EspressoTestingIdlingResource.decrement();
                        } else {

                        }
                    }else{
                        Log.e(TAG, "no data returns");
                    }

                }
            });
        EspressoTestingIdlingResource.decrement();


        branchesSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                branchesSwipeLayout.setRefreshing(true);
                branchesViewModel.getAllBranches();
                branchesSwipeLayout.setRefreshing(false);

            }
        });


    }
}
