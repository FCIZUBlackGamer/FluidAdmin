package com.thetatechno.fluidadmin.ui.addOrUpdateBranch;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.AddOrUpdateStatusResponse;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.ui.HomeActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddOrUpdateBranch extends Fragment implements TextWatcher, View.OnClickListener {
    private Branch branch;
    private static String ARG_BRANCH = "branch";
    private TextInputEditText branchDescriptionEditTxt;
    private TextInputEditText branchEmailEditTxt;
    private TextInputEditText branchAddressEditTxt;
    private TextInputEditText branchPhoneEditTxt;
    private TextInputLayout branchDescriptionInputLayout, branchEmailInputLayout, branchAddressInputLayout, branchPhoneInputLayout;
    private NavController navController;
    private Button cancelBtn, addOrUpdateBranchBtn;
    private boolean isBranchHasData;
    private AddOrUpdateBranchViewModel viewModel;
    private AddOrUpdateStatusResponse addNewBranchResponse;
    private AddOrUpdateStatusResponse updateBranchResponse;
    private String descriptionValidateMessage, addressValidateMessage, emailValidateMessage, phoneValidateMessage;

    public AddOrUpdateBranch() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            branch = (Branch) getArguments().getSerializable(ARG_BRANCH);
        }
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                onCancelOrBackButtonPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    private void onCancelOrBackButtonPressed() {
        navController.popBackStack();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        return inflater.inflate(R.layout.fragment_add_or_update_branch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(AddOrUpdateBranchViewModel.class);
        initiateView(view);
        updateData();
        branchDescriptionEditTxt.addTextChangedListener(this);
        branchEmailEditTxt.addTextChangedListener(this);
        branchPhoneEditTxt.addTextChangedListener(this);
        branchAddressEditTxt.addTextChangedListener(this);
        addOrUpdateBranchBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);


    }


    private void initiateView(View view) {
        branchEmailEditTxt = view.findViewById(R.id.branchEmailEdtTxt);
        branchAddressEditTxt = view.findViewById(R.id.addressEdtTxt);
        branchDescriptionEditTxt = view.findViewById(R.id.branch_description_edit_txt);
        branchPhoneEditTxt = view.findViewById(R.id.branchMobileEdtTxt);
        addOrUpdateBranchBtn = view.findViewById(R.id.addOrUpdateBranchBtn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        branchDescriptionInputLayout = view.findViewById(R.id.branch_description_txt_input_layout);
        branchAddressInputLayout = view.findViewById(R.id.addressInputLayout);
        branchEmailInputLayout = view.findViewById(R.id.branchEmailInputLayout);
        branchPhoneInputLayout = view.findViewById(R.id.branchPhoneInputLayout);
    }

    private void updateData() {
        if (branch != null) {
            branchDescriptionEditTxt.setText(branch.getDescription());
            branchAddressEditTxt.setText(branch.getAddress());
            branchEmailEditTxt.setText(branch.getEmail());
            branchPhoneEditTxt.setText(branch.getMobileNumber());
            isBranchHasData = true;
            addOrUpdateBranchBtn.setHint(getResources().getString(R.string.update_txt));
            ((HomeActivity) getActivity()).getSupportActionBar().setTitle("update branch");

        } else {

            branch = new Branch();
            isBranchHasData = false;
            branchDescriptionEditTxt.setText("");
            branchAddressEditTxt.setEnabled(true);
            branchEmailEditTxt.setText("");
            branchPhoneEditTxt.setText("");
            ((HomeActivity) getActivity()).getSupportActionBar().setTitle("add branch");
            addOrUpdateBranchBtn.setHint(getResources().getString(R.string.add_txt));

        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        branchPhoneInputLayout.setErrorEnabled(false);
        branchEmailInputLayout.setErrorEnabled(false);
        branchAddressInputLayout.setErrorEnabled(false);
        branchDescriptionInputLayout.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addOrUpdateBranchBtn:
                EspressoTestingIdlingResource.increment();

                if (isDataValid()) {
                    if (!isBranchHasData) {
                        if (addNewBranchResponse == null)
                            addNewBranch();
                        else {
                            viewModel.addNewBranch(branchDescriptionEditTxt.getText().toString()
                                    , branchAddressEditTxt.getText().toString(),
                                    branchEmailEditTxt.getText().toString(),
                                    branchPhoneEditTxt.getText().toString());
                        }
                    } else {
                        if (updateBranchResponse == null)
                            updateBranch();
                        else
                            viewModel.updateBranch(branch,branchDescriptionEditTxt.getText().toString(),
                                    branchAddressEditTxt.getText().toString(),
                                    branchEmailEditTxt.getText().toString(),
                                    branchPhoneEditTxt.getText().toString());
                    }
                    EspressoTestingIdlingResource.decrement();

                }
                break;
            case R.id.cancel_btn:
                onCancelOrBackButtonPressed();
                break;

        }

    }

    private boolean isDataValid() {
        if (isDescriptionValid(branchDescriptionEditTxt.getText().toString()) &&
                isAddressValid(branchAddressEditTxt.getText().toString()) &&
                isEmailValid(branchEmailEditTxt.getText().toString()) && isPhoneValid(branchPhoneEditTxt.getText().toString()))
            return true;
        else
            return false;

    }

    private boolean isEmailValid(String email) {
        emailValidateMessage = viewModel.validateEmail(email);
        if (emailValidateMessage.isEmpty()) {
            branchEmailInputLayout.setErrorEnabled(false);
            return true;
        } else {
            branchEmailInputLayout.setError(emailValidateMessage);
            branchEmailInputLayout.setErrorEnabled(true);

            return false;
        }
    }

    private boolean isPhoneValid(String phone) {
        phoneValidateMessage = viewModel.validatePhoneNumber(phone);
        if (phoneValidateMessage.isEmpty()) {
            branchPhoneInputLayout.setErrorEnabled(false);
            return true;
        } else {
            branchPhoneInputLayout.setErrorEnabled(true);
            branchPhoneInputLayout.setError(phoneValidateMessage);

            return false;
        }
    }

    private boolean isAddressValid(String address) {
        addressValidateMessage = viewModel.validateAddress(address);
        if (addressValidateMessage.isEmpty()) {
            branchAddressInputLayout.setErrorEnabled(false);
            return true;
        } else {
            branchAddressInputLayout.setErrorEnabled(true);
            branchAddressInputLayout.setError(phoneValidateMessage);

            return false;
        }
    }

    private boolean isDescriptionValid(String description) {
        descriptionValidateMessage = viewModel.validateDescription(description);
        if (descriptionValidateMessage.isEmpty()) {
            branchDescriptionInputLayout.setErrorEnabled(false);
            return true;
        } else {
            branchDescriptionInputLayout.setErrorEnabled(true);
            branchDescriptionInputLayout.setError(phoneValidateMessage);

            return false;
        }
    }

    private void addNewBranch() {

        viewModel.addNewBranch(branchDescriptionEditTxt.getText().toString(),
                branchAddressEditTxt.getText().toString(),
                branchEmailEditTxt.getText().toString(),
                branchPhoneEditTxt.getText().toString()
        ).observe(getActivity(), new Observer<AddOrUpdateStatusResponse>() {
            @Override
            public void onChanged(AddOrUpdateStatusResponse s) {
                if (s != null) {
                    addNewBranchResponse = s;
                    EspressoTestingIdlingResource.increment();
                    if (addNewBranchResponse.getStatus() > 0) {
                        onAddOrUpdateSuccessfully();
                        Toast.makeText(getContext(), "added successfully", Toast.LENGTH_SHORT).show();
                    } else if (addNewBranchResponse.getStatus() == -9999) {

                        Toast.makeText(getActivity(), "null parameter", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "other error " + addNewBranchResponse.getStatus(), Toast.LENGTH_SHORT).show();

                    }
                    EspressoTestingIdlingResource.decrement();
                }
            }
        });

    }

    private void updateBranch() {
        viewModel.updateBranch(branch,branchDescriptionEditTxt.getText().toString(),
                branchAddressEditTxt.getText().toString(),
                branchEmailEditTxt.getText().toString(),
                branchPhoneEditTxt.getText().toString()).observe(getActivity(), new Observer<AddOrUpdateStatusResponse>() {
            @Override
            public void onChanged(AddOrUpdateStatusResponse s) {
                EspressoTestingIdlingResource.increment();
                updateBranchResponse = s;
                Log.i("AddOrUpdate", "Update agent status" + s.getStatus());
                if (updateBranchResponse.getStatus() >= 0 ) {
                    onAddOrUpdateSuccessfully();
                    Toast.makeText(getContext(), "added successfully", Toast.LENGTH_SHORT).show();
                }
                EspressoTestingIdlingResource.decrement();

            }
        });
    }

    private void onAddOrUpdateSuccessfully() {

        EspressoTestingIdlingResource.increment();

        navController.navigate(R.id.action_addOrUpdateBranch_to_branches, null,
                new NavOptions.Builder()
                        .setPopUpTo(R.id.branches,
                                true).build());
        EspressoTestingIdlingResource.decrement();

    }

}
