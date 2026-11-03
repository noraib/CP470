package com.code.wlu.noraibarguren.androidassignments;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TestToolbarTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void openTestToolbar() {
        // from MainActivity, push Test Toolbar button
        onView(withId(R.id.testToolbarButton)).perform(click());
    }

    @Test
    public void testToolbarShowsThreeIconsAndAbout() {
        // the 3 icons are visible in toolbar
        onView(withId(R.id.action_one)).check(matches(isDisplayed()));
        onView(withId(R.id.action_two)).check(matches(isDisplayed()));
        onView(withId(R.id.action_three)).check(matches(isDisplayed()));

        // About item is in the 3 points
        openActionBarOverflowOrOptionsMenu(
                InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(R.string.toolbar_about)).check(matches(isDisplayed()));
    }

    @Test
    public void testAboutItemClosesMenuAndStaysInActivity() {
        openActionBarOverflowOrOptionsMenu(
                InstrumentationRegistry.getInstrumentation().getTargetContext());
        onView(withText(R.string.toolbar_about)).perform(click());

        // we still are in TestToolbar
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void testItemOneShowsSnackbar() {
        // item 1 -> Snackbar with default message
        onView(withId(R.id.action_one)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.snackbar_item_one)));
    }

    @Test
    public void testFabShowsSnackbar() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.fab_message)));
    }

    @Test
    public void testItemTwoNegativeButtonStaysInActivity() {
        // item 2 -> dialog "Do you want to go back?"
        onView(withId(R.id.action_two)).perform(click());
        onView(withText(R.string.dialog_back_title)).check(matches(isDisplayed()));

        // negative button -> activity doesn't finish
        onView(withText(R.string.dialog_back_no)).perform(click());
        onView(withId(R.id.fab)).check(matches(isDisplayed()));
    }

    @Test
    public void testItemTwoPositiveButtonFinishesActivity() {
        // item 2 -> positive button -> goes back to MainActivity
        onView(withId(R.id.action_two)).perform(click());
        onView(withText(R.string.dialog_back_yes)).perform(click());
        onView(withId(R.id.btnGoToList)).check(matches(isDisplayed()));
    }

    @Test
    public void testItemThreeSetsNewSnackbarMessage() {
        // item 3 -> personalized dialog: write new message and say OK
        onView(withId(R.id.action_three)).perform(click());
        onView(withId(R.id.new_message)).perform(typeText("Mensaje nuevo"), closeSoftKeyboard());
        onView(withText(R.string.dialog_ok)).perform(click());

        // item 1 -> Snackbar shows new message
        onView(withId(R.id.action_one)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText("Mensaje nuevo")));
    }

    @Test
    public void testItemThreeCancelKeepsDefaultMessage() {
        onView(withId(R.id.action_three)).perform(click());
        onView(withId(R.id.new_message)).perform(typeText("No guardar"), closeSoftKeyboard());
        onView(withText(R.string.dialog_cancel)).perform(click());

        onView(withId(R.id.action_one)).perform(click());
        onView(withId(com.google.android.material.R.id.snackbar_text))
                .check(matches(withText(R.string.snackbar_item_one)));
    }

    @Test
    public void testUpButtonReturnsToMain() {
        // Up arrow (parentActivityName = MainActivity)
        onView(withContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description))
                .perform(click());
        onView(withId(R.id.btnGoToList)).check(matches(isDisplayed()));
    }
}
