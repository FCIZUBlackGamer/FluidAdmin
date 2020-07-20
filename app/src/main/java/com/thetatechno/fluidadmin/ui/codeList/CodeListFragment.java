package com.thetatechno.fluidadmin.ui.codeList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.code_model.Code;
import com.thetatechno.fluidadmin.model.code_model.CodeList;
import com.thetatechno.fluidadmin.model.specialities_model.SpecialityCodeListModel;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

public class CodeListFragment extends Fragment {
    private NavController navController;
    private List<Code> codeList;
    private final String TAG = CodeListFragment.class.getSimpleName();
    private RecyclerView codeListRecyclerView;
    private FloatingActionButton addNewCodeFab;
    private CodeListAdapter codeListAdapter;
    private CodeListViewModel codeListViewModel;
    private SwipeRefreshLayout codeSwipeRefreshLayout;
    private ProgressBar codeListProgressBar;

    public CodeListFragment() {
        // Required empty public constructor
    }


    public static CodeListFragment newInstance() {
        CodeListFragment fragment = new CodeListFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_code_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        codeListRecyclerView = view.findViewById(R.id.codeListView);
        addNewCodeFab = view.findViewById(R.id.addNewCodeFab);
        codeListViewModel = ViewModelProviders.of(this).get(CodeListViewModel.class);
        codeSwipeRefreshLayout = view.findViewById(R.id.codeSwipeLayout);
        codeListProgressBar = view.findViewById(R.id.loadCodeProgressBar);
        codeListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        codeListProgressBar.setVisibility(View.VISIBLE);
        codeListViewModel.getDataForCode(EnumCode.Code.STFFGRP.toString(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase()).observe(getViewLifecycleOwner(), new Observer<SpecialityCodeListModel>() {
            @Override
            public void onChanged(SpecialityCodeListModel model) {
                codeListProgressBar.setVisibility(View.GONE);
                    if (model.getCodeList() !=null && model.getCodeList().getCodeList() != null) {
                        EspressoTestingIdlingResource.increment();
                        codeList = model.getCodeList().getCodeList();
                        codeListAdapter = new CodeListAdapter(navController, getContext(), codeList);
                        codeListRecyclerView.setAdapter(codeListAdapter);
                        EspressoTestingIdlingResource.decrement();
                    } else {
                        Toast.makeText(getContext(),model.getErrorMessage(),Toast.LENGTH_SHORT).show();
                    }
            }
        });

        codeSwipeRefreshLayout.setOnRefreshListener(() -> {
            codeSwipeRefreshLayout.setRefreshing(true);
            codeListViewModel.getDataForCode(EnumCode.Code.STFFGRP.toString(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
            codeSwipeRefreshLayout.setRefreshing(false);

        });
        addNewCodeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_codeListFragment_to_codeAddFragment);


            }
        });

    }


}
