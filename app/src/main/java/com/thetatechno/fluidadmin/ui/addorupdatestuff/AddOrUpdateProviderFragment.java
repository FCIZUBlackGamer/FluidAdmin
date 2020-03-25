package com.thetatechno.fluidadmin.ui.addorupdatestuff;


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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.List;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_STAFF;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddOrUpdateProviderFragment extends Fragment  {
    private static String TAG = "AddStaff";
    private TextInputEditText idEdtTxt;
    private   TextInputEditText providerfirstNameEditTxt;
    private   EditText providerLastNameEditTxt;
    private   EditText providerEmailEditTxt;
    private  EditText providerMobileEditTxt;
    private TextInputLayout providerIdTxtInputLayout,providerLastNameTxtInputLayout,
            providerFirstNameTextInputLayout,providerEmailTextInputLayout, providerMobileInputLayout;
    private   RadioGroup genderRadioGroup;
    private Button addBtn;
    private  Button cancelBtn;
    private  AddOrUpdateProviderViewModel addOrUpdateViewModel;
    private  boolean isStaffHasData;
    private  Staff staff;
    private  Spinner specialitySpinner;
    private  NavController navController;
    private  String idTxt, firstNameTxt, lastNameTxt, specialityTxt;
    private  String idValidateMessage, firstNameValidateMessage, lastNameValidateMessage, specialityValidateMessage, emailValidateMessage, phoneValidateMessage;
    private ArrayList<String> specialitiesList;
    private ImageView providerProfileImage;

    public AddOrUpdateProviderFragment() {
        // Required empty public constructor
    }

    public static AddOrUpdateProviderFragment newInstance(Staff staff) {
        AddOrUpdateProviderFragment fragment = new AddOrUpdateProviderFragment();
        Log.i(TAG, "new Instance method");
        Bundle args = new Bundle();
        args.putSerializable(ARG_STAFF, staff);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getSerializable(ARG_STAFF) != null) {
                staff = (Staff) getArguments().getSerializable(ARG_STAFF);
            }

        }
        navController = Navigation.findNavController((HomeActivity) getActivity(), R.id.nav_host_fragment);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                onCancelOrBackBtnPressed();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_or_update_provider, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (staff.getStaffId() != null) {
            ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Update Provider");
        } else {

            ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Add Provider");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initiateViews(view);
        addOrUpdateViewModel = ViewModelProviders.of(this).get(AddOrUpdateProviderViewModel.class);
        getSpecialitiesList();
        updateData();
        changeImageAvatar();

        idEdtTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                isIdValid(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                providerIdTxtInputLayout.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {
                isIdValid(s.toString());
            }
        });
        providerfirstNameEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                providerFirstNameTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        providerLastNameEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                providerLastNameTxtInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        providerEmailEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                providerEmailTextInputLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s){
            }
        });
        providerMobileEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                providerMobileInputLayout.setErrorEnabled(false);

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EspressoTestingIdlingResource.increment();
                idTxt = idEdtTxt.getText().toString();
                firstNameTxt = providerfirstNameEditTxt.getText().toString();
                lastNameTxt = providerLastNameEditTxt.getText().toString();
                specialityTxt = specialitySpinner.getSelectedItem().toString();
                if (isDataValid(idTxt, firstNameTxt, lastNameTxt, specialityTxt, providerEmailEditTxt.getText().toString(), providerMobileEditTxt.getText().toString())) {
                    getDataFromUi();
                    if (!isStaffHasData) {
                        EspressoTestingIdlingResource.increment();
                        addOrUpdateViewModel.addNewProvider(staff, specialitySpinner.getSelectedItem().toString()).observe(getActivity(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                Log.i(TAG, "add staff message" + s);
                                if (s.contains("success")) {
                                    EspressoTestingIdlingResource.increment();
                                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                                    onAddOrUpdateData();
                                    EspressoTestingIdlingResource.decrement();

                                }

                            }
                        });
                        EspressoTestingIdlingResource.decrement();
                    } else {

                        addOrUpdateViewModel.updateProvider(staff, specialitySpinner.getSelectedItem().toString()).observe(getActivity(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                EspressoTestingIdlingResource.increment();
                                Log.i(TAG, "Update staff message" + s);
                                if (s.contains("success")) {
                                    EspressoTestingIdlingResource.increment();
                                    onAddOrUpdateData();
                                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                                    EspressoTestingIdlingResource.decrement();

                                }
                                EspressoTestingIdlingResource.decrement();


                            }
                        });
                    }
                }
                EspressoTestingIdlingResource.decrement();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelOrBackBtnPressed();
            }
        });

    }

    private void changeImageAvatar() {
        if(staff == null || staff.getImageLink().isEmpty()){
            genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.maleRadioButton:
                            providerProfileImage.setImageResource(R.drawable.man);
                            break;
                        case R.id.femaleRadioButton:
                            providerProfileImage.setImageResource(R.drawable.ic_girl);

                    }
                }
            });
        }
    }

    private void initiateViews(View view) {
        idEdtTxt = view.findViewById(R.id.providerIdEdtTxt);
        providerIdTxtInputLayout = view.findViewById(R.id.providerIdTxtInputLayout);
        providerfirstNameEditTxt = view.findViewById(R.id.providerFirstNameEdtTxt);
        providerFirstNameTextInputLayout = view.findViewById(R.id.providerFirstNameTxtInputLayout);
        providerLastNameTxtInputLayout = view.findViewById(R.id.providerFamilyNameTxtInputLayout);
        providerLastNameEditTxt = view.findViewById(R.id.providerLastNameEdtTxt);
        providerEmailEditTxt = view.findViewById(R.id.providerEmailEditTxt);
        providerEmailTextInputLayout = view.findViewById(R.id.providerEmailTxtInputLayout);
        providerMobileEditTxt = view.findViewById(R.id.providerMobileEdtTxt);
        providerMobileInputLayout = view.findViewById(R.id.providerMobileTxtInputLayout);
        genderRadioGroup = view.findViewById(R.id.providerGenderRadioGroup);
        addBtn = view.findViewById(R.id.addOrUpdateProviderBtn);
        cancelBtn = view.findViewById(R.id.cancelAddOrUpdateProviderBtn);
        providerProfileImage = view.findViewById(R.id.addProfileImg);
        specialitySpinner = view.findViewById(R.id.specialitySpinner);

    }

    private boolean isDataValid(String idTxt, String firstNameTxt, String lastNameTxt, String specialityTxt, String email, String phone) {
        if (isIdValid(idTxt) && isFirstNameValid(firstNameTxt) && isLastNameValid(lastNameTxt) && isSpecialitySelected(specialityTxt) && isEmailValid(email) && isPhoneValid(phone))
            return true;
        else
            return false;
    }

    private boolean isIdValid(String id) {
        idValidateMessage = addOrUpdateViewModel.validateId(id);
        if (idValidateMessage.isEmpty()) {
            providerIdTxtInputLayout.setErrorEnabled(false);
            return true;
        }
        else {
            providerIdTxtInputLayout.setError(idValidateMessage);
            providerIdTxtInputLayout.setErrorEnabled(true);

            return false;
        }

    }

    private boolean isFirstNameValid(String firstName) {
        firstNameValidateMessage = addOrUpdateViewModel.validateFirstName(firstName);
        if (firstNameValidateMessage.isEmpty()) {
            providerFirstNameTextInputLayout.setErrorEnabled(false);
            return true;
        }
        else {
            providerFirstNameTextInputLayout.setError(firstNameValidateMessage);
            providerFirstNameTextInputLayout.setErrorEnabled(true);
            return false;
        }

    }

    private boolean isLastNameValid(String lastName) {
        lastNameValidateMessage = addOrUpdateViewModel.validateFamilyName(lastName);
        if (lastNameValidateMessage.isEmpty()) {
            providerLastNameTxtInputLayout.setErrorEnabled(false);
            return true;
        }
        else {
            providerLastNameTxtInputLayout.setError(lastNameValidateMessage);
            providerLastNameTxtInputLayout.setErrorEnabled(true);

            return false;
        }

    }

    private boolean isEmailValid(String email) {
        emailValidateMessage = addOrUpdateViewModel.validateEmail(email);
        if (emailValidateMessage.isEmpty()) {
            providerLastNameTxtInputLayout.setErrorEnabled(false);
            return true;
        }
        else {
            providerEmailTextInputLayout.setError(emailValidateMessage);
            providerLastNameTxtInputLayout.setErrorEnabled(true);

            return false;
        }
    }

    private boolean isPhoneValid(String phone) {
        phoneValidateMessage = addOrUpdateViewModel.validatePhoneNumber(phone);
        if (phoneValidateMessage.isEmpty()) {
            providerMobileInputLayout.setErrorEnabled(false);
            return true;
        }
        else {
            providerMobileInputLayout.setError(phoneValidateMessage);
            providerMobileInputLayout.setErrorEnabled(true);
            return false;
        }
    }

    private boolean isSpecialitySelected(String speciality) {
        specialityValidateMessage = addOrUpdateViewModel.validateSpeciality(speciality);
        if (specialityValidateMessage.isEmpty())
            return true;
        else {
            Toast.makeText(getContext(), specialityValidateMessage, Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    private void getDataFromUi() {
        staff.setStaffId(idEdtTxt.getText().toString());
        staff.setFirstName(providerfirstNameEditTxt.getText().toString());
        staff.setFamilyName(providerLastNameEditTxt.getText().toString());
        int id = genderRadioGroup.getCheckedRadioButtonId();
        if (id == R.id.maleRadioButton)
            staff.setGender(EnumCode.Gender.M.toString());
        else if (id == R.id.femaleRadioButton)
            staff.setGender(EnumCode.Gender.F.toString());
        staff.setEmail(providerEmailEditTxt.getText().toString());
        staff.setMobileNumber(providerMobileEditTxt.getText().toString());
        staff.setTypeCode(EnumCode.StaffTypeCode.PRVDR.toString());
        staff.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());
        staff.setSpecialityCode(specialitySpinner.getSelectedItem().toString());
    }

    private void getSpecialitiesList() {
        addOrUpdateViewModel.getSpecialities().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> codeList) {
                if (codeList.size() > 0) {
                    specialitiesList = (ArrayList<String>) codeList;
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(getContext(), R.layout.simple_spinner_dropdown_item, specialitiesList);
                    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);
                    specialitySpinner.setAdapter(adapter);
                    if (staff != null)
                        if (staff.getSpeciality() != null) {
                            for (int i = 0; i < specialitiesList.size(); i++) {
                                if (specialitiesList.get(i).equals(staff.getSpecialityCode())) {
                                    specialitySpinner.setSelection(i);
                                }
                            }
                        }

                }
            }
        });
    }

    private void onAddOrUpdateData() {
        EspressoTestingIdlingResource.increment();
        navController.navigate(R.id.providerList, null,
                new NavOptions.Builder()
                        .setPopUpTo(R.id.providerList,
                                true).build());
        EspressoTestingIdlingResource.decrement();

    }

    private void onCancelOrBackBtnPressed() {
        navController.popBackStack();
    }

    private void updateData() {
        if (staff != null) {
            idEdtTxt.setText(staff.getStaffId());
            idEdtTxt.setEnabled(false);
            providerfirstNameEditTxt.setText(staff.getFirstName());
            providerLastNameEditTxt.setText(staff.getFamilyName());
            providerEmailEditTxt.setText(staff.getEmail());
            if (staff.getGender() == EnumCode.Gender.F.toString())
                genderRadioGroup.check(R.id.femaleRadioButton);
            else if (staff.getGender().equals(EnumCode.Gender.M.toString()))
                genderRadioGroup.check(R.id.maleRadioButton);
            addBtn.setHint(getResources().getString(R.string.update_txt));
            isStaffHasData = true;
            if (!staff.getImageLink().isEmpty()) {
                Glide.with(this).load(Constants.BASE_URL + Constants.BASE_EXTENSION_FOR_PHOTOS + staff.getImageLink())
                        .circleCrop()
                        .placeholder(R.drawable.ic_girl)
                        .into(providerProfileImage);
            } else {
                if (!staff.getGender().isEmpty()) {
                    if (staff.getGender().equals(EnumCode.Gender.M.toString())) {
                        providerProfileImage.setImageResource(R.drawable.man);
                    } else if (staff.getGender().equals(EnumCode.Gender.F.toString())) {
                        providerProfileImage.setImageResource(R.drawable.ic_girl);
                    }
                } else {
                    providerProfileImage.setImageResource(R.drawable.man);
                }
            }

        } else {
            idEdtTxt.setEnabled(true);
            staff = new Staff();
            isStaffHasData = false;
            providerfirstNameEditTxt.setText("");
            providerLastNameEditTxt.setText("");
            providerEmailEditTxt.setText("");
            addBtn.setHint(getResources().getString(R.string.add_txt));
            providerProfileImage.setImageResource(R.drawable.man);
            genderRadioGroup.check(R.id.maleRadioButton);

        }
    }


}
