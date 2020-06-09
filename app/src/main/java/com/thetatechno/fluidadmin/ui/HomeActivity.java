package com.thetatechno.fluidadmin.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnClickedCommunicator;
import com.thetatechno.fluidadmin.listeners.OnConfirmAppointmentListener;
import com.thetatechno.fluidadmin.listeners.OnConfirmDeleteListener;
import com.thetatechno.fluidadmin.listeners.OnConfirmLinkToFacilityListener;
import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.listeners.OnItemClickedListener;
import com.thetatechno.fluidadmin.listeners.OnLinkToFacilityClickedListener;
import com.thetatechno.fluidadmin.listeners.OnOpenCancelAppointmentDialogListener;
import com.thetatechno.fluidadmin.model.Error;
import com.thetatechno.fluidadmin.model.Status;
import com.thetatechno.fluidadmin.model.session_model.Session;
import com.thetatechno.fluidadmin.model.shedule.Schedule;
import com.thetatechno.fluidadmin.model.time_slot_model.TimeSlot;
import com.thetatechno.fluidadmin.model.branches_model.Branch;
import com.thetatechno.fluidadmin.model.code_model.Code;
import com.thetatechno.fluidadmin.model.facility_model.Facilities;
import com.thetatechno.fluidadmin.model.facility_model.Facility;
import com.thetatechno.fluidadmin.model.facility_model.FacilityCodes;
import com.thetatechno.fluidadmin.model.Staff;
import com.thetatechno.fluidadmin.ui.dialogs.ConfirmDeleteDialog;
import com.thetatechno.fluidadmin.ui.dialogs.FacilitiesListDialog;
import com.thetatechno.fluidadmin.ui.facilityList.FacilityListViewModel;
import com.thetatechno.fluidadmin.utils.Constants;
import com.thetatechno.fluidadmin.utils.PreferenceController;
import com.thetatechno.fluidadmin.utils.EnumCode;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;


