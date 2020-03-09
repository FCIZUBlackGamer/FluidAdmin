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
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.ui.HomeActivity;
import com.thetatechno.fluidadmin.utils.App;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.EnumCode;
import com.thetatechno.fluidadmin.utils.PreferenceController;

import java.io.Serializable;
import java.util.List;

import static com.thetatechno.fluidadmin.utils.Constants.ARG_STAFF;


public class AddOrUpdateAgentFragment extends Fragment {
    EditText idTxt;
    EditText firstNameTxt;
    EditText lastNameTxt;
    EditText emailTxt;
    EditText phoneTxt;
    RadioGroup genderRadioGroup;
    Button addBtn;
    Button cancelBtn;
    ImageView addProfileImg;
    AddOrUpdateViewModel addOrUpdateViewModel;
    boolean isStaffHasData;
    Staff staff;
    List<Staff> agentList;
    NavController navController;
    Bundle bundle;

    private static String TAG = "AddStaff";

    public AddOrUpdateAgentFragment() {

    }

    public static AddOrUpdateAgentFragment newInstance(Staff staff) {
        AddOrUpdateAgentFragment fragment = new AddOrUpdateAgentFragment();
        Log.i(TAG, "new Instance method");
        Bundle args = new Bundle();
        args.putSerializable(ARG_STAFF, staff);
        fragment.setArguments(args);
        return fragment;
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

        bundle = new Bundle();
        bundle.putSerializable("type", EnumCode.UsageType.Agent);
        bundle.putSerializable("agentList", (Serializable) agentList);
        navController.navigate(R.id.action_addOrUpdateAgentFragment_to_agentListFragment);
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

        idTxt = view.findViewById(R.id.agentIdEdtTxt);
        firstNameTxt = view.findViewById(R.id.first_name_edt_txt);
        lastNameTxt = view.findViewById(R.id.family_name_edt_txt);
        emailTxt = view.findViewById(R.id.emailEditTxt);
        phoneTxt = view.findViewById(R.id.mobile_num_Edt_txt);
        genderRadioGroup = view.findViewById(R.id.genderRadioGroup);
        addBtn = view.findViewById(R.id.addOrUpdateBtn);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        addProfileImg = view.findViewById(R.id.addProfileImg);
        addOrUpdateViewModel = ViewModelProviders.of(this).get(AddOrUpdateViewModel.class);
        updateData();
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                staff.setStaffId(idTxt.getText().toString());
                staff.setFirstName(firstNameTxt.getText().toString());
                staff.setFamilyName(lastNameTxt.getText().toString());
                staff.setStaffId(idTxt.getText().toString());
                int id = genderRadioGroup.getCheckedRadioButtonId();
                if (id == R.id.maleRadioButton)
                    staff.setGender(EnumCode.Gender.M.toString());
                else if (id == R.id.femaleRadioButton)
                    staff.setGender(EnumCode.Gender.F.toString());
                staff.setEmail(emailTxt.getText().toString());
                staff.setMobileNumber(phoneTxt.getText().toString());
                staff.setTypeCode(EnumCode.StaffTypeCode.DSPTCHR.toString());
                staff.setLangId(PreferenceController.getInstance(App.getContext()).get(PreferenceController.LANGUAGE).toUpperCase());

                if (!isStaffHasData) {
                    addOrUpdateViewModel.addNewStaff(staff).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i("AddOrUpdate", "add agent message" + s);
                            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                            if (s.contains("success")) {
//                                listener.onFragmentAddOrUpdateEntity(EnumCode.UsageType.Agent);
                                navController.navigate(R.id.action_addOrUpdateAgentFragment_to_agentListFragment);

                            }


                        }
                    });
                } else {
                    addOrUpdateViewModel.updateStaff(staff).observe(getActivity(), new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            Log.i("AddOrUpdate", "Update agent message" + s);
                            Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                            if (s.contains("success"))
                                navController.navigate(R.id.action_addOrUpdateAgentFragment_to_agentListFragment);

                        }
                    });
                }
            }
        });

        if (this.getArguments() != null) {
            idTxt.setEnabled(false);
        }
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelOrBackButtonPressed();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(staff.getStaffId()!=null){
            ((HomeActivity)getActivity()).getSupportActionBar().setTitle("update agent");
        }else {

            ((HomeActivity)getActivity()).getSupportActionBar().setTitle("add agent");
        }
    }

    private void updateData() {
        if (staff != null) {

            idTxt.setText(staff.getStaffId());
            firstNameTxt.setText(staff.getFirstName());
            lastNameTxt.setText(staff.getFamilyName());
            emailTxt.setText(staff.getEmail());
            phoneTxt.setText(staff.getMobileNumber());
            if (staff.getGender() == EnumCode.Gender.F.toString())
                genderRadioGroup.check(R.id.femaleRadioButton);
            else if (staff.getGender().equals(EnumCode.Gender.M.toString()))
                genderRadioGroup.check(R.id.maleRadioButton);
            addBtn.setHint(getResources().getString(R.string.update_txt));
            isStaffHasData = true;
            if(!staff.getImageLink().isEmpty()){
                Glide.with(this).load(Constants.BASE_URL+Constants.BASE_EXTENSION_FOR_PHOTOS+staff.getImageLink())
                        .circleCrop()
                        .into(addProfileImg);
            }
            else {
                if(!staff.getGender().isEmpty()) {
                    if (staff.getGender().equals(EnumCode.Gender.M.toString())) {
                        addProfileImg.setImageResource(R.drawable.man);
                    } else if(staff.getGender().equals(EnumCode.Gender.F.toString())){
                        addProfileImg.setImageResource(R.drawable.ic_girl);
                    }
                }
                else {
                    addProfileImg.setImageResource(R.drawable.man);
                }

            }

        } else {

            staff = new Staff();
            isStaffHasData = false;
            firstNameTxt.setText("");
            lastNameTxt.setText("");
            emailTxt.setText("");
            addBtn.setHint(getResources().getString(R.string.add_txt));
            addProfileImg.setImageResource(R.drawable.man);
//            navController.getCurrentDestination().setLabel("add agent");
//           getActivity().setTitle("add agent");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
