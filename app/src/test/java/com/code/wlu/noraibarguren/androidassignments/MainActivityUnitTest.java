package com.code.wlu.noraibarguren.androidassignments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.Shadows.shadowOf;

import android.app.Activity;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class MainActivityUnitTest {

    private MainActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(MainActivity.class).setup().get();
    }

    @Test
    public void loginButtonStartsLoginActivity() {
        activity.findViewById(R.id.btnGoToLogin).performClick();
        Intent next = shadowOf(activity).getNextStartedActivity();
        assertNotNull(next);
        assertEquals(LoginActivity.class.getName(), next.getComponent().getClassName());
    }

    @Test
    public void listButtonStartsListItemsActivityForResult() {
        activity.findViewById(R.id.btnGoToList).performClick();
        ShadowActivity.IntentForResult started = shadowOf(activity).getNextStartedActivityForResult();
        assertNotNull(started);
        assertEquals(ListItemsActivity.class.getName(), started.intent.getComponent().getClassName());
        assertEquals(10, started.requestCode);
    }

    @Test
    public void startChatButtonStartsChatWindow() {
        activity.findViewById(R.id.startChatButton).performClick();
        Intent next = shadowOf(activity).getNextStartedActivity();
        assertNotNull(next);
        assertEquals(ChatWindow.class.getName(), next.getComponent().getClassName());
    }

    @Test
    public void onActivityResultShowsToastWithResponse() {
        Intent data = new Intent();
        data.putExtra("Response", "Here is my response");
        activity.onActivityResult(10, Activity.RESULT_OK, data);
        assertEquals("ListItemsActivity passed: Here is my response", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void onActivityResultIgnoresOtherCodes() {
        ShadowToast.reset();
        activity.onActivityResult(99, Activity.RESULT_OK, new Intent());
        activity.onActivityResult(10, Activity.RESULT_CANCELED, new Intent());
        activity.onActivityResult(10, Activity.RESULT_OK, null);
        // ninguna de estas llamadas debe mostrar un Toast
        assertEquals(0, ShadowToast.shownToastCount());
    }
}
