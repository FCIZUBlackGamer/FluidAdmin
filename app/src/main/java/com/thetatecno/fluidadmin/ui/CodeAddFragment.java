package com.thetatecno.fluidadmin.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.model.Code;


public class CodeAddFragment extends Fragment {

    private static final String ARG_CODE = "code";

    private Code code;
    private boolean isCodeNotNull;
    EditText codeIdEditTxt, codeDescriptionEditTxt, codeTypeEditTxt, userCodeEditTxt;
    Button cancel_btn, addOrUpdateBtn;
    CodeViewModel codeViewModel;


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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.code_add_view, container, false);
        codeViewModel = ViewModelProviders.of(this).get(CodeViewModel.class);
        initViews(view);

        updateViewWithData();

        addOrUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCodeNotNull) {
                    getDataFromUi();
                    codeViewModel.updateCode(code).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i("CodeFragment","updateCode message "+ s);
                        }
                    });
                } else {
                    code = new Code();
                    getDataFromUi();
                    codeViewModel.addNewCode(code).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i("CodeFragment","addNewCode message "+ s);
                        }
                    });
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed();
            }
        });
        return view;
    }
    private  void  getDataFromUi(){
        code.setCode(codeIdEditTxt.getText().toString());
        code.setCodeType(codeTypeEditTxt.getText().toString());
        code.setDescription(codeDescriptionEditTxt.getText().toString());
        code.setUserCode(userCodeEditTxt.getText().toString());
        code.setSystemRequired("");
    }

    private void updateViewWithData() {
        if (code != null) {
            isCodeNotNull = true;
            codeIdEditTxt.setText(code.getCode());
            userCodeEditTxt.setText(code.getUserCode());
            codeDescriptionEditTxt.setText(code.getDescription());
            codeTypeEditTxt.setText(code.getCodeType());
            addOrUpdateBtn.setText(getResources().getString(R.string.update_txt));
        } else {
            isCodeNotNull = false;
            codeIdEditTxt.setText("");
            userCodeEditTxt.setText("");
            codeDescriptionEditTxt.setText("");
            codeTypeEditTxt.setText("");
            addOrUpdateBtn.setText(getResources().getString(R.string.add_txt));
        }
    }

    private void initViews(View view) {
        codeIdEditTxt = view.findViewById(R.id.code_id_et);
        codeDescriptionEditTxt = view.findViewById(R.id.desc_et);
        codeTypeEditTxt = view.findViewById(R.id.type_et);
        userCodeEditTxt = view.findViewById(R.id.user_code_et);
        cancel_btn = view.findViewById(R.id.cancel_btn);
        addOrUpdateBtn = view.findViewById(R.id.addOrUpdateCodeBtn);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
