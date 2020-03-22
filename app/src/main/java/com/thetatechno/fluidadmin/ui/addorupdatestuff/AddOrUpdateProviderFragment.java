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
public class AddOrUpdateProviderFragment extends Fragment {
    private static String TAG = "AddStaff";
    EditText idEdtTxt;
    EditText providerfirstNameEditTxt;
    EditText providerLastNameEditTxt;
    EditText providerEmailEditTxt;
    EditText providerMobileEditTxt;
    RadioGroup genderRadioGroup;
    Button addBtn;
    Button cancelBtn;
    AddOrUpdateProviderViewModel addOrUpdateViewModel;
    boolean isStaffHasData;
    Staff staff;
    Spinner specialitySpinner;
    NavController navController;
    String idTxt, firstNameTxt, lastNameTxt, specialityTxt;
    String idValidateMessage, firstNameValidateMessage, lastNameValidateMessage, specialityValidateMessage, emailValidateMessage, phoneValidateMessage;
    ArrayList<String> specialitiesList;
    ImageView addProfileImage;

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
        idEdtTxt = view.findViewById(R.id.providerIdEdtTxt);
        providerfirstNameEditTxt = view.findViewById(R.id.providerFirstNameEdtTxt);
        providerLastNameEditTxt = view.findViewById(R.id.providerLastNameEdtTxt);
        providerEmailEditTxt = view.findViewById(R.id.providerEmailEditTxt);
        providerMobileEditTxt = view.findViewById(R.id.providerMobileEdtTxt);
        genderRadioGroup = view.findViewById(R.id.providerGenderRadioGroup);
        addBtn = view.findViewById(R.id.addOrUpdateProviderBtn);
        cancelBtn = view.findViewById(R.id.cancelAddOrUpdateProviderBtn);
        addProfileImage = view.findViewById(R.id.addProfileImg);
        specialitySpinner = view.findViewById(R.id.specialitySpinner);
        addOrUpdateViewModel = ViewModelProviders.of(this).get(AddOrUpdateProviderViewModel.class);
        getSpecialitiesList();
        updateData();

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
                        EspressoTestingIdlingResource.increment();
                        addOrUpdateViewModel.updateProvider(staff, specialitySpinner.getSelectedItem().toString()).observe(getActivity(), new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                Log.i(TAG, "Update staff message" + s);
                                if (s.contains("success")) {
                                    EspressoTestingIdlingResource.increment();
                                    onAddOrUpdateData();
                                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                                    EspressoTestingIdlingResource.decrement();

                                }

                            }
                        });
                        EspressoTestingIdlingResource.decrement();
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

    private boolean isDataValid(String idTxt, String firstNameTxt, String lastNameTxt, String specialityTxt, String email, String phone) {
        if (isIdValid(idTxt) && isFirstNameValid(firstNameTxt) && isLastNameValid(lastNameTxt) && isSpecialitySelected(specialityTxt) && isEmailValid(email) && isPhoneValid(phone))
            return true;
        else
            return false;
    }

    private boolean isIdValid(String id) {
        idValidateMessage = addOrUpdateViewModel.validateId(id);
        if (idValidateMessage.isEmpty())
            return true;
        else {
            idEdtTxt.setError(idValidateMessage);
            requestFocus(idEdtTxt);

            return false;
        }

    }

    private boolean isFirstNameValid(String firstName) {
        firstNameValidateMessage = addOrUpdateViewModel.validateFirstName(firstName);
        if (firstNameValidateMessage.isEmpty())

            return true;
        else {
            providerfirstNameEditTxt.setError(firstNameValidateMessage);
            requestFocus(providerfirstNameEditTxt);

            return false;
        }

    }

    private boolean isLastNameValid(String lastName) {
        lastNameValidateMessage = addOrUpdateViewModel.validateFamilyName(lastName);
        if (lastNameValidateMessage.isEmpty())
            return true;
        else {
            providerLastNameEditTxt.setError(lastNameValidateMessage);
            requestFocus(providerLastNameEditTxt);
            return false;
        }

    }

    private boolean isEmailValid(String email) {
        emailValidateMessage = addOrUpdateViewModel.validateEmail(email);
        if (emailValidateMessage.isEmpty())
            return true;
        else {
            providerEmailEditTxt.setError(emailValidateMessage);
            requestFocus(providerEmailEditTxt);
            return false;
        }
    }

    private boolean isPhoneValid(String phone) {
        phoneValidateMessage = addOrUpdateViewModel.validatePhoneNumber(phone);
        if (phoneValidateMessage.isEmpty())
            return true;
        else {
            providerMobileEditTxt.setError(phoneValidateMessage);
            requestFocus(providerMobileEditTxt);
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

    public void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
                        .into(addProfileImage);
            } else {
                if (!staff.getGender().isEmpty()) {
                    if (staff.getGender().equals(EnumCode.Gender.M.toString())) {
                        addProfileImage.setImageResource(R.drawable.man);
                    } else if (staff.getGender().equals(EnumCode.Gender.F.toString())) {
                        addProfileImage.setImageResource(R.drawable.ic_girl);
                    }
                } else {
                    addProfileImage.setImageResource(R.drawable.man);
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
            addProfileImage.setImageResource(R.drawable.man);

        }
    }


}
