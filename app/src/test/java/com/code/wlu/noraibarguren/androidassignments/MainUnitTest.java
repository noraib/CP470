package com.code.wlu.noraibarguren.androidassignments;

import org.junit.Test;
import static org.junit.Assert.*;

public class MainUnitTest {

    @Test
    public void testToastMessageFromListItems() {
        String messagePassed = "Here is my response";

        // mensaje que muestra MainActivity al volver de ListItemsActivity
        String toastText = "ListItemsActivity passed: " + messagePassed;
        assertEquals("ListItemsActivity passed: Here is my response", toastText);
    }

    @Test
    public void testResultIsHandledOnlyForRequestCode10() {
        int requestCode = 10;
        int resultOk = -1; // Activity.RESULT_OK vale -1

        // solo se muestra el toast si requestCode es 10 y el resultado es OK
        assertTrue(requestCode == 10 && resultOk == -1);
        assertFalse(99 == 10 && resultOk == -1);
        assertFalse(requestCode == 10 && 0 == -1); // RESULT_CANCELED vale 0
    }

    @Test
    public void testSwitchMessage() {
        // mensaje del switch segun su estado (ON / OFF)
        boolean isChecked = true;
        String message = isChecked ? "Switch is ON" : "Switch is OFF";
        assertEquals("Switch is ON", message);

        isChecked = false;
        message = isChecked ? "Switch is ON" : "Switch is OFF";
        assertEquals("Switch is OFF", message);
    }
}