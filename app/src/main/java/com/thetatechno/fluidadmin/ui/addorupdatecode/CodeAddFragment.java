package com.thetatechno.fluidadmin.ui.addorupdatecode;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnFragmentInteractionListener;
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.ui.HomeActivity;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_CODE;


public class CodeAddFragment extends DialogFragment {


    private Code code;
    EditText codeIdEditTxt, codeDescriptionEditTxt;
    String idTxt, descriptionTxt;
    CodeViewModel codeViewModel;
    NavController navController;
    List<Code> codeList;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    private String idValidateMessage, descriptionValidateMessage;
    Context context;


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

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                onBackOrCancelBtnPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.code_add_view, null);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        codeViewModel = ViewModelProviders.of(this).get(CodeViewModel.class);
        context = getContext();
        builder.setTitle(getResources().getString(R.string.add_specialilty_title));
        builder.setView(view);
        if (code == null) {
            builder.setPositiveButton(R.string.add_txt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }

            });
        } else {
            builder.setPositiveButton(R.string.update_txt, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {

                }
            });
        }
        builder.setNegativeButton(R.string.cancel_btn_txt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onBackOrCancelBtnPressed();
            }
        });
        codeIdEditTxt = view.findViewById(R.id.code_id_et);
        codeDescriptionEditTxt = view.findViewById(R.id.desc_et);
        updateViewWithData();
        dialog = builder.create();
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        Button button = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        if(button.getText().toString().equals(getResources().getString(R.string.add_txt))){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    code = new Code();
                    getDataFromUi();
                    if (isDataValid()) {
                        fillCodeObjectWithUiData();
                        codeViewModel.addNewCode(code).observe(getActivity(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                Log.i("CodeFragment", "addNewCode message " + s);
                                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                                if (s.contains("success"))
                                    onButtonPressed();
                            }
                        });
                    }
                }
            });
        }
        else if (button.getText().toString().equals(getResources().getString(R.string.update_txt))){
            getDataFromUi();
            if (isDataValid()) {
                fillCodeObjectWithUiData();
                codeViewModel.updateCode(code).observe(getActivity(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s != null)
                            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        Log.i("CodeFragment", "updateCode message " + s);
                        if (s.contains("successfully"))
                            onButtonPressed();

                    }
                });
            }
        }
    }

    private void getDataFromUi() {
        idTxt = codeIdEditTxt.getText().toString();
        descriptionTxt = codeDescriptionEditTxt.getText().toString();

    }

    private void fillCodeObjectWithUiData() {
        code.setCode(idTxt);
        code.setDescription(descriptionTxt);
        code.setSystemRequired("Y");
        code.setUserCode(idTxt);
        code.setCodeType(EnumCode.Code.STFFGRP.toString());
        code.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }

    private void updateViewWithData() {
        if (code != null) {
            codeIdEditTxt.setText(code.getCode());
            codeIdEditTxt.setEnabled(false);
            codeDescriptionEditTxt.setText(code.getDescription());
            builder.setTitle(getResources().getString(R.string.update_specialilty_title));
        } else {
            codeIdEditTxt.setText("");
            codeIdEditTxt.setEnabled(true);
            codeDescriptionEditTxt.setText("");
            builder.setTitle(getResources().getString(R.string.add_specialilty_title));

        }
    }

    public void onButtonPressed() {
        navController.navigate(R.id.codeList, null,
                new NavOptions.Builder()
                        .setPopUpTo(R.id.codeList,
                                true).build());

    }

    void onBackOrCancelBtnPressed() {
        dismiss();

    }


    private boolean isDataValid() {
        if (isIdValid(idTxt) && isDescriptionValid(descriptionTxt))
            return true;
        else
            return false;
    }

    private boolean isIdValid(String id) {
        idValidateMessage = codeViewModel.validateId(id);
        if (idValidateMessage.isEmpty())
            return true;
        else {
            codeIdEditTxt.setError(idValidateMessage);
            requestFocus(codeIdEditTxt);
            return false;
        }

    }

    private boolean isDescriptionValid(String description) {
        descriptionValidateMessage = codeViewModel.validateDescription(description);
        if (descriptionValidateMessage.isEmpty())

            return true;
        else {
            codeDescriptionEditTxt.setError(descriptionValidateMessage);
            requestFocus(codeDescriptionEditTxt);
            return false;
        }

    }

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
