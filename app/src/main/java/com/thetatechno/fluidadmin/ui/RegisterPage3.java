package com.thetatechno.fluidadmin.ui;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.databinding.FragmentRegisterPage3Binding;
import com.thetatechno.fluidadmin.model.ClientModelForRegister;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.model.code_model.Code;
import com.thetatechno.fluidadmin.model.code_model.CodeList;
import com.thetatechno.fluidadmin.utils.EnumCode;

import java.util.ArrayList;
import java.util.Calendar;


public class RegisterPage3 extends Fragment implements TextWatcher {
    private RegisterViewModel viewModel;
    private FragmentRegisterPage3Binding binding;
    private ArrayList<Code> nationalityList;
    private ArrayList<Code> idTypes;
    private ClientModelForRegister customer = new ClientModelForRegister();
    private static final int NULL_PARAMETER = -1;
    private static final int USER_NOT_EXIST = -2;
    private String gender;
    private String idTypeCode;
    private String nationalityCode;
    private NavController navController;
    private Observer<CodeList> nationalityListObserver = new Observer<CodeList>() {
        @Override
        public void onChanged(CodeList codeList) {
            if (codeList != null) {
                if (codeList.getCodeList() != null) {
                    nationalityList = (ArrayList<Code>) codeList.getCodeList();
                    ArrayAdapter<Code> nationalitiesAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, nationalityList);
                    binding.nationalityTxtView.setAdapter(nationalitiesAdapter);

                }
            }
        }
    };
    private Observer<CodeList> idTypesObserver = new Observer<CodeList>() {
        @Override
        public void onChanged(CodeList codeList) {
            if (codeList != null) {

                if (codeList.getCodeList() != null) {
                    idTypes = (ArrayList<Code>) codeList.getCodeList();
                    ArrayAdapter<Code> identityTypesListAdapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, idTypes);
                    binding.identityTypeSpinner.setAdapter(identityTypesListAdapter);

                }
            }
        }
    };

    public RegisterPage3() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_page_3, container, false);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        navController = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        viewModel.getNationalityList().observe(getViewLifecycleOwner(), nationalityListObserver);
        viewModel.getIDTypes().observe(getViewLifecycleOwner(), idTypesObserver);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                redirectToClientList();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        binding.dateOfBirthEdtTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        binding.nationalityTxtView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                nationalityCode = nationalityList.get(position).getCode();
            }
        });
        binding.dateOfBirthEdtTxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                }
            }
        });
        binding.identityTypeSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idTypeCode = idTypes.get(position).getCode();
            }
        });
        binding.registerClientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidFirstName(binding.firstnameEditTxt.getText().toString()) && isValidFamilyName(binding.lastNameEdtTxt.getText().toString())
                        && isValidDateOfBirth() && isValidID()) {
                    Log.i("Validation", "validation true");
                    addNewPatient();

                }
            }
        });

        if (binding.genderGroup.getCheckedRadioButtonId() == R.id.rbFemale) {
            gender = EnumCode.Gender.F.toString();
        } else if (binding.genderGroup.getCheckedRadioButtonId() == R.id.rbMale) {
            gender = EnumCode.Gender.M.toString();
        }
        return binding.getRoot();

    }

    private void addNewPatient() {
        viewModel.addNewClient(binding.firstnameEditTxt.getText().toString(),
                binding.middleNameEdtTxt.getText().toString(),
                binding.lastNameEdtTxt.getText().toString(),
                binding.emailEditTxt.getText().toString(),
                binding.phoneTextEditTxt.getText().toString(),
                binding.dateOfBirthEdtTxt.getText().toString(),
                gender, nationalityCode, idTypeCode, binding.identityNumberTxt.getText().toString()).observe(getViewLifecycleOwner(), new Observer<Status>() {

            @Override
            public void onChanged(Status status) {
                if (status != null) {
                    if (Integer.parseInt(status.getStatus()) > 0) {
                        Toast.makeText(getContext(), R.string.user_created_sueccfully, Toast.LENGTH_SHORT).show();
                        redirectToClientList();
                    } else if (Integer.parseInt(status.getStatus()) == NULL_PARAMETER) {
                        Toast.makeText(getContext(), R.string.null_parameter_txt, Toast.LENGTH_SHORT).show();

                    }else if (Integer.parseInt(status.getStatus()) == USER_NOT_EXIST) {
                        Toast.makeText(getContext(), R.string.user_not_exist, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), getString(R.string.error_with_status_code_txt) + status.getStatus(), Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private void redirectToClientList() {
navController.popBackStack();
    }

    private void showDatePicker() {
        final Calendar newCalendar = Calendar.getInstance();
        @SuppressLint("SetTextI18n") DatePickerDialog StartTime = new DatePickerDialog(getContext(), R.style.DialogTheme, (DatePickerDialog.OnDateSetListener) (view, year, monthOfYear, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year, monthOfYear, dayOfMonth);
            binding.dateOfBirthEdtTxt.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        StartTime.show();
    }

    private boolean isValidFirstName(String firstName) {
        String firstNameValidationTxt = viewModel.validateFirstName(firstName);
        if (!firstNameValidationTxt.isEmpty()) {
            binding.firstNameTextInput.setError(firstNameValidationTxt);
            binding.firstNameTextInput.setErrorEnabled(true);
            return false;
        } else {
            binding.firstNameTextInput.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isValidFamilyName(String familyName) {
        String familyNameValidationTxt = viewModel.validateFamilyName(familyName);
        if (!familyNameValidationTxt.isEmpty()) {
            binding.familyNameTextInput.setError(familyNameValidationTxt);
            binding.familyNameTextInput.setErrorEnabled(true);

            return false;
        } else {
            binding.familyNameTextInput.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isMiddleNameValid() {
        String middleNameValidateMessage = viewModel.validateMiddleName(binding.middleNameEdtTxt.getText().toString());
        if (middleNameValidateMessage.isEmpty()) {
            binding.middleNameInputLayout.setErrorEnabled(false);
            return true;
        } else {
            binding.middleNameInputLayout.setError(middleNameValidateMessage);
            binding.middleNameInputLayout.setErrorEnabled(true);
            return false;
        }

    }

    private boolean isValidDateOfBirth() {
        String validateDateOfBirthString = viewModel.validateDateOfBirth(binding.dateOfBirthEdtTxt.getText().toString());
        if (validateDateOfBirthString.isEmpty()) {
            binding.middleNameInputLayout.setErrorEnabled(false);
            return true;
        } else {
            binding.middleNameInputLayout.setError(validateDateOfBirthString);
            binding.middleNameInputLayout.setErrorEnabled(true);
            return false;
        }
    }

    private boolean isValidID() {
        String type = binding.identityTypeSpinner.getText().toString();
        if (type.trim().isEmpty()) {
            return true;
        } else {
            if (binding.identityNumberTxt.getText().toString().trim().isEmpty()) {
                binding.identityNumberTxtInput.setErrorEnabled(true);
                binding.identityNumberTxtInput.setError("identity number is required field");
                return false;
            } else {
                binding.identityNumberTxtInput.setErrorEnabled(false);
                return true;

            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        binding.firstNameTextInput.setErrorEnabled(false);
        binding.middleNameInputLayout.setErrorEnabled(false);
        binding.familyNameTextInput.setErrorEnabled(false);
        binding.dateOfBirthInputLayout.setErrorEnabled(false);
        binding.nationalityInputLayout.setErrorEnabled(false);
        binding.identityNumberTxtInput.setErrorEnabled(false);
        binding.identityTypeInputLayout.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}


