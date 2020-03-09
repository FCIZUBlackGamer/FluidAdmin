package com.thetatechno.fluidadmin.ui.codeList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.model.CodeList;
import com.thetatechno.fluidadmin.ui.adapters.CodeListAdapter;
import com.thetatechno.fluidadmin.ui.addorupdatecode.CodeAddFragment;
import com.thetatechno.fluidadmin.ui.stafflist.AgentListFragment;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

public class CodeListFragment extends Fragment {
    NavController navController;
    List<Code> codeList;
    final String TAG = AgentListFragment.class.getSimpleName();
    RecyclerView codeListRecyclerView;
    FloatingActionButton addNewCodeFab;
    CodeListAdapter codeListAdapter;
    CodeListViewModel codeListViewModel;
    SwipeRefreshLayout codeSwipeRefreshLayout;


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
        addNewCodeFab = view.findViewById(R.id.fab);
        codeListViewModel = ViewModelProviders.of(this).get(CodeListViewModel.class);
        codeSwipeRefreshLayout = view.findViewById(R.id.codeSwipeLayout);
        codeListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            codeListViewModel.getDataForCode(EnumCode.Code.STFFGRP.toString(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase()).observe(this, new Observer<CodeList>() {
                @Override
                public void onChanged(CodeList codeListData) {
                    if (codeListData != null) {
                        if (codeListData.getCodeList() != null) {
                            codeList = codeListData.getCodeList();
                            codeListAdapter = new CodeListAdapter(navController, getContext(), codeList, getActivity().getSupportFragmentManager());
                            codeListRecyclerView.setAdapter(codeListAdapter);
                            codeSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            Log.e("Staff List", "Is Null");
                        }
                    } else {
                        Log.e("Staff", "Is Null");
                    }
                }
            });
       codeSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               codeSwipeRefreshLayout.setRefreshing(true);
               codeListViewModel.getDataForCode(EnumCode.Code.STFFGRP.toString(), PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
           }
       });
        addNewCodeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               navController.navigate(R.id.action_codeListFragment_to_codeAddFragment);
//                CodeAddFragment codeAddFragment = CodeAddFragment.newInstance(null);
//                codeAddFragment.show(getActivity().getSupportFragmentManager(),CodeAddFragment.class.getSimpleName());

            }
        });

    }

}
