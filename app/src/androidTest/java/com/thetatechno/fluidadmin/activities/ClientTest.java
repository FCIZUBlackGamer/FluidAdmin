package com.thetatechno.fluidadmin.activities;

import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;

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
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.thetatechno.fluidadmin.activities.CustomMatchers.withItemContent;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4ClassRunner.class)

public class ClientTest {

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

    public void countNumberOfRowsBeforeDoAnyAction(){
        // Open Drawer to click on navigation.
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open()); // Open Drawer
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.clientList));
        onView(ViewMatchers.withId(R.id.clientListView)).check(matches(isDisplayed()));
        RecyclerView recyclerView = homeActivity.findViewById(R.id.clientListView);
        countBefore = Objects.requireNonNull(recyclerView.getAdapter()).getItemCount();
        Log.e("count", "Provider items count before: " + countBefore);
    }



    @Test
    public void testSarchWithClients(){
        countNumberOfRowsBeforeDoAnyAction();
        onView(withId(R.id.action_search)).perform(click());
        onView(isAssignableFrom(EditText.class)).perform(typeText("Afra Abdelkader"), pressImeActionButton());
        onView(anyOf(withText("Afra Abdelkader"), withId(R.id.clientFullNameTxt))).perform(click());
        onView(ViewMatchers.withId(R.id.clientListView)).check(new RecyclerViewItemCountAssertion(not(countBefore)));
        onView(ViewMatchers.withId(R.id.clientListView)).check(new RecyclerViewItemCountAssertion(greaterThan(0)));

    }

    @After
    public void unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoTestingIdlingResource.getIdlingResource());

    }
}
