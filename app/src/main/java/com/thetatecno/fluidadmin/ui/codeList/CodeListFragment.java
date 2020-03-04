package com.thetatecno.fluidadmin.ui.codeList;

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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.model.Code;
import com.thetatecno.fluidadmin.model.CodeList;
import com.thetatecno.fluidadmin.ui.adapters.CodeListAdapter;
import com.thetatecno.fluidadmin.ui.stafflist.AgentListFragment;
import com.thetatecno.fluidadmin.utils.App;
import com.thetatecno.fluidadmin.utils.PreferenceController;

import java.util.List;

public class CodeListFragment extends Fragment {
    NavController navController;
    List<Code> codeList;
    final String TAG = AgentListFragment.class.getSimpleName();
    RecyclerView codeListRecyclerView;
    FloatingActionButton addNewCodeFab;
    CodeListAdapter codeListAdapter;
    CodeListViewModel codeListViewModel;


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
        codeListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        if (codeList == null)
            codeListViewModel.getDataForCode("", PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase()).observe(this, new Observer<CodeList>() {
                @Override
                public void onChanged(CodeList codeListData) {
                    if (codeListData != null) {
                        if (codeListData.getCodeList() != null) {
                            codeList = codeListData.getCodeList();
                            codeListAdapter = new CodeListAdapter(navController, getContext(), codeList, getActivity().getSupportFragmentManager());
                            codeListRecyclerView.setAdapter(codeListAdapter);
                        } else {
                            Log.e("Staff List", "Is Null");
                        }
                    } else {
                        Log.e("Staff", "Is Null");
                    }
                }
            });
        else {
            codeListAdapter = new CodeListAdapter(navController, getContext(), codeList, getActivity().getSupportFragmentManager());
            codeListRecyclerView.setAdapter(codeListAdapter);

        }
        addNewCodeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navController.navigate(R.id.action_codeListFragment_to_codeAddFragment);

            }
        });

    }

}
