package com.thetatecno.fluidadmin.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.navigation.NavigationView;
import com.thetatecno.fluidadmin.R;
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

import java.util.List;

import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.BreadcrumbBuilder;
import io.sentry.event.UserBuilder;


public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener , OnFragmentInteractionListener {

    List<Staff> agentList;
    List<Staff> providerList;
    List<Person> personList;
    List<Facility> facilityList;
    List<Code> codeList;
    static UsageType usageType;
    MainViewModel mainViewModel;
    private static SentryClient sentry;
    NavigationView navigationView;
    static String langId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(this));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );
        setContentView(R.layout.activity_home);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        checkOnTheCurrentLanguage();
        navigationView.setNavigationItemSelectedListener(this);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final FloatingActionButton fab = findViewById(R.id.fab);
        try {
            mainViewModel.getStaffDataForAgents(langId, EnumCode.StaffTypeCode.DSPTCHR.toString()).observe(this, new Observer<StaffData>() {
                @Override
                public void onChanged(StaffData staffData) {
                    if (staffData != null) {
                        if (staffData.getStaffList() != null) {
                            usageType = UsageType.Agent;
                            agentList = staffData.getStaffList();
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Agent, agentList, null, null, null,null))
                                    .commit();
                        } else {
                            Log.e("Staff List", "Is Null");
                        }
                    } else {
                        Log.e("Staff", "Is Null");
                    }
                }
            });
        } catch (Exception e) {
            Sentry.capture(e);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "" + usageType.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if(usageType == UsageType.Provider) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, AddNewStaffFragment.newInstance(null))
                            .addToBackStack("AddNewStaffFragment")
                            .commit();
                }
                else if(usageType == UsageType.Agent){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, AddOrUpdateAgentFragment.newInstance(null))
                            .addToBackStack("AddOrUpdateAgentFragment")
                            .commit();

                }
                else if(usageType == UsageType.Code){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, CodeAddFragment.newInstance(null))
                            .addToBackStack("CodeAddFragment")

                            .commit();
                }
                else if(usageType == UsageType.Facility){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.nav_host_fragment, FacilityAddFragment.newInstance(null))
                            .addToBackStack("FacilityAddFragment")
                            .commit();
                }

             fab.setVisibility(View.GONE);
            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.agents:
                Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Agents Button From Navigation Drawer").build());
                try {
                    mainViewModel.getStaffDataForAgents(langId, EnumCode.StaffTypeCode.DSPTCHR.toString()).observe(this, new Observer<StaffData>() {
                        @Override
                        public void onChanged(StaffData staffData) {
                            if (staffData != null) {
                                if (staffData.getStaffList() != null) {
                                    usageType = UsageType.Agent;
                                    agentList = staffData.getStaffList();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Agent, agentList, null, null, null,null))
                                            .commit();
                                } else {
                                    Log.e("Staff List", "Is Null");
                                }
                            } else {
                                Log.e("Staff", "Is Null");
                            }
                        }
                    });
                } catch (Exception e) {
                    Sentry.capture(e);
                }

                break;
            case R.id.provider:
                Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Provider Button From Navigation Drawer").build());
                try {
                    mainViewModel.getStaffDataForProviders(langId, EnumCode.StaffTypeCode.PRVDR.toString()).observe(this, new Observer<StaffData>() {
                        @Override
                        public void onChanged(StaffData staffData) {
                            if (staffData != null) {
                                if (staffData.getStaffList() != null) {
                                    usageType = UsageType.Provider;
                                    providerList = staffData.getStaffList();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Provider, null, providerList, null, null,null))
                                            .commit();
                                } else {
                                    Log.e("Staff List", "Is Null");
                                }
                            } else {
                                Log.e("Staff", "Is Null");
                            }
                        }
                    });
                } catch (Exception e) {
                    Sentry.capture(e);
                }

                break;
            case R.id.client:
                Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Client Button From Navigation Drawer").build());

                try {
                    mainViewModel.getStaffDataForPerson("", langId).observe(this, new Observer<CustomerList>() {
                        @Override
                        public void onChanged(CustomerList customerList) {
                            if (customerList != null) {
                                if (customerList.getPersonList() != null) {
                                    personList = customerList.getPersonList();
                                    usageType = UsageType.Person;
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Person, null, null, personList, null,null))
                                            .commit();
                                }
                            }
                        }
                    });

                } catch (Exception e) {
                    Sentry.capture(e);
                }

                break;

            case R.id.code:
                Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Code Button From Navigation Drawer").build());
                try {
                    mainViewModel.getStaffDataForCode("", langId).observe(this, new Observer<CodeList>() {
                        @Override
                        public void onChanged(CodeList codeList1) {
                            if (codeList1 != null) {
                                if (codeList1.getCodeList() != null) {
                                    codeList = codeList1.getCodeList();

                                    usageType = UsageType.Code;
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Code, null, null, null, null,codeList))
                                            .commit();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    Sentry.capture(e);
                }
                break;
            case R.id.clinic:
                Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Clinic Button From Navigation Drawer").build());

                try {
                    mainViewModel.getStaffDataForClinics("", langId, EnumCode.ClinicTypeCode.CLINIC.toString()).observe(this, new Observer<Facilities>() {
                        @Override
                        public void onChanged(Facilities facilities) {
                            if (facilities != null) {
                                if (facilities.getFacilities() != null) {
                                    usageType = UsageType.Facility;
                                    facilityList = facilities.getFacilities();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Facility, null, null, null, facilityList,null))
                                            .commit();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    Sentry.capture(e);
                }


                break;

            case R.id.waiting_area:
                Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Waiting Area Button From Navigation Drawer").build());

                try {
                    mainViewModel.getStaffDataForClinics("", langId, EnumCode.ClinicTypeCode.WAITAREA.toString()).observe(this, new Observer<Facilities>() {
                        @Override
                        public void onChanged(Facilities facilities) {
                            if (facilities != null) {
                                if (facilities.getFacilities() != null) {
                                    usageType = UsageType.Facility;
                                    facilityList = facilities.getFacilities();
                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Facility, null, null, null, facilityList,null))
                                            .commit();
                                }
                            }
                        }
                    });
                } catch (Exception e) {
                    Sentry.capture(e);
                }


                break;

            case R.id.language_reference:
                changeLanguage((String) item.getTitle());
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void checkOnTheCurrentLanguage() {
        langId = PreferenceController.getInstance(this).get(PreferenceController.LANGUAGE).toUpperCase();
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
    public void onFragmentInteraction() {
        getSupportFragmentManager().popBackStack("CodeAddFragment",0);
    }
}
