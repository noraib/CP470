package com.code.wlu.noraibarguren.androidassignments;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
}