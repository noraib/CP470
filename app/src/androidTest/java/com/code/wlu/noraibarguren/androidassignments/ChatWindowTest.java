package com.code.wlu.noraibarguren.androidassignments;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChatWindowTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testStartChatAndSendMessage() {
        // clic en el boton "Start Chat" en MainActivity
        onView(withId(R.id.startChatButton)).perform(click());

        // comprobar que la vista ChatWindow esta visible
        onView(withId(R.id.sendButton)).check(matches(isDisplayed()));

        // escribir un mensaje en el EditText
        onView(withId(R.id.chatEditText)).perform(typeText("Test mensaje"), closeSoftKeyboard());

        // pulsar el boton Send
        onView(withId(R.id.sendButton)).perform(click());

        // comprobar que el EditText quedo limpio tras enviar
        onView(withId(R.id.chatEditText)).check(matches(withText("")));
    }

    @Test
    public void testNavigateToLoginAndList() {
        // entrar en LoginActivity y comprobar que carga
        onView(withId(R.id.btnGoToLogin)).perform(click());

        // volver a MainActivity
        androidx.test.espresso.Espresso.pressBack();

        // entrar en ListItemsActivity
        onView(withId(R.id.btnGoToList)).perform(click());

        // volver a MainActivity
        androidx.test.espresso.Espresso.pressBack();
    }

    @Test
    public void testMessagesAppearInBothRowTypes() {
        // abrir el chat desde MainActivity
        onView(withId(R.id.startChatButton)).perform(click());

        // enviar el primer mensaje (posicion 0 -> chat_row_incoming)
        onView(withId(R.id.chatEditText)).perform(typeText("primero"), closeSoftKeyboard());
        onView(withId(R.id.sendButton)).perform(click());

        // enviar el segundo mensaje (posicion 1 -> chat_row_outgoing)
        onView(withId(R.id.chatEditText)).perform(typeText("segundo"), closeSoftKeyboard());
        onView(withId(R.id.sendButton)).perform(click());

        // comprobar que cada fila muestra su texto en el ListView
        onData(anything()).inAdapterView(withId(R.id.chatView)).atPosition(0)
                .onChildView(withId(R.id.message_text)).check(matches(withText("primero")));
        onData(anything()).inAdapterView(withId(R.id.chatView)).atPosition(1)
                .onChildView(withId(R.id.message_text)).check(matches(withText("segundo")));
    }

    @Test
    public void testEmptyMessageIsNotAdded() {
        // abrir el chat desde MainActivity
        onView(withId(R.id.startChatButton)).perform(click());

        // pulsar Send con el EditText vacio
        onView(withId(R.id.sendButton)).perform(click());

        // pulsar Send con solo espacios en blanco
        onView(withId(R.id.chatEditText)).perform(typeText("   "), closeSoftKeyboard());
        onView(withId(R.id.sendButton)).perform(click());

        // comprobar que no se agrego ninguna fila al ListView
        onView(withId(R.id.chatView)).check(matches(hasChildCount(0)));
    }

    @Test
    public void testBackFromChatReturnsToMain() {
        // abrir el chat desde MainActivity
        onView(withId(R.id.startChatButton)).perform(click());

        // pulsar el boton Back de Android
        androidx.test.espresso.Espresso.pressBack();

        // comprobar que se volvio a MainActivity
        onView(withId(R.id.startChatButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testMainActivityRecreate() {
        // recrear la actividad: fuerza onSaveInstanceState, onRestoreInstanceState y onDestroy
        activityRule.getScenario().recreate();

        // comprobar que MainActivity sigue mostrando sus botones
        onView(withId(R.id.btnGoToLogin)).check(matches(isDisplayed()));
    }

    // ------------------------------------------------------------------
    // LoginActivity
    // ------------------------------------------------------------------

    @Test
    public void testLoginWithEmptyEmail() {
        // entrar en LoginActivity desde MainActivity
        onView(withId(R.id.btnGoToLogin)).perform(click());

        // dejar el email vacio y pulsar el boton de login
        onView(withId(R.id.emailEditText)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // comprobar que se queda en LoginActivity (no avanza)
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithInvalidEmailFormat() {
        // entrar en LoginActivity desde MainActivity
        onView(withId(R.id.btnGoToLogin)).perform(click());

        // escribir un email con formato invalido
        onView(withId(R.id.emailEditText)).perform(replaceText("abc"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // comprobar que se queda en LoginActivity
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginWithEmptyPassword() {
        // entrar en LoginActivity desde MainActivity
        onView(withId(R.id.btnGoToLogin)).perform(click());

        // email valido pero sin contrasena
        onView(withId(R.id.emailEditText)).perform(replaceText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(replaceText(""), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // comprobar que se queda en LoginActivity
        onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginSuccessSavesEmail() {
        // entrar en LoginActivity desde MainActivity
        onView(withId(R.id.btnGoToLogin)).perform(click());

        // escribir email y contrasena validos y pulsar login
        onView(withId(R.id.emailEditText)).perform(replaceText("test@example.com"), closeSoftKeyboard());
        onView(withId(R.id.passwordEditText)).perform(typeText("1234"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // comprobar que se abrio MainActivity
        onView(withId(R.id.btnGoToLogin)).check(matches(isDisplayed()));

        // volver a LoginActivity y comprobar que el email quedo guardado en SharedPreferences
        pressBack();
        onView(withId(R.id.emailEditText)).check(matches(withText("test@example.com")));
    }

    @Test
    public void testLoginUpButtonFinishesActivity() {
        // entrar en LoginActivity desde MainActivity
        onView(withId(R.id.btnGoToLogin)).perform(click());

        // pulsar la flecha Up de la barra superior (onOptionsItemSelected -> finish)
        onView(withContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description))
                .perform(click());

        // comprobar que se volvio a MainActivity
        onView(withId(R.id.btnGoToLogin)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginActivityRecreate() {
        // lanzar LoginActivity directamente y recrearla para cubrir el ciclo de vida
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {
            scenario.recreate();
            onView(withId(R.id.loginButton)).check(matches(isDisplayed()));
        }
    }

    // ------------------------------------------------------------------
    // ListItemsActivity
    // ------------------------------------------------------------------

    @Test
    public void testSwitchToggleOnAndOff() {
        // entrar en ListItemsActivity desde MainActivity
        onView(withId(R.id.btnGoToList)).perform(click());

        // activar el switch (isChecked = true -> switch_on)
        onView(withId(R.id.switchWidget)).perform(click());

        // desactivar el switch (isChecked = false -> switch_off)
        onView(withId(R.id.switchWidget)).perform(click());

        // comprobar que seguimos en ListItemsActivity
        onView(withId(R.id.switchWidget)).check(matches(isDisplayed()));
    }

    @Test
    public void testCheckBoxDialogCancel() {
        // entrar en ListItemsActivity desde MainActivity
        onView(withId(R.id.btnGoToList)).perform(click());

        // marcar el checkbox -> aparece el dialogo "Finish Activity?"
        onView(withId(R.id.checkBox)).perform(click());
        onView(withText(R.string.dialog_title)).check(matches(isDisplayed()));

        // pulsar Cancel -> el dialogo se cierra (dialog.dismiss())
        onView(withText(R.string.cancel)).perform(click());

        // comprobar que seguimos en ListItemsActivity
        onView(withId(R.id.checkBox)).check(matches(isDisplayed()));

        // desmarcar el checkbox (isChecked = false -> no aparece dialogo)
        onView(withId(R.id.checkBox)).perform(click());
        onView(withId(R.id.checkBox)).check(matches(isDisplayed()));
    }

    @Test
    public void testCheckBoxDialogOkReturnsToMain() {
        // entrar en ListItemsActivity desde MainActivity
        onView(withId(R.id.btnGoToList)).perform(click());

        // marcar el checkbox y pulsar Yes en el dialogo (setResult RESULT_OK + finish)
        onView(withId(R.id.checkBox)).perform(click());
        onView(withText(R.string.ok)).perform(click());

        // comprobar que se volvio a MainActivity (onActivityResult muestra el Toast)
        onView(withId(R.id.btnGoToList)).check(matches(isDisplayed()));
    }

    @Test
    public void testListItemsUpButtonFinishesActivity() {
        // entrar en ListItemsActivity desde MainActivity
        onView(withId(R.id.btnGoToList)).perform(click());

        // pulsar la flecha Up de la barra superior (onOptionsItemSelected -> finish)
        onView(withContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description))
                .perform(click());

        // comprobar que se volvio a MainActivity
        onView(withId(R.id.btnGoToList)).check(matches(isDisplayed()));
    }

    @Test
    public void testCameraResultReplacesImageButton() {
        // lanzar ListItemsActivity directamente
        try (ActivityScenario<ListItemsActivity> scenario = ActivityScenario.launch(ListItemsActivity.class)) {
            scenario.onActivity(activity -> {
                // simular la respuesta de la camara con un Bitmap en los extras
                Bundle extras = new Bundle();
                extras.putParcelable("data", Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888));
                Intent data = new Intent();
                data.putExtras(extras);

                // requestCode correcto + RESULT_OK -> reemplaza la imagen del ImageButton
                activity.onActivityResult(1, Activity.RESULT_OK, data);

                // data nulo -> rama en la que no se hace nada
                activity.onActivityResult(1, Activity.RESULT_OK, null);

                // requestCode distinto -> se ignora
                activity.onActivityResult(99, Activity.RESULT_OK, data);
            });

            // comprobar que el ImageButton sigue visible
            onView(withId(R.id.imageButton)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testListItemsActivityRecreate() {
        // lanzar ListItemsActivity directamente y recrearla para cubrir el ciclo de vida
        try (ActivityScenario<ListItemsActivity> scenario = ActivityScenario.launch(ListItemsActivity.class)) {
            scenario.recreate();
            onView(withId(R.id.checkBox)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void testChatUpButtonReturnsToMain() {
        // abrir el chat desde MainActivity
        onView(withId(R.id.startChatButton)).perform(click());

        // pulsar la flecha Up (parentActivityName = MainActivity)
        onView(withContentDescription(androidx.appcompat.R.string.abc_action_bar_up_description))
                .perform(click());

        // comprobar que se volvio a MainActivity
        onView(withId(R.id.startChatButton)).check(matches(isDisplayed()));
    }
}