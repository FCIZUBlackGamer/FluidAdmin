package com.thetatecno.fluidadmin;

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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.thetatecno.fluidadmin.model.CustomerList;
import com.thetatecno.fluidadmin.model.Facilities;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.model.Person;
import com.thetatecno.fluidadmin.model.Provider;
import com.thetatecno.fluidadmin.model.Staff;
import com.thetatecno.fluidadmin.model.StaffData;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    enum TypeCode {
        PRVDR,
        DSPTCHR,
        CLINIC,
        WAITAREA
    }

    List<Staff> agentList;
    List<Staff> providerList;
    List<Person> personList;
    List<Facility> facilityList;
    static UsageType usageType;
    MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        mainViewModel.getStaffDataForAgents("EN", TypeCode.DSPTCHR.toString()).observe(this, new Observer<StaffData>() {
            @Override
            public void onChanged(StaffData staffData) {
                if (staffData != null) {
                    if (staffData.getStaffList() != null) {
                        usageType = UsageType.Agent;
                        agentList = staffData.getStaffList();
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Agent, agentList, null, null, null))
                                .commit();
                    } else {
                        Log.e("Staff List", "Is Null");
                    }
                } else {
                    Log.e("Staff", "Is Null");
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "" + usageType.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
                mainViewModel.getStaffDataForAgents("EN", TypeCode.DSPTCHR.toString()).observe(this, new Observer<StaffData>() {
                    @Override
                    public void onChanged(StaffData staffData) {
                        if (staffData != null) {
                            if (staffData.getStaffList() != null) {
                                usageType = UsageType.Agent;
                                agentList = staffData.getStaffList();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Agent, agentList, null, null, null))
                                        .commit();
                            } else {
                                Log.e("Staff List", "Is Null");
                            }
                        } else {
                            Log.e("Staff", "Is Null");
                        }
                    }
                });
                break;
            case R.id.provider:
                mainViewModel.getStaffDataForProviders("EN", TypeCode.PRVDR.toString()).observe(this, new Observer<StaffData>() {
                    @Override
                    public void onChanged(StaffData staffData) {
                        if (staffData != null) {
                            if (staffData.getStaffList() != null) {
                                usageType = UsageType.Provider;
                                providerList = staffData.getStaffList();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Provider, null, providerList, null, null))
                                        .commit();
                            } else {
                                Log.e("Staff List", "Is Null");
                            }
                        } else {
                            Log.e("Staff", "Is Null");
                        }
                    }
                });
                break;
            case R.id.client:
                mainViewModel.getStaffDataForPerson("", "EN").observe(this, new Observer<CustomerList>() {
                    @Override
                    public void onChanged(CustomerList customerList) {
                        if (customerList != null) {
                            if (customerList.getPersonList() != null) {
                                personList = customerList.getPersonList();
                                usageType = UsageType.Person;
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Person, null, null, personList, null))
                                        .commit();
                            }
                        }
                    }
                });

                break;
            case R.id.clinic:
                mainViewModel.getStaffDataForClinics("", "EN", TypeCode.CLINIC.toString()).observe(this, new Observer<Facilities>() {
                    @Override
                    public void onChanged(Facilities facilities) {
                        if (facilities != null) {
                            if (facilities.getFacilities() != null) {
                                usageType = UsageType.Facility;
                                facilityList = facilities.getFacilities();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Facility, null, null, null, facilityList))
                                        .commit();
                            }
                        }
                    }
                });

                break;

            case R.id.waiting_area:
                mainViewModel.getStaffDataForClinics("", "EN", TypeCode.WAITAREA.toString()).observe(this, new Observer<Facilities>() {
                    @Override
                    public void onChanged(Facilities facilities) {
                        if (facilities != null) {
                            if (facilities.getFacilities() != null) {
                                usageType = UsageType.Facility;
                                facilityList = facilities.getFacilities();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Facility, null, null, null, facilityList))
                                        .commit();
                            }
                        }
                    }
                });

                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
