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
import com.thetatecno.fluidadmin.ui.addorupdatecode.CodeAddFragment;
import com.thetatecno.fluidadmin.ui.addorupdatefacility.FacilityAddFragment;
import com.thetatecno.fluidadmin.ui.addorupdatestuff.AddOrUpdateAgentFragment;
import com.thetatecno.fluidadmin.ui.addorupdatestuff.AddOrUpdateProviderFragment;
import com.thetatecno.fluidadmin.utils.Constants;
import com.thetatecno.fluidadmin.utils.PreferenceController;
import com.thetatecno.fluidadmin.utils.EnumCode;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.io.Serializable;
import java.util.List;

import io.sentry.Sentry;
import io.sentry.SentryClient;
import io.sentry.android.AndroidSentryClientFactory;
import io.sentry.event.BreadcrumbBuilder;
import io.sentry.event.UserBuilder;


public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener, OnFragmentInteractionWithFacilityTypesListener, OnDeleteListener, OnConfirmDeleteListener {

    List<Staff> agentList;
    List<Staff> providerList;
    List<Person> personList;
    List<Facility> facilityList;
    List<Code> codeList;
    static EnumCode.UsageType usageType;
    MainViewModel mainViewModel;
    NavigationView navigationView;
    static String langId;
    ConfirmDeleteDialog confirmDeleteDialog;
    NavController navController;
    Bundle bundle;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Sentry.init("https://77af95af46ac4f068742d097b9c782c1@sentry.io/2577929", new AndroidSentryClientFactory(this));
        Sentry.getContext().setUser(
                new UserBuilder().setUsername("theta").build()
        );
        setContentView(R.layout.activity_home);
        bundle = new Bundle();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        checkOnTheCurrentLanguage();
        navigationView.setNavigationItemSelectedListener(this);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        fab = findViewById(R.id.fab);
        try {
            mainViewModel.getStaffDataForAgents(langId, EnumCode.StaffTypeCode.DSPTCHR.toString()).observe(this, new Observer<StaffData>() {
                @Override
                public void onChanged(StaffData staffData) {
                    if (staffData != null) {
                        if (staffData.getStaffList() != null) {
                            usageType = EnumCode.UsageType.Agent;
                            agentList = staffData.getStaffList();
                            bundle.putSerializable("type", (Serializable) EnumCode.UsageType.Agent);
                            bundle.putSerializable("agentList", (Serializable) agentList);
                            bundle.putSerializable("providerList", (Serializable) providerList);
                            bundle.putSerializable("personList", (Serializable) personList);
                            bundle.putSerializable("facilityList", (Serializable) facilityList);
                            bundle.putSerializable("codeList", (Serializable) codeList);
                            navController.navigate(R.id.mainFragment2, bundle);

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
                fab.setVisibility(View.GONE);

                Snackbar.make(view, "" + usageType.toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if (usageType == EnumCode.UsageType.Provider) {
                   bundle.putSerializable(Constants.ARG_STAFF,null);
                    navController.navigate(R.id.action_mainFragment2_to_addOrUpdateProviderFragment,bundle);

                } else if (usageType == EnumCode.UsageType.Agent) {

                    navController.navigate(R.id.action_mainFragment2_to_addOrUpdateAgentFragment,bundle);
                } else if (usageType == EnumCode.UsageType.Code) {

                    navController.navigate(R.id.action_mainFragment2_to_codeAddFragment,bundle);
                } else if (usageType == EnumCode.UsageType.Facility) {

                    navController.navigate(R.id.action_mainFragment2_to_facilityAddFragment,bundle);
                }

            }

        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(fab.getVisibility() == View.GONE)
        fab.setVisibility(View.VISIBLE);
        switch (item.getItemId()) {
            case R.id.agents:
                loadAgentsData();
                break;
            case R.id.provider:
                loadProvidersData();
                break;
            case R.id.client:
                loadClientsData();

                break;

            case R.id.code:
                loadCodesData();
                break;
            case R.id.clinic:
               loadFacilityClinicTypeData();

                break;

            case R.id.waiting_area:
                loadFaicilityWaitingAreaTypeData();
                break;

            case R.id.language_reference:
                changeLanguage((String) item.getTitle());
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFaicilityWaitingAreaTypeData() {
        Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Waiting Area Button From Navigation Drawer").build());

        try {
            mainViewModel.getStaffDataForClinics("", langId, EnumCode.ClinicTypeCode.WAITAREA.toString()).observe(this, new Observer<Facilities>() {
                @Override
                public void onChanged(Facilities facilities) {
                    if (facilities != null) {
                        if (facilities.getFacilities() != null) {
                            usageType = EnumCode.UsageType.Facility;
                            facilityList = facilities.getFacilities();
                            bundle.putSerializable("type", (Serializable) EnumCode.UsageType.Facility);
                            bundle.putSerializable("agentList", (Serializable) agentList);
                            bundle.putSerializable("providerList", (Serializable) providerList);
                            bundle.putSerializable("personList", (Serializable) personList);
                            bundle.putSerializable("facilityList", (Serializable) facilityList);
                            bundle.putSerializable("codeList", (Serializable) codeList);
                            navController.navigate(R.id.mainFragment2, bundle);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Sentry.capture(e);
        }


    }

    private void loadFacilityClinicTypeData() {
        Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Clinic Button From Navigation Drawer").build());

        try {
            mainViewModel.getStaffDataForClinics("", langId, EnumCode.ClinicTypeCode.CLINIC.toString()).observe(this, new Observer<Facilities>() {
                @Override
                public void onChanged(Facilities facilities) {
                    if (facilities != null) {
                        if (facilities.getFacilities() != null) {
                            usageType = EnumCode.UsageType.Facility;
                            facilityList = facilities.getFacilities();
                            bundle.putSerializable("type", (Serializable) EnumCode.UsageType.Facility);
                            bundle.putSerializable("agentList", (Serializable) agentList);
                            bundle.putSerializable("providerList", (Serializable) providerList);
                            bundle.putSerializable("personList", (Serializable) personList);
                            bundle.putSerializable("facilityList", (Serializable) facilityList);
                            bundle.putSerializable("codeList", (Serializable) codeList);
                            navController.navigate(R.id.mainFragment2, bundle);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Sentry.capture(e);
        }

    }

    private void loadCodesData() {
        Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Code Button From Navigation Drawer").build());
        try {
            mainViewModel.getDataForCode("", langId).observe(this, new Observer<CodeList>() {
                @Override
                public void onChanged(CodeList codeList1) {
                    if (codeList1 != null) {
                        if (codeList1.getCodeList() != null) {
                            codeList = codeList1.getCodeList();

                            usageType = EnumCode.UsageType.Code;
                            bundle.putSerializable("type", EnumCode.UsageType.Code);
                            bundle.putSerializable("agentList", (Serializable) agentList);
                            bundle.putSerializable("providerList", (Serializable) providerList);
                            bundle.putSerializable("personList", (Serializable) personList);
                            bundle.putSerializable("facilityList", (Serializable) facilityList);
                            bundle.putSerializable("codeList", (Serializable) codeList);
                            navController.navigate(R.id.mainFragment2, bundle);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Sentry.capture(e);
        }
    }

    private void loadClientsData() {
        Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Client Button From Navigation Drawer").build());

        Log.e("Client", "Found");
        try {
            mainViewModel.getAllClients("", langId).observe(this, new Observer<CustomerList>() {
                @Override
                public void onChanged(CustomerList customerList) {
                    if (customerList != null) {
                        if (customerList.getPersonList() != null) {
                            personList = customerList.getPersonList();
                            usageType = EnumCode.UsageType.Person;
                            Gson gson = new Gson();
                            Log.e("Data", gson.toJson(personList));
                            bundle.putSerializable("type", (Serializable) EnumCode.UsageType.Person);
                            bundle.putSerializable("agentList", (Serializable) agentList);
                            bundle.putSerializable("providerList", (Serializable) providerList);
                            bundle.putSerializable("personList", (Serializable) personList);
                            bundle.putSerializable("facilityList", (Serializable) facilityList);
                            bundle.putSerializable("codeList", (Serializable) codeList);
                            navController.navigate(R.id.mainFragment2, bundle);
                        } else {
                            Log.e("getPersonList()", "Null");
                        }
                    } else {
                        Log.e("customerList", "Null");
                    }
                }
            });

        } catch (Exception e) {
            Sentry.capture(e);
        }
    }

    public void loadAgentsData(){
        Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Agents Button From Navigation Drawer").build());
        try {
            mainViewModel.getStaffDataForAgents(langId, EnumCode.StaffTypeCode.DSPTCHR.toString()).observe(this, new Observer<StaffData>() {
                @Override
                public void onChanged(StaffData staffData) {
                    if (staffData != null) {
                        if (staffData.getStaffList() != null) {
                            usageType = EnumCode.UsageType.Agent;
                            agentList = staffData.getStaffList();
                            bundle.putSerializable("type", (Serializable) EnumCode.UsageType.Agent);
                            bundle.putSerializable("agentList", (Serializable) agentList);
                            bundle.putSerializable("providerList", (Serializable) providerList);
                            bundle.putSerializable("personList", (Serializable) personList);
                            bundle.putSerializable("facilityList", (Serializable) facilityList);
                            bundle.putSerializable("codeList", (Serializable) codeList);
                            navController.navigate(R.id.mainFragment2, bundle);
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
    }
    public void loadProvidersData(){
        Sentry.getContext().recordBreadcrumb(new BreadcrumbBuilder().setMessage("User Clicked Provider Button From Navigation Drawer").build());
        try {
            mainViewModel.getStaffDataForProviders(langId, EnumCode.StaffTypeCode.PRVDR.toString()).observe(this, new Observer<StaffData>() {
                @Override
                public void onChanged(StaffData staffData) {
                    if (staffData != null) {
                        if (staffData.getStaffList() != null) {
                            usageType = EnumCode.UsageType.Provider;
                            providerList = staffData.getStaffList();
                            bundle.putSerializable("type", (Serializable) EnumCode.UsageType.Provider);
                            bundle.putSerializable("agentList", (Serializable) agentList);
                            bundle.putSerializable("providerList", (Serializable) providerList);
                            bundle.putSerializable("personList", (Serializable) personList);
                            bundle.putSerializable("facilityList", (Serializable) facilityList);
                            bundle.putSerializable("codeList", (Serializable) codeList);
                            navController.navigate(R.id.mainFragment2, bundle);
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
    public void onFragmentAddOrUpdateEntity(EnumCode.UsageType usageType) {
        fab.setVisibility(View.VISIBLE);
        if(usageType == EnumCode.UsageType.Agent)
        {

            loadAgentsData();

        }
        else if(usageType == EnumCode.UsageType.Provider)
        {
            loadProvidersData();
        }
        else if (usageType == EnumCode.UsageType.Code){
            loadCodesData();
        }
    }
    @Override
    public void onAddOrUpdateFacility(EnumCode.ClinicTypeCode clinicTypeCode) {
        fab.setVisibility(View.VISIBLE);
     if(clinicTypeCode == EnumCode.ClinicTypeCode.CLINIC){
         loadFacilityClinicTypeData();
     }
     else if (clinicTypeCode.equals(EnumCode.ClinicTypeCode.WAITAREA))
     {
         loadFaicilityWaitingAreaTypeData();
     }

    }
    @Override
    public void onDeleteButtonClicked(Object itemClicked) {
        confirmDeleteDialog = new ConfirmDeleteDialog(this, itemClicked);
        confirmDeleteDialog.show();

    }

    @Override
    public void onOkButtonClicked(Object itemDeleted) {
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
                }
            });
        }
        if (itemDeleted instanceof Code) {
            Log.i("Object", "code type " + ((Code) itemDeleted).getCode());
            mainViewModel.deleteCode((Code) itemDeleted).observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    Toast.makeText(HomeActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }


    @Override
    public void onDisplayAddBtn() {
        fab.setVisibility(View.VISIBLE);
    }
}
