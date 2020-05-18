package com.thetatechno.fluidadmin.activities;

import android.util.Log;
import android.view.Gravity;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.ui.HomeActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4ClassRunner.class)

public class ProviderTest {

    @Rule
    public ActivityScenarioRule activityRole = new ActivityScenarioRule(HomeActivity.class);
    HomeActivity homeActivity;
    int countBefore;

    @Before
    public void registerIdlingResource() {
//        ActivityScenario activityScenario = ActivityScenario.launch(HomeActivity.class);
        activityRole.getScenario().onActivity(new ActivityScenario.ActivityAction<HomeActivity>() {
            @Override
            public void perform(HomeActivity activity) {
                homeActivity = activity;
                IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource());

            }
        });

    }

    public void openDrawerAndGoToProviderListAndCountNumberBeforeDoingAction() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer

        // Nav to fragment.
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.providerList));

        onView(ViewMatchers.withId(R.id.providerRecyclerView)).check(matches(isDisplayed()));
        RecyclerView recyclerView = homeActivity.findViewById(R.id.providerRecyclerView);
        countBefore = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
    }

    @Test
    public void testAddNewProvider() {
        openDrawerAndGoToProviderListAndCountNumberBeforeDoingAction();
        Log.e("count", "Provider items count before: " + countBefore);
        onView(withId(R.id.addProviderFab)).perform(click());
        onView(withId(R.id.providerIdEdtTxt)).perform(typeText("STFF25467"), closeSoftKeyboard());
        onView(withId(R.id.providerFirstNameEdtTxt)).perform(typeText("nesma"), closeSoftKeyboard());
        onView(withId(R.id.providerLastNameEdtTxt)).perform(typeText("tharwat"), closeSoftKeyboard());
        onView(withId(R.id.femaleRadioButton))
                .perform(click());
        onView(withId(R.id.providerSpecialityAutoCompleteTextView)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("ENT"))).perform(click());
        onView(withId(R.id.providerSpecialityAutoCompleteTextView)).check(matches(withSpinnerText(containsString("ENT"))));
        onView(withId(R.id.addOrUpdateProviderBtn)).perform(click());
        onView(ViewMatchers.withId(R.id.providerRecyclerView)).check(new RecyclerViewItemCountAssertion(greaterThan(countBefore)));

    }


    @Test
    public void testDeleteProvider() {
       openDrawerAndGoToProviderListAndCountNumberBeforeDoingAction();
        onView(withId(R.id.providerRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(countBefore-2, MyViewAction.clickChildViewWithId(R.id.providerTxtViewOption)));
        onView(anyOf(withText(R.string.delete_txt), withId(R.id.deleteProvider))).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        // check that is deleted successfully
        onView(ViewMatchers.withId(R.id.providerRecyclerView)).check(new RecyclerViewItemCountAssertion(lessThan(countBefore)));
    }
    @Test
    public void testUpdateSpecificProvider(){
        openDrawerAndGoToProviderListAndCountNumberBeforeDoingAction();
        onView(withId(R.id.providerRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.providerTxtViewOption)));
        onView(anyOf(withText(R.string.update_txt), withId(R.id.editProvider))).perform(click());
        onView(withId(R.id.providerFirstNameEdtTxt)).perform(clearText(),typeText("nesma"), closeSoftKeyboard());
        onView(withId(R.id.providerLastNameEdtTxt)).perform(clearText(),typeText("tharwat"), closeSoftKeyboard());
        onView(withId(R.id.providerEmailEditTxt)).perform(clearText(),typeText("nesmatharwat20@gmail.com"),closeSoftKeyboard());
        onView(withId(R.id.femaleRadioButton))
                .perform(click());
        onView(withId(R.id.providerSpecialityAutoCompleteTextView)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("ENT"))).perform(click());
        onView(withId(R.id.providerSpecialityAutoCompleteTextView)).check(matches(withSpinnerText(containsString("ENT"))));
        onView(withId(R.id.addOrUpdateProviderBtn)).perform(click());
        onView(ViewMatchers.withId(R.id.providerRecyclerView)).check(new RecyclerViewItemCountAssertion(is(countBefore)));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource());

    }
}
