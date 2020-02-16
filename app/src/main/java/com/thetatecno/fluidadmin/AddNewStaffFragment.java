package com.thetatecno.fluidadmin;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.utils.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddNewStaffFragment extends Fragment {
    private static String TAG = "AddStaff";
    private static String ARG_CODE_TYPE = "codeType";
    private static String ARG_STAFF = "staff";
    private String codeType;
    EditText idTxt;
    EditText firstNameTxt;
    EditText lastNameTxt;
    EditText emailTxt;
    EditText phoneTxt;
    RadioGroup genderRadioGroup;
    Button addBtn;
    Button cancelBtn;

    Staff staff;
    enum Gender {
        F,
        M
    }
    public AddNewStaffFragment() {
        // Required empty public constructor
    }

    public static AddNewStaffFragment newInstance(Staff staff, String codeType) {
        AddNewStaffFragment fragment = new AddNewStaffFragment();
        Log.i(TAG, "new Instance method");
        Bundle args = new Bundle();
        args.putString(ARG_CODE_TYPE, codeType);
        args.putSerializable(ARG_STAFF, staff);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            codeType = getArguments().getString(ARG_CODE_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_staff, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        idTxt = view.findViewById(R.id.staffId);
        firstNameTxt = view.findViewById(R.id.first_name_edt_txt);
        lastNameTxt = view.findViewById(R.id.family_name_edt_txt);
        emailTxt = view.findViewById(R.id.email_txt);
        phoneTxt = view.findViewById(R.id.mobile_num_Edt_txt);
        genderRadioGroup = view.findViewById(R.id.genderRadioGroup);
        updateData();
        addBtn = view.findViewById(R.id.addOrUpdateBtn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void updateData() {
        if (staff != null) {
          firstNameTxt.setText(staff.getFirstName());
          lastNameTxt.setText(staff.getFamilyName());
          emailTxt.setText(staff.getEmail());
          if(staff.getGender() == Gender.F.toString())
          genderRadioGroup.check(R.id.femaleRadioButton);
          else if(staff.getGender().equals(Gender.M.toString()))
              genderRadioGroup.check(R.id.maleRadioButton);
          addBtn.setText(getResources().getString(R.string.update_txt));

        }
        else {
            firstNameTxt.setText("");
            lastNameTxt.setText("");
            emailTxt.setText("");
            addBtn.setText(getResources().getString(R.string.add_txt));
        }
    }
}