public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnDeleteListener, OnConfirmDeleteListener, OnLinkToFacilityClickedListener, OnConfirmLinkToFacilityListener, OnClickedCommunicator, OnConfirmAppointmentListener, OnOpenCancelAppointmentDialogListener {
    private MainViewModel mainViewModel;
    private FacilityListViewModel facilityListViewModel;
    private NavigationView navigationView;
    private ConfirmDeleteDialog confirmDeleteDialog;
    private NavController navController;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private FacilitiesListDialog facilitiesListDialog;
    private AlertDialog alertDialog;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private List<Facility> facilityList = new ArrayList<>();
    private String deleteStaffMessage = "";
    private String deleteCodeMessage = "";
    private String deleteFacilityMessage = "";
    private String deleteBranchMessage = "";
    private String deleteScheduleMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(this));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );

        setContentView(R.layout.activity_home);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        facilityListViewModel = ViewModelProviders.of(this).get(FacilityListViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.agentList, R.id.providerList, R.id.clientList,
                R.id.codeList, R.id.facility, R.id.branches, R.id.appointments, R.id.scheduleFragment,R.id.sessionFragment)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        checkOnTheCurrentLanguage();
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void checkOnTheCurrentLanguage() {

        if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ARABIC)) {
            navigationView.getMenu().findItem(R.id.language_reference).setTitle(R.string.menu_english_language);

        } else if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ENGLISH)) {
            navigationView.getMenu().findItem(R.id.language_reference).setTitle(R.string.menu_arabic_language);

        }
    }

    private void changeLanguage(String language) {

        if (!PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(language)) {
            if (PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).equals(Constants.ARABIC)) {
                PreferenceController.getInstance(this).persist(PreferenceController.LANGUAGE, Constants.ENGLISH);
            } else {
                PreferenceController.getInstance(this).persist(PreferenceController.LANGUAGE, Constants.ARABIC);
            }
            finish();
            startActivity(new Intent(this, HomeActivity.class));

        }

    }


    @Override
    public void onDeleteButtonClicked(final Object itemClicked) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_title_dialog);
        builder.setPositiveButton(R.string.delete_txt, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onOkButtonClicked(itemClicked);
            }
        });
        builder.setNegativeButton(R.string.cancel_btn_txt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public void onOkButtonClicked(final Object itemDeleted) {
        EspressoTestingIdlingResource.increment();
        if (itemDeleted instanceof Facility) {
            Log.i("Object", "facility type " + ((Facility) itemDeleted).getId());
            if (deleteFacilityMessage.isEmpty())
                deleteFacility((Facility) itemDeleted);
            else
                mainViewModel.deleteFacility((Facility) itemDeleted);


        } else if (itemDeleted instanceof Staff) {
            Log.i("Object", "staff type " + ((Staff) itemDeleted).getStaffId());
            if (deleteStaffMessage.isEmpty())
                deleteAgentOrProvider((Staff) itemDeleted);
            else
                mainViewModel.deleteAgentOrProvider((Staff) itemDeleted);
        } else if (itemDeleted instanceof Code) {
            Log.i("Object", "code type " + ((Code) itemDeleted).getCode());
            if (deleteCodeMessage.isEmpty())
                deleteCode((Code) itemDeleted);
            else
                mainViewModel.deleteCode((Code) itemDeleted);
        } else if (itemDeleted instanceof Branch) {
            Log.i("Object", "branch " + ((Branch) itemDeleted).getDescription());
            if (deleteBranchMessage.isEmpty())
                deleteBranch((Branch) itemDeleted);
            else
                mainViewModel.deleteBranch((Branch) itemDeleted);
        } else if (itemDeleted instanceof Schedule) {
            Log.i("Object", "schedule " + ((Schedule) itemDeleted).getDescription());
            if (deleteScheduleMessage.isEmpty())
                deleteSchedule((Schedule) itemDeleted);
            else
                mainViewModel.deleteSchedule((Schedule) itemDeleted);
        }
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        EspressoTestingIdlingResource.decrement();

    }

    int id;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(false);
        drawer.closeDrawers();
        id = item.getItemId();
        switch (id) {
            case R.id.language_reference:
                changeLanguage((String) item.getTitle());
                break;
            case R.id.agentList:
                navigateToAgentList();
                break;

            case R.id.providerList:
                navigateToProviderList();
                break;

            case R.id.codeList:
                navigateToCodeList();
                break;
            case R.id.clientList:
                navigateToClientList();

                break;

            case R.id.facility:
                navigateToClinicTypeList();
                break;
            case R.id.appointments:
                navigateToAppointments();
                break;
            case R.id.branches:
                navigateToBranches();
                break;
            case R.id.scheduleFragment:
                navigateToSchedules();
                break;
            case R.id.sessionFragment:
                navigateToSessions();
                break;
        }

        return true;

    }

    private void navigateToSessions() {
        navController.popBackStack();
        navController.navigate(R.id.sessionFragment);
    }

    public void getFacilities() {
//        EspressoTestingIdlingResource.increment();
        facilityListViewModel.getFacilityDataForClinicType("").observe(this, new Observer<Facilities>() {
            @Override
            public void onChanged(Facilities facilities) {
//                EspressoTestingIdlingResource.decrement();
                if (facilities.getFacilities() != null) {
                    EspressoTestingIdlingResource.increment();
                    facilityList = facilities.getFacilities();
                    checkAllFacilitiesThatLinkedToStaffAndShowDialog(staff);
                    EspressoTestingIdlingResource.decrement();

                }
            }
        });
    }

    Staff staff;

    public void buildLinkToFacilityDialog(Staff staff) {
        EspressoTestingIdlingResource.decrement();
        this.staff = staff;
        if (facilityList.size() == 0) {
            getFacilities();

        } else {
            facilityListViewModel.getFacilityDataForClinicType("");
        }


    }

    private void checkAllFacilitiesThatLinkedToStaffAndShowDialog(@NotNull Staff staff) {
        EspressoTestingIdlingResource.increment();
        List<Facility> facilityArrayList = facilityList;
        if (staff.getFacilityList() != null)
            for (int i = 0; i < staff.getFacilityList().size(); i++) {
                for (int j = 0; j < facilityArrayList.size(); j++) {
                    if (staff.getFacilityList().get(i).getDescription().equals(facilityArrayList.get(j).getDescription())) {
                        facilityArrayList.get(j).setSelected(true);
                    }
                }
            }
        facilitiesListDialog = new FacilitiesListDialog(HomeActivity.this, facilityArrayList, staff.getStaffId());
        facilitiesListDialog.show();


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            facilitiesListDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
        EspressoTestingIdlingResource.decrement();


    }

    @Override
    public void onShowDialogLinkToFacility(Staff staff) {
        buildLinkToFacilityDialog(staff);

    }

    public void navigateToAgentList() {

        navController.navigate(R.id.agentList, null,
                new NavOptions.Builder()
                        .setPopUpTo(R.id.agentList,
                                true).build());

    }

    public void navigateToProviderList() {
        navController.popBackStack();
        navController.navigate(R.id.providerList);

    }

    public void navigateToClientList() {
        navController.popBackStack();
        navController.navigate(R.id.clientList);

    }

    public void navigateToClinicTypeList() {
        navController.popBackStack();
        navController.navigate(R.id.facility);
    }

    public void navigateToCodeList() {
        navController.popBackStack();
        navController.navigate(R.id.codeList);

    }

    public void navigateToAppointments() {
        navController.popBackStack();
        navController.navigate(R.id.appointments);
    }

    public void navigateToBranches() {
        navController.popBackStack();
        navController.navigate(R.id.branches);
    }

    public void navigateToSchedules() {
        navController.popBackStack();
        navController.navigate(R.id.scheduleFragment);
    }

    @Override
    public void onConfirmLinkToFacility(String staffId, FacilityCodes facilityCodes) {
        EspressoTestingIdlingResource.increment();
        mainViewModel.linkToFacility(staffId, facilityCodes, new OnDataChangedCallBackListener<String>() {
            @Override
            public void onResponse(String b) {
                EspressoTestingIdlingResource.decrement();
                Log.i(TAG, "onConfirmLinkToFacility: status " + b);
                EspressoTestingIdlingResource.increment();
                if (facilitiesListDialog.isShowing()) {
                    facilitiesListDialog.dismiss();
                    navigateToAgentList();
                }
                EspressoTestingIdlingResource.decrement();

            }
        });
    }

    private void deleteAgentOrProvider(final Staff itemDeleted) {
        mainViewModel.deleteAgentOrProvider(itemDeleted).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                EspressoTestingIdlingResource.increment();
                deleteStaffMessage = s;
                if (itemDeleted.getTypeCode().equals(EnumCode.StaffTypeCode.DSPTCHR.toString())) {
                    navigateToAgentList();
                    Toast.makeText(HomeActivity.this, deleteStaffMessage, Toast.LENGTH_SHORT).show();
                } else if (itemDeleted.getTypeCode().equals(EnumCode.StaffTypeCode.PRVDR.toString())) {
                    navigateToProviderList();
                    Toast.makeText(HomeActivity.this, deleteStaffMessage, Toast.LENGTH_SHORT).show();

                }
                EspressoTestingIdlingResource.decrement();
            }
        });
    }

    private void deleteFacility(Facility facility) {
        mainViewModel.deleteFacility(facility).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                EspressoTestingIdlingResource.increment();
                deleteFacilityMessage = s;
                Toast.makeText(HomeActivity.this, deleteFacilityMessage, Toast.LENGTH_SHORT).show();
                navigateToClinicTypeList();
                EspressoTestingIdlingResource.decrement();
            }
        });


    }

    private void deleteCode(Code code) {
        mainViewModel.deleteCode(code).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                deleteCodeMessage = s;
                Toast.makeText(HomeActivity.this, deleteCodeMessage, Toast.LENGTH_SHORT).show();
                navigateToCodeList();
            }
        });
    }

    private void deleteBranch(Branch branch) {
        mainViewModel.deleteBranch(branch).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                deleteBranchMessage = s;
                Toast.makeText(HomeActivity.this, deleteBranchMessage, Toast.LENGTH_SHORT).show();
                navigateToBranches();
            }
        });
    }

    private void deleteSchedule(Schedule schedule) {
        mainViewModel.deleteSchedule(schedule).observe(this, new Observer<Error>() {
            @Override
            public void onChanged(Error error) {
                if (error.getErrorCode() == 0) {
                    deleteScheduleMessage = error.getErrorMessage();
                    Toast.makeText(HomeActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    navigateToSchedules();
                } else {
                    Toast.makeText(HomeActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deleteSession(Session session) {
        mainViewModel.deleteSession(session).observe(this, new Observer<Error>() {
            @Override
            public void onChanged(Error error) {
                Toast.makeText(HomeActivity.this, error.getErrorMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(TimeSlot selectedSlot) {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        Fragment timeSlot = navHostFragment.getChildFragmentManager().getFragments().get(0);

        if (timeSlot != null) {
            OnItemClickedListener onItemClickedListener = (OnItemClickedListener) timeSlot;
            onItemClickedListener.onClickAtPosition(selectedSlot);
        }
    }

    @Override
    public void onClickOnOkBtn() {
        navigateToClientList();
    }

    @Override
    public void onOpenCancelAppointmentDialog(String appointmentId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(HomeActivity.this);
        dialogBuilder.setIcon(R.drawable.ic_fluid);
        dialogBuilder.setTitle(R.string.cancel_appointment_txt);
        dialogBuilder.setPositiveButton(R.string.ok_txt_btn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelAppointment(appointmentId);
                dialog.dismiss();
            }
        });
        dialogBuilder.setNegativeButton(R.string.cancel_btn_txt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder.show();
    }

    private void cancelAppointment(String appointmentId) {
        mainViewModel.cancelAppointment(appointmentId).observe(this, new Observer<Status>() {
            @Override
            public void onChanged(Status status) {
                if (status != null) {
                    if (status.getStatus().equals("0")) {
                        Toast.makeText(HomeActivity.this, "cancel appointment successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeActivity.this, "error with status code" + status.getStatus(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
