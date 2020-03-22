package com.thetatechno.fluidadmin.activities;

import android.util.Log;
import android.view.View;
import android.widget.SearchView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.thetatechno.fluidadmin.R;
import com.thetatechno.fluidadmin.ui.EspressoTestingIdlingResource;
import com.thetatechno.fluidadmin.ui.HomeActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4ClassRunner.class)

public class AgentTest {
    @Rule
    public ActivityScenarioRule activityRole = new ActivityScenarioRule(HomeActivity.class);
    HomeActivity homeActivity;
    int countBefore;

    @Before
    public void registerIdlingResource() {
        activityRole.getScenario().onActivity(new ActivityScenario.ActivityAction<HomeActivity>() {
            @Override
            public void perform(HomeActivity activity) {
                homeActivity = activity;
                IdlingRegistry.getInstance().register(EspressoTestingIdlingResource.getIdlingResource());

            }
        });

    }

    public void countNumberOfRowsBeforeDoAnyAction(){
        onView(ViewMatchers.withId(R.id.agentsListRecyclerView)).check(matches(isDisplayed()));
        RecyclerView recyclerView = homeActivity.findViewById(R.id.agentsListRecyclerView);
        countBefore = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
        Log.e("count", "Provider items count before: " + countBefore);
    }

    public void countNumberOfRowsInsideDialog(){
        onView(ViewMatchers.withId(R.id.facilityListView)).check(matches(isDisplayed()));
        onView(withId(R.id.link_btn)).check(matches(isDisplayed()));
        RecyclerView recyclerView = homeActivity.findViewById(R.id.facilityListView);
        countBefore = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
        Log.e("count", "Provider items count before: " + countBefore);
    }

    @Test
    public void testAddNewAgent() {
       countNumberOfRowsBeforeDoAnyAction();
        onView(withId(R.id.addAgentFab)).perform(click());
        onView(withId(R.id.agentIdEdtTxt)).perform(typeText("2"), closeSoftKeyboard());
        onView(withId(R.id.agentFirstNameEdtTxt)).perform(typeText("nesma"), closeSoftKeyboard());
        onView(withId(R.id.agentFamilyNameEdtTxt)).perform(typeText("tharwat"), closeSoftKeyboard());
        onView(withId(R.id.agentFemaleRadioButton))
                .perform(click());
        onView(withId(R.id.addOrUpdateAgentBtn)).perform(click());
        onView(ViewMatchers.withId(R.id.agentsListRecyclerView)).check(new RecyclerViewItemCountAssertion(greaterThan(countBefore)));

    }

    @Test
    public void testDeleteAgent() {
       countNumberOfRowsBeforeDoAnyAction();
        onView(withId(R.id.agentsListRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.agentTextViewOptions)));
        onView(anyOf(withText(R.string.delete_txt), withId(R.id.deleteAgent))).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(ViewMatchers.withId(R.id.agentsListRecyclerView)).check(new RecyclerViewItemCountAssertion(lessThan(countBefore)));
    }

    @Test
    public void testUpdateSpecificAgent(){
        countNumberOfRowsBeforeDoAnyAction();
        onView(withId(R.id.agentsListRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(countBefore-2, MyViewAction.clickChildViewWithId(R.id.agentTextViewOptions)));
        onView(anyOf(withText(R.string.update_txt), withId(R.id.editAgent))).perform(click());
        onView(withId(R.id.agentFirstNameEdtTxt)).perform(clearText(),typeText("nesma"), closeSoftKeyboard());
        onView(withId(R.id.agentFamilyNameEdtTxt)).perform(clearText(),typeText("tharwat"), closeSoftKeyboard());
        onView(withId(R.id.agentEmailEditTxt)).perform(clearText(),typeText("nesmatharwat20@gmail.com"),closeSoftKeyboard());
        onView(withId(R.id.agentFemaleRadioButton))
                .perform(click());
        onView(withId(R.id.addOrUpdateAgentBtn)).perform(click());
        onView(ViewMatchers.withId(R.id.agentsListRecyclerView)).check(new RecyclerViewItemCountAssertion(is(countBefore)));
    }

    @Test
    public void testLinkToFacility(){
        onView(withId(R.id.agentsListRecyclerView)).check(matches(isDisplayed()));
        onView(withId(R.id.agentsListRecyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.agentTextViewOptions)));
        onView(anyOf(withText(R.string.link_to_facility_txt), withId(R.id.linkToFacility))).perform(click());
        onView(withId(R.id.searchForSpecificFacility)).perform(typeSearchViewText("Room 5"));
        onView(withId(R.id.facilityListView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.facilityCheckBox)));
        onView(withId(R.id.link_btn)).perform(click());
    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource());

    }

     static ViewAction typeSearchViewText(final String text){
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                Log.i("Test",text);

                ((SearchView) view).setQuery(text,false);

            }
        };
    }
}
