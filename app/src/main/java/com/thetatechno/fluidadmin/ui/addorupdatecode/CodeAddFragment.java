package com.thetatechno.fluidadmin.ui.addorupdatecode;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnFragmentInteractionListener;
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_CODE;


public class CodeAddFragment extends DialogFragment {


    private Code code;
    private boolean isCodeNotNull;
    EditText codeIdEditTxt, codeDescriptionEditTxt;
    Button cancel_btn, addOrUpdateBtn;
    CodeViewModel codeViewModel;
    NavController navController;
    List<Code> codeList;

    private OnFragmentInteractionListener mListener;

    public CodeAddFragment() {
        // Required empty public constructor
    }


    public static CodeAddFragment newInstance(Code code) {
        CodeAddFragment fragment = new CodeAddFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CODE, code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            code = (Code) getArguments().getSerializable(ARG_CODE);
            codeList = (List<Code>) getArguments().getSerializable("codeList");

        }
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);


        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                onBackOrCancelBtnPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.code_add_view, container, false);
        codeViewModel = ViewModelProviders.of(this).get(CodeViewModel.class);
        initViews(view);

        updateViewWithData();

        if (this.getArguments() != null) {
            codeIdEditTxt.setEnabled(false);
        }

        addOrUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCodeNotNull) {
                    getDataFromUi();
                    codeViewModel.updateCode(code).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
                            Log.i("CodeFragment","updateCode message "+ s);

                            if(s.contains("successfully"))
                            onButtonPressed();

                        }
                    });
                } else {
                    code = new Code();
                    getDataFromUi();
                    codeViewModel.addNewCode(code).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i("CodeFragment","addNewCode message "+ s);
                            Toast.makeText(getActivity(), s,Toast.LENGTH_SHORT).show();
                            if(s.contains("successfully"))
                                onButtonPressed();
                        }
                    });
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackOrCancelBtnPressed();
            }
        });
        return view;
    }

    private  void  getDataFromUi(){
        code.setCode(codeIdEditTxt.getText().toString());
        code.setDescription(codeDescriptionEditTxt.getText().toString());
        code.setSystemRequired("Y");
        code.setUserCode(codeIdEditTxt.getText().toString());
        code.setCodeType(EnumCode.Code.STFFGRP.toString());
        code.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());

    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        if(window == null) return;
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;;
        window.setAttributes(params);
    }

    private void updateViewWithData() {
        if (code != null) {
            isCodeNotNull = true;
            codeIdEditTxt.setText(code.getCode());
            codeDescriptionEditTxt.setText(code.getDescription());
            addOrUpdateBtn.setHint(getResources().getString(R.string.update_txt));
        } else {
            isCodeNotNull = false;
            codeIdEditTxt.setText("");
            codeDescriptionEditTxt.setText("");
            addOrUpdateBtn.setHint(getResources().getString(R.string.add_txt));
        }
    }

    private void initViews(View view) {
        codeIdEditTxt = view.findViewById(R.id.code_id_et);
        codeDescriptionEditTxt = view.findViewById(R.id.desc_et);
        cancel_btn = view.findViewById(R.id.cancel_btn);
        addOrUpdateBtn = view.findViewById(R.id.addOrUpdateCodeBtn);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentAddOrUpdateEntity(EnumCode.UsageType.Code);
        }
        navController.navigate(R.id.action_codeAddFragment_to_codeListFragment);

    }
    void onBackOrCancelBtnPressed(){
//        navController.navigate(R.id.action_codeAddFragment_to_codeListFragment);
        dismiss();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
