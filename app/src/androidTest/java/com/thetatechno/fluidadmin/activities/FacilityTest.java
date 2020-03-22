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
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
@RunWith(AndroidJUnit4ClassRunner.class)
public class FacilityTest {

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

    public void openDrawerGoToFacilityListListAndGetNumberCount() {
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.facility));
        onView(ViewMatchers.withId(R.id.facilityListClinicRecyclerView)).check(matches(isDisplayed()));
        RecyclerView recyclerView = homeActivity.findViewById(R.id.facilityListClinicRecyclerView);
        countBefore = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
    }
    @Test
    public void testAddNewFacility() {
       openDrawerGoToFacilityListListAndGetNumberCount();
        onView(withId(R.id.addNewFacilityFab)).perform(click());
        onView(withId(R.id.facility_id_et)).perform(typeText("RM899"), closeSoftKeyboard());
        onView(withId(R.id.desc_et)).perform(typeText("Room 899"), closeSoftKeyboard());
        onView(withId(R.id.clinicTyperadioButton)).perform(click());
        onView(withId(R.id.addOrUpdateFacilityBtn)).perform(click());
        onView(ViewMatchers.withId(R.id.facilityListClinicRecyclerView)).check(new RecyclerViewItemCountAssertion(greaterThan(countBefore)));

    }

    @Test
    public void testDeleteFacility() {
      openDrawerGoToFacilityListListAndGetNumberCount();
        onView(withId(R.id.facilityListClinicRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.facilityTextViewOptions)));
        onView(anyOf(withText(R.string.delete_txt), withId(R.id.deleteFacility))).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(ViewMatchers.withId(R.id.facilityListClinicRecyclerView)).check(new RecyclerViewItemCountAssertion(lessThan(countBefore)));


    }


    @Test
    public void testUpdateSpecificFacility() {
        openDrawerGoToFacilityListListAndGetNumberCount();
        onView(withId(R.id.facilityListClinicRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, MyViewAction.clickChildViewWithId(R.id.facilityTextViewOptions)));
        onView(anyOf(withText(R.string.update_txt), withId(R.id.editFacility))).perform(click());
        onView(withId(R.id.desc_et)).perform(clearText(),typeText("Room 11"), closeSoftKeyboard());
        onView(withId(R.id.clinicTyperadioButton)).perform(click());
        onView(withId(R.id.waitingAreaSpinner)).perform(click());
        onData(anything()).atPosition(1).perform(click());
        onView(withId(R.id.addOrUpdateFacilityBtn)).perform(click());
        onView(ViewMatchers.withId(R.id.facilityListClinicRecyclerView)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.facilityListClinicRecyclerView)).check(new RecyclerViewItemCountAssertion(is(countBefore)));
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource());

    }
}
