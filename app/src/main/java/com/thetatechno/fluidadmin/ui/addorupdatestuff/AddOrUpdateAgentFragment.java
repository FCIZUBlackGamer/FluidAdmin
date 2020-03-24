package com.thetatechno.fluidadmin.ui.addorupdatestuff;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.ui.HomeActivity;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.util.List;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_STAFF;


public class AddOrUpdateAgentFragment extends Fragment implements View.OnClickListener {
    TextInputEditText agentIdEditTxt;
    TextInputEditText agentFirstNameEditTxt;
    TextInputEditText agentLastNameEditTxt;
    TextInputEditText agentEmailEditTxt;
    TextInputEditText agentNumberEditTxt;
    TextInputLayout agentIdInputLayout, agentFirstNameInputLayout, agentFamilyNameInputLayout, agentEmailInputLayout, agentNumberInputLayout;
    RadioGroup genderRadioGroup;
    Button addBtn;
    Button cancelBtn;
    ImageView addProfileImg;
    AddOrUpdateViewModel addOrUpdateViewModel;
    boolean isStaffHasData;
    Staff staff;
    List<Staff> agentList;
    NavController navController;
    String addNewAgentMessage;
    String updateAgentMessage;
    String idTxt, firstNameTxt, lastNameTxt, emailTxt, phoneTxt;
    String idValidateMessage, firstNameValidateMessage, lastNameValidateMessage, emailValidateMessage, phoneValidateMessage;
    private static String TAG = "AddStaff";

