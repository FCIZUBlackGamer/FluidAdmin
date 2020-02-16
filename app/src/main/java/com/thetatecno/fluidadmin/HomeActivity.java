package com.thetatecno.fluidadmin;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.thetatecno.fluidadmin.model.Agent;
import com.thetatecno.fluidadmin.model.Facility;
import com.thetatecno.fluidadmin.model.Person;
import com.thetatecno.fluidadmin.model.Provider;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    List<Agent> agentList;
    List<Provider> providerList;
    List<Person> personList;
    List<Facility> facilityList;
    static UsageType usageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        usageType = UsageType.Agent;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Agent, agentList, null, null, null))
                .commit();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, ""+usageType.toString(), Snackbar.LENGTH_LONG)
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
                usageType = UsageType.Agent;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Agent, agentList, null, null, null))
                        .commit();
                break;
            case R.id.provider:
                usageType = UsageType.Provider;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Provider, null, providerList, null, null))
                        .commit();
                break;
            case R.id.client:
                usageType = UsageType.Person;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Person, null, null, personList, null))
                        .commit();
                break;
            case R.id.facility:
                usageType = UsageType.Facility;
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, MainFragment.setTypeAndData(UsageType.Facility, null, null, null, facilityList))
                        .commit();
                break;

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
