package com.code.wlu.noraibarguren.androidassignments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowToast;

@RunWith(RobolectricTestRunner.class)
public class LoginActivityUnitTest {

    private LoginActivity activity;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Before
    public void setUp() {
        // empezar sin ningun email guardado
        RuntimeEnvironment.getApplication()
                .getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).edit().clear().commit();

        activity = Robolectric.buildActivity(LoginActivity.class).setup().get();
        emailEditText = activity.findViewById(R.id.emailEditText);
        passwordEditText = activity.findViewById(R.id.passwordEditText);
        loginButton = activity.findViewById(R.id.loginButton);
    }

    @Test
    public void defaultEmailIsShownOnFirstLaunch() {
        assertEquals("email@domain.com", emailEditText.getText().toString());
    }

    @Test
    public void emptyEmailShowsErrorAndDoesNotNavigate() {
        emailEditText.setText("");
        passwordEditText.setText("1234");
        loginButton.performClick();

        assertEquals(activity.getString(R.string.login_email_dialog), ShadowToast.getTextOfLatestToast());
        assertNull(shadowOf(activity).getNextStartedActivity());
    }

    @Test
    public void invalidEmailFormatShowsErrorAndDoesNotNavigate() {
        emailEditText.setText("abc");
        passwordEditText.setText("1234");
        loginButton.performClick();

        assertEquals(activity.getString(R.string.login_email_dialog), ShadowToast.getTextOfLatestToast());
        assertNull(shadowOf(activity).getNextStartedActivity());
    }

    @Test
    public void emptyPasswordShowsErrorAndDoesNotNavigate() {
        emailEditText.setText("test@example.com");
        passwordEditText.setText("");
        loginButton.performClick();

        assertEquals(activity.getString(R.string.login_password_dialog), ShadowToast.getTextOfLatestToast());
        assertNull(shadowOf(activity).getNextStartedActivity());
    }

    @Test
    public void validLoginSavesEmailAndStartsMainActivity() {
        emailEditText.setText("test@example.com");
        passwordEditText.setText("1234");
        loginButton.performClick();

        // se inicia MainActivity
        Intent next = shadowOf(activity).getNextStartedActivity();
        assertNotNull(next);
        assertEquals(MainActivity.class.getName(), next.getComponent().getClassName());

        // el email queda guardado en SharedPreferences
        SharedPreferences prefs = activity.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        assertEquals("test@example.com", prefs.getString("DefaultEmail", ""));
    }

    @Test
    public void upButtonFinishesActivity() {
        assertTrue(activity.onOptionsItemSelected(new RoboMenuItem(android.R.id.home)));
        assertTrue(activity.isFinishing());
    }
}
