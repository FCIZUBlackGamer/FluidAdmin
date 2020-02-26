package com.thetatecno.fluidadmin.ui.addorupdatecode;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.listeners.OnFragmentInteractionListener;
import com.thetatecno.fluidadmin.model.Code;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.utils.App;
import com.thetatecno.fluidadmin.utils.EnumCode;
import com.thetatecno.fluidadmin.utils.PreferenceController;

import java.io.Serializable;
import java.util.List;

import static com.thetatecno.fluidadmin.utils.Constants.ARG_CODE;


public class CodeAddFragment extends Fragment {


    private Code code;
    private boolean isCodeNotNull;
    EditText codeIdEditTxt, codeDescriptionEditTxt, codeTypeEditTxt, userCodeEditTxt;
    Button cancel_btn, addOrUpdateBtn;
    CodeViewModel codeViewModel;

    NavController navController;
    List<Code> codeList;
    Bundle bundle;

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
        code.setCodeType(codeTypeEditTxt.getText().toString());
        code.setDescription(codeDescriptionEditTxt.getText().toString());
        code.setUserCode(userCodeEditTxt.getText().toString());
        code.setSystemRequired("");
        code.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());

    }

    private void updateViewWithData() {
        if (code != null) {
            isCodeNotNull = true;
            codeIdEditTxt.setText(code.getCode());
            userCodeEditTxt.setText(code.getUserCode());
            codeDescriptionEditTxt.setText(code.getDescription());
            codeTypeEditTxt.setText(code.getCodeType());
            addOrUpdateBtn.setHint(getResources().getString(R.string.update_txt));
        } else {
            isCodeNotNull = false;
            codeIdEditTxt.setText("");
            userCodeEditTxt.setText("");
            codeDescriptionEditTxt.setText("");
            codeTypeEditTxt.setText("");
            addOrUpdateBtn.setHint(getResources().getString(R.string.add_txt));
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
            mListener.onFragmentAddOrUpdateEntity(EnumCode.UsageType.Code);
        }
        navController.navigate(R.id.action_codeAddFragment_to_codeListFragment);

    }
    void onBackOrCancelBtnPressed(){

        navController.navigate(R.id.action_codeAddFragment_to_codeListFragment);
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