    public AddOrUpdateAgentFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            staff = (Staff) getArguments().getSerializable(ARG_STAFF);
            agentList = (List<Staff>) getArguments().getSerializable("agentList");
        }
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                onCancelOrBackButtonPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    void onCancelOrBackButtonPressed() {
        navController.popBackStack();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_or_update_agent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiateView(view);
        addOrUpdateViewModel = ViewModelProviders.of(this).get(AddOrUpdateViewModel.class);
        updateData();
        if (staff == null || staff.getImageLink().isEmpty()) {

            genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.agentMaleRadioButton:
                            addProfileImg.setImageResource(R.drawable.man);
                            break;
                        case R.id.agentFemaleRadioButton:
                            addProfileImg.setImageResource(R.drawable.ic_girl);

                    }
                }
            });
        }

        agentIdEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isIdValid(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isIdValid(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                isIdValid(s.toString());
            }
        });
        agentFirstNameEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isFirstNameValid(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                isFirstNameValid(s.toString());

            }
        });
        agentLastNameEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isLastNameValid(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                isLastNameValid(s.toString());

            }
        });
        agentEmailEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isEmailValid(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                isEmailValid(s.toString());
            }
        });
        agentNumberEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isPhoneValid(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                isPhoneValid(s.toString());

            }
        });
        addBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);

    }

    private void initiateView(View view) {
        agentIdEditTxt = view.findViewById(R.id.agentIdEdtTxt);
        agentFirstNameEditTxt = view.findViewById(R.id.agentFirstNameEdtTxt);
        agentLastNameEditTxt = view.findViewById(R.id.agentFamilyNameEdtTxt);
        agentEmailEditTxt = view.findViewById(R.id.agentEmailEditTxt);
        agentNumberEditTxt = view.findViewById(R.id.agentMobileEdtTxt);
        agentIdInputLayout = view.findViewById(R.id.agentIdTxtInputLayout);
        agentFirstNameInputLayout = view.findViewById(R.id.agentFirstNameInputLayout);
        agentFamilyNameInputLayout = view.findViewById(R.id.agentFamilyNameInputLayout);
        agentEmailInputLayout = view.findViewById(R.id.agentEmailInputLayout);
        agentNumberInputLayout = view.findViewById(R.id.agentMobileInputLayout);
        genderRadioGroup = view.findViewById(R.id.agentGenderRadioGroup);
        addBtn = view.findViewById(R.id.addOrUpdateAgentBtn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        addProfileImg = view.findViewById(R.id.addProfileImg);
    }

    private void getDataFromUi() {
        staff.setStaffId(agentIdEditTxt.getText().toString());
        staff.setFirstName(agentFirstNameEditTxt.getText().toString());
        staff.setFamilyName(agentLastNameEditTxt.getText().toString());
        staff.setStaffId(agentIdEditTxt.getText().toString());
        int id = genderRadioGroup.getCheckedRadioButtonId();
        if (id == R.id.agentMaleRadioButton)
            staff.setGender(EnumCode.Gender.M.toString());
        else if (id == R.id.agentFemaleRadioButton)
            staff.setGender(EnumCode.Gender.F.toString());
        staff.setEmail(agentEmailEditTxt.getText().toString());
        staff.setMobileNumber(agentNumberEditTxt.getText().toString());
        staff.setTypeCode(EnumCode.StaffTypeCode.DSPTCHR.toString());
        staff.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
    }

    private void onAddOrUpdateSuccessfully() {

        EspressoTestingIdlingResource.increment();

        navController.navigate(R.id.agentList, null,
                new NavOptions.Builder()
                        .setPopUpTo(R.id.agentList,
                                true).build());
        EspressoTestingIdlingResource.decrement();

    }


    private void updateData() {
        if (staff != null) {
            agentIdEditTxt.setText(staff.getStaffId());
            agentIdEditTxt.setEnabled(false);
            agentFirstNameEditTxt.setText(staff.getFirstName());
            agentLastNameEditTxt.setText(staff.getFamilyName());
            agentEmailEditTxt.setText(staff.getEmail());
            agentNumberEditTxt.setText(staff.getMobileNumber());
            if (staff.getGender() == EnumCode.Gender.F.toString())
                genderRadioGroup.check(R.id.agentFemaleRadioButton);
            else if (staff.getGender().equals(EnumCode.Gender.M.toString()))
                genderRadioGroup.check(R.id.agentMaleRadioButton);
            addBtn.setHint(getResources().getString(R.string.update_txt));

            isStaffHasData = true;
            if (!staff.getImageLink().isEmpty()) {
                Glide.with(this).load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + staff.getImageLink())
                        .circleCrop()
                        .placeholder(R.drawable.ic_girl)
                        .into(addProfileImg);
            } else {
                if (!staff.getGender().isEmpty()) {
                    if (staff.getGender().equals(EnumCode.Gender.M.toString())) {
                        addProfileImg.setImageResource(R.drawable.man);
                    } else if (staff.getGender().equals(EnumCode.Gender.F.toString())) {
                        addProfileImg.setImageResource(R.drawable.ic_girl);
                    }
                } else {
                    addProfileImg.setImageResource(R.drawable.ic_girl);
                }

            }
            ((HomeActivity) getActivity()).getSupportActionBar().setTitle("update agent");

        } else {

            staff = new Staff();
            isStaffHasData = false;
            agentFirstNameEditTxt.setText("");
            agentIdEditTxt.setEnabled(true);
            agentLastNameEditTxt.setText("");
            agentEmailEditTxt.setText("");
            addBtn.setHint(getResources().getString(R.string.add_txt));
            addProfileImg.setImageResource(R.drawable.ic_girl);
            ((HomeActivity) getActivity()).getSupportActionBar().setTitle("add agent");

        }
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.addOrUpdateAgentBtn:
                EspressoTestingIdlingResource.increment();
                idTxt = agentIdEditTxt.getText().toString();
                firstNameTxt = agentFirstNameEditTxt.getText().toString();
                lastNameTxt = agentLastNameEditTxt.getText().toString();
                emailTxt = agentEmailEditTxt.getText().toString();
                phoneTxt = agentNumberEditTxt.getText().toString();
                if (isDataValid(idTxt, firstNameTxt, lastNameTxt, emailTxt, phoneTxt)) {
                    getDataFromUi();
                    if (!isStaffHasData) {
                        if (addNewAgentMessage == null)
                            addNewAgent();
                        else {
                            addOrUpdateViewModel.addNewAgent(staff);
                        }
                    } else {
                        if (updateAgentMessage == null)
                            updateAgent();
                        else
                            addOrUpdateViewModel.updateAgent(staff);
                    }
                    EspressoTestingIdlingResource.decrement();

                }
                break;
            case R.id.cancel_btn:
                onCancelOrBackButtonPressed();
                break;
        }
    }

    private boolean isDataValid(String idTxt, String firstNameTxt, String lastNameTxt, String email, String phone) {
        if (isIdValid(idTxt) && isFirstNameValid(firstNameTxt) && isLastNameValid(lastNameTxt) && isEmailValid(email) && isPhoneValid(phone))
            return true;
        else
            return false;
    }

    private boolean isIdValid(String id) {
        idValidateMessage = addOrUpdateViewModel.validateId(id);
        if (idValidateMessage.isEmpty()) {
            agentIdInputLayout.setErrorEnabled(false);
            return true;
        } else {
            agentIdInputLayout.setError(idValidateMessage);
            agentIdInputLayout.setErrorEnabled(true);

            return false;
        }

    }

    private boolean isFirstNameValid(String firstName) {
        firstNameValidateMessage = addOrUpdateViewModel.validateFirstName(firstName);
        if (firstNameValidateMessage.isEmpty()) {
            agentFirstNameInputLayout.setErrorEnabled(false);
            return true;
        } else {
            agentFirstNameInputLayout.setError(firstNameValidateMessage);
            agentIdInputLayout.setErrorEnabled(true);
            return false;
        }

    }

    private boolean isLastNameValid(String lastName) {
        lastNameValidateMessage = addOrUpdateViewModel.validateFamilyName(lastName);
        if (lastNameValidateMessage.isEmpty()) {
            agentFamilyNameInputLayout.setErrorEnabled(false);
            return true;
        } else {
            agentFamilyNameInputLayout.setError(lastNameValidateMessage);
            agentFamilyNameInputLayout.setErrorEnabled(true);
            return false;
        }

    }

    private boolean isEmailValid(String email) {
        emailValidateMessage = addOrUpdateViewModel.validateEmail(email);
        if (emailValidateMessage.isEmpty()) {
            agentEmailInputLayout.setErrorEnabled(false);
            return true;
        } else {
            agentEmailInputLayout.setError(emailValidateMessage);
            agentEmailInputLayout.setErrorEnabled(true);

            return false;
        }
    }

    private boolean isPhoneValid(String phone) {
        phoneValidateMessage = addOrUpdateViewModel.validatePhoneNumber(phone);
        if (phoneValidateMessage.isEmpty()) {
            agentNumberInputLayout.setErrorEnabled(false);

            return true;
        } else {
            agentNumberInputLayout.setErrorEnabled(true);
            agentNumberInputLayout.setError(phoneValidateMessage);

            return false;
        }
    }

    private void addNewAgent() {

        addOrUpdateViewModel.addNewAgent(staff).observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                addNewAgentMessage = s;
                EspressoTestingIdlingResource.increment();
                Log.i("AddOrUpdate", "add agent message" + addNewAgentMessage);
                if (addNewAgentMessage.contains("success")) {

                    onAddOrUpdateSuccessfully();
                    Toast.makeText(getContext(), addNewAgentMessage, Toast.LENGTH_SHORT).show();


                } else if (addNewAgentMessage.contains("Failed")) {

                    Toast.makeText(getActivity(), addNewAgentMessage, Toast.LENGTH_SHORT).show();
                }
                EspressoTestingIdlingResource.decrement();
            }
        });

    }

    private void updateAgent() {
        addOrUpdateViewModel.updateAgent(staff).observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                updateAgentMessage = s;
                Log.i("AddOrUpdate", "Update agent message" + s);
                Toast.makeText(getContext(), updateAgentMessage, Toast.LENGTH_SHORT).show();
                if (updateAgentMessage.contains("success")) {
                    onAddOrUpdateSuccessfully();
                }
            }
        });
    }

    private void hideKeyboard(Context context, View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
