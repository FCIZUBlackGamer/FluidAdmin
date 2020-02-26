package com.thetatecno.fluidadmin.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.thetatecno.fluidadmin.R;
import com.thetatecno.fluidadmin.listeners.OnConfirmDeleteListener;
import com.thetatecno.fluidadmin.listeners.OnDeleteListener;
import com.thetatecno.fluidadmin.listeners.OnFragmentInteractionListener;
import com.thetatecno.fluidadmin.listeners.OnFragmentInteractionWithFacilityTypesListener;
import com.thetatecno.fluidadmin.model.Code;
import com.thetatecno.fluidadmin.model.CodeList;
import com.thetatecno.fluidadmin.model.CustomerList;
import com.thetatecno.fluidadmin.model.Facilities;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.model.Person;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.model.StaffData;
import com.thetatecno.fluidadmin.utils.Constants;
import com.thetatecno.fluidadmin.utils.PreferenceController;
import com.thetatecno.fluidadmin.utils.EnumCode;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.Serializable;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.BreadcrumbBuilder;
import io.sentry.event.UserBuilder;


public class HomeActivity extends BaseActivity implements   NavigationView.OnNavigationItemSelectedListener, OnDeleteListener, OnConfirmDeleteListener {
    MainViewModel mainViewModel;
    NavigationView navigationView;
    ConfirmDeleteDialog confirmDeleteDialog;
    NavController navController;
    Bundle bundle;
    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.agentList, R.id.providerList, R.id.clientList,
                R.id.codeList, R.id.clinicList, R.id.waiting_areaList)
                .setDrawerLayout(drawer)
                .build();
         navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        checkOnTheCurrentLanguage();
//        navigationView.setNavigationItemSelectedListener(this);

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
            // recreate();

        }

    }

    @Override
    public void onDeleteButtonClicked(Object itemClicked) {
        confirmDeleteDialog = new ConfirmDeleteDialog(this, itemClicked);
        confirmDeleteDialog.show();

    }

    @Override
    public void onOkButtonClicked(final Object itemDeleted) {
        if (confirmDeleteDialog.isShowing()) {
            confirmDeleteDialog.dismiss();
        }
        if (itemDeleted instanceof Facility) {
            Log.i("Object", "facility type " + ((Facility) itemDeleted).getCode());
            mainViewModel.deleteFacility((Facility) itemDeleted).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Toast.makeText(HomeActivity.this, s, Toast.LENGTH_SHORT).show();

                }
            });


        }
        if (itemDeleted instanceof Staff) {
            Log.i("Object", "staff type " + ((Staff) itemDeleted).getStaffId());
            mainViewModel.deleteAgentOrProvider((Staff) itemDeleted).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Toast.makeText(HomeActivity.this, s, Toast.LENGTH_SHORT).show();

                    if (((Staff) itemDeleted).getTypeCode().equals(EnumCode.StaffTypeCode.DSPTCHR.toString()))
                    {
                        navController.navigate(R.id.agentList  ,null,
                                new NavOptions.Builder()
                                        .setPopUpTo(R.id.agentList,
                                                true).build());
                        Toast.makeText(HomeActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                    else if(((Staff) itemDeleted).getTypeCode().equals(EnumCode.StaffTypeCode.PRVDR.toString())){
                        navController.navigate(R.id.providerList  ,null,
                                new NavOptions.Builder()
                                        .setPopUpTo(R.id.providerList,
                                                true).build());
                        Toast.makeText(HomeActivity.this, s, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        if (itemDeleted instanceof Code) {
            Log.i("Object", "code type " + ((Code) itemDeleted).getCode());
            mainViewModel.deleteCode((Code) itemDeleted).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Toast.makeText(HomeActivity.this, s, Toast.LENGTH_SHORT).show();

                    navController.navigate(R.id.codeList  ,null,
                           new NavOptions.Builder()
                                    .setPopUpTo(R.id.codeList,
                                            true).build());
                }
            });
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        item.setChecked(true);


        drawer.closeDrawers();

        int id = item.getItemId();

        switch (id) {

            case R.id.language_reference:
                changeLanguage((String) item.getTitle());
                break;
            case R.id.agentList:
                navController.navigate(R.id.agentList);
                break;

            case R.id.providerList:
                navController.navigate(R.id.providerList);
                break;

            case R.id.codeList:
                navController.navigate(R.id.codeList);
                break;
            case R.id.clientList:
                navController.navigate(R.id.clinicList);
                break;

            case R.id.clinicList:
                navController.navigate(R.id.clinicList);
                break;

            case R.id.waiting_areaList:
                navController.navigate(R.id.waiting_areaList);
                break;





        }
        return true;

    }
}
