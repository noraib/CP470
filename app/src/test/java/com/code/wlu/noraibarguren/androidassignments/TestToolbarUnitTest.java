package com.code.wlu.noraibarguren.androidassignments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Intent;
import android.os.Looper;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.fakes.RoboMenu;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class TestToolbarUnitTest {

    private TestToolbar activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(TestToolbar.class).setup().get();
    }

    private void idle() {
        shadowOf(Looper.getMainLooper()).idle();
    }

    private String snackbarText() {
        TextView text = activity.findViewById(com.google.android.material.R.id.snackbar_text);
        return text.getText().toString();
    }

    @Test
    public void menuHasThreeIconsAndAbout() {
        // onCreateOptionsMenu inflates toolbar_menu: 3 icons + About
        RoboMenu menu = new RoboMenu(activity);
        assertTrue(activity.onCreateOptionsMenu(menu));
        assertEquals(4, menu.size());
        assertNotNull(menu.findItem(R.id.action_one));
        assertNotNull(menu.findItem(R.id.action_two));
        assertNotNull(menu.findItem(R.id.action_three));
        assertNotNull(menu.findItem(R.id.action_about));
    }

    @Test
    public void itemOneShowsDefaultSnackbar() {
        assertTrue(activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_one)));
        idle();
        assertEquals(activity.getString(R.string.snackbar_item_one), snackbarText());
    }

    @Test
    public void fabShowsSnackbar() {
        activity.findViewById(R.id.fab).performClick();
        idle();
        assertEquals(activity.getString(R.string.fab_message), snackbarText());
    }

    @Test
    public void itemTwoNegativeKeepsActivity() {
        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_two));
        idle();
        AlertDialog dialog = (AlertDialog) ShadowDialog.getLatestDialog();
        assertTrue(dialog.isShowing());

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();
        idle();
        assertFalse(dialog.isShowing());
        assertFalse(activity.isFinishing());
    }

    @Test
    public void itemTwoPositiveFinishesActivity() {
        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_two));
        idle();
        AlertDialog dialog = (AlertDialog) ShadowDialog.getLatestDialog();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
        idle();
        assertTrue(activity.isFinishing());
    }

    @Test
    public void itemThreeSetsNewSnackbarMessage() {
        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_three));
        idle();
        AlertDialog dialog = (AlertDialog) ShadowDialog.getLatestDialog();
        EditText input = dialog.findViewById(R.id.new_message);
        input.setText("Mensaje nuevo");
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
        idle();

        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_one));
        idle();
        assertEquals("Mensaje nuevo", snackbarText());
    }

    @Test
    public void itemThreeEmptyOrCancelKeepsDefaultMessage() {
        // OK with empty message -> we keep default message
        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_three));
        idle();
        AlertDialog dialog = (AlertDialog) ShadowDialog.getLatestDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
        idle();

        // Cancel with text -> doesn't change either
        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_three));
        idle();
        dialog = (AlertDialog) ShadowDialog.getLatestDialog();
        ((EditText) dialog.findViewById(R.id.new_message)).setText("No guardar");
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();
        idle();

        activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_one));
        idle();
        assertEquals(activity.getString(R.string.snackbar_item_one), snackbarText());
    }

    @Test
    public void aboutItemShowsToast() {
        assertTrue(activity.onOptionsItemSelected(new RoboMenuItem(R.id.action_about)));
        assertEquals(activity.getString(R.string.about_message), ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void unknownItemIsNotHandled() {
        assertFalse(activity.onOptionsItemSelected(new RoboMenuItem(123456)));
    }

    @Test
    public void mainButtonStartsTestToolbar() {
        MainActivity main = Robolectric.buildActivity(MainActivity.class).setup().get();
        main.findViewById(R.id.testToolbarButton).performClick();
        Intent next = shadowOf(main).getNextStartedActivity();
        assertNotNull(next);
        assertEquals(TestToolbar.class.getName(), next.getComponent().getClassName());
    }
}
