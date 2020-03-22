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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.listeners.OnConfirmDeleteListener;
import com.thetatechno.fluidadmin.listeners.OnConfirmLinkToFacilityListener;
import com.thetatechno.fluidadmin.listeners.OnDataChangedCallBackListener;
import com.thetatechno.fluidadmin.listeners.OnDeleteListener;
import com.thetatechno.fluidadmin.listeners.OnLinkToFacilityClickedListener;
import com.thetatechno.fluidadmin.model.Code;
import com.thetatechno.fluidadmin.model.Facilities;
import com.thetatechno.fluidadmin.model.Facility;
import com.thetatechno.fluidadmin.model.FacilityCodes;
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
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.UserBuilder;


public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnDeleteListener, OnConfirmDeleteListener, OnLinkToFacilityClickedListener, OnConfirmLinkToFacilityListener {
    private MainViewModel mainViewModel;
    private FacilityListViewModel facilityListViewModel;
    private NavigationView navigationView;
    private ConfirmDeleteDialog confirmDeleteDialog;
    private NavController navController;
    private Bundle bundle;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private FacilitiesListDialog facilitiesListDialog;
    private AlertDialog alertDialog;
    private static final String TAG = HomeActivity.class.getSimpleName();
    private List<Facility> facilityList = new ArrayList<>();
    private String deleteStaffMessage = "";
    private String deleteCodeMessage = "";
    private String deleteFacilityMessage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(this));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );

        setContentView(R.layout.activity_home);
        bundle = new Bundle();
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
                R.id.codeList, R.id.facility)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        checkOnTheCurrentLanguage();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(false);


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
//        confirmDeleteDialog = new ConfirmDeleteDialog(this, itemClicked);
//        confirmDeleteDialog.show();

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


        }
        if (itemDeleted instanceof Staff) {
            Log.i("Object", "staff type " + ((Staff) itemDeleted).getStaffId());
            if (deleteStaffMessage.isEmpty())
                deleteAgentOrProvider((Staff) itemDeleted);
            else
                mainViewModel.deleteAgentOrProvider((Staff) itemDeleted);
        }
        if (itemDeleted instanceof Code) {
            Log.i("Object", "code type " + ((Code) itemDeleted).getCode());
            if (deleteCodeMessage.isEmpty())
                deleteCode((Code) itemDeleted);
            else
                mainViewModel.deleteCode((Code) itemDeleted);
        }
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
        EspressoTestingIdlingResource.decrement();

    }

    int id;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.isChecked();
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

        }

        return true;

    }

    public void getFacilities() {
        EspressoTestingIdlingResource.increment();
        facilityListViewModel.getFacilityDataForClinicType("").observe(this, new Observer<Facilities>() {
            @Override
            public void onChanged(Facilities facilities) {
                EspressoTestingIdlingResource.decrement();
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
        navController.navigate(R.id.providerList, null,
                new NavOptions.Builder()
                        .setPopUpTo(R.id.providerList,
                                true).build());

    }

    public void navigateToClientList() {

        navController.navigate(R.id.clientList, null,
                new NavOptions.Builder()
                        .setPopUpTo(R.id.clientList,
                                true).build());

    }

    public void navigateToClinicTypeList() {
        navController.navigate(R.id.facility, null,
                new NavOptions.Builder()
                        .setPopUpTo(R.id.facility,
                                true).build());
    }

    public void navigateToCodeList() {
        navController.navigate(R.id.codeList, null,
                new NavOptions.Builder()
                        .setPopUpTo(R.id.codeList,
                                true).build());

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
}
