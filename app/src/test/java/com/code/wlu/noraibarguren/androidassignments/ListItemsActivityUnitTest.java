package com.code.wlu.noraibarguren.androidassignments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class ListItemsActivityUnitTest {

    private ListItemsActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(ListItemsActivity.class).setup().get();
    }

    private AlertDialog showDialogByCheckingBox() {
        CheckBox checkBox = activity.findViewById(R.id.checkBox);
        checkBox.performClick();
        shadowOf(Looper.getMainLooper()).idle();
        return (AlertDialog) ShadowDialog.getLatestDialog();
    }

    @Test
    public void switchShowsOnAndOffToasts() {
        Switch switchWidget = activity.findViewById(R.id.switchWidget);

        switchWidget.performClick();
        assertEquals(activity.getString(R.string.switch_on), ShadowToast.getTextOfLatestToast());

        switchWidget.performClick();
        assertEquals(activity.getString(R.string.switch_off), ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void printShowsToastWithMessage() {
        activity.print("hola");
        assertEquals("hola", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void checkingCheckBoxShowsDialog() {
        AlertDialog dialog = showDialogByCheckingBox();
        assertNotNull(dialog);
        assertTrue(dialog.isShowing());
    }

    @Test
    public void dialogYesReturnsResultAndFinishes() {
        AlertDialog dialog = showDialogByCheckingBox();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();
        shadowOf(Looper.getMainLooper()).idle();

        ShadowActivity shadow = shadowOf(activity);
        assertTrue(activity.isFinishing());
        assertEquals(Activity.RESULT_OK, shadow.getResultCode());
        assertEquals("Here is my response", shadow.getResultIntent().getStringExtra("Response"));
    }

    @Test
    public void dialogCancelDismissesWithoutFinishing() {
        AlertDialog dialog = showDialogByCheckingBox();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();
        shadowOf(Looper.getMainLooper()).idle();

        assertFalse(dialog.isShowing());
        assertFalse(activity.isFinishing());
    }

    @Test
    public void uncheckingCheckBoxDoesNotShowNewDialog() {
        AlertDialog first = showDialogByCheckingBox();
        first.getButton(AlertDialog.BUTTON_NEGATIVE).performClick();
        shadowOf(Looper.getMainLooper()).idle();

        // desmarcar el checkbox -> no debe aparecer otro dialogo
        CheckBox checkBox = activity.findViewById(R.id.checkBox);
        checkBox.performClick();
        shadowOf(Looper.getMainLooper()).idle();
        assertSame(first, ShadowDialog.getLatestDialog());
    }

    @Test
    public void upButtonFinishesActivity() {
        assertTrue(activity.onOptionsItemSelected(new RoboMenuItem(android.R.id.home)));
        assertTrue(activity.isFinishing());
    }

    @Test
    public void cameraButtonWithPermissionLaunchesCameraIntent() {
        shadowOf(RuntimeEnvironment.getApplication()).grantPermissions(Manifest.permission.CAMERA);

        ImageButton imageButton = activity.findViewById(R.id.imageButton);
        imageButton.performClick();

        ShadowActivity.IntentForResult started = shadowOf(activity).getNextStartedActivityForResult();
        assertNotNull(started);
        assertEquals(MediaStore.ACTION_IMAGE_CAPTURE, started.intent.getAction());
        assertEquals(1, started.requestCode);
    }

    @Test
    public void cameraButtonWithoutPermissionDoesNotFinish() {
        ImageButton imageButton = activity.findViewById(R.id.imageButton);
        imageButton.performClick();
        assertFalse(activity.isFinishing());
    }

    @Test
    public void cameraResultReplacesImageButtonPicture() {
        ImageButton imageButton = activity.findViewById(R.id.imageButton);
        Drawable before = imageButton.getDrawable();

        Bundle extras = new Bundle();
        extras.putParcelable("data", Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888));
        Intent data = new Intent();
        data.putExtras(extras);

        // requestCode correcto + RESULT_OK -> cambia la imagen
        activity.onActivityResult(1, Activity.RESULT_OK, data);
        Drawable after = imageButton.getDrawable();
        assertNotSame(before, after);

        // data nulo o requestCode distinto -> no cambia nada
        activity.onActivityResult(1, Activity.RESULT_OK, null);
        assertSame(after, imageButton.getDrawable());
        activity.onActivityResult(99, Activity.RESULT_OK, data);
        assertSame(after, imageButton.getDrawable());
    }
}
