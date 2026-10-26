package com.code.wlu.noraibarguren.androidassignments;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class ChatUnitTest {

    @Test
    public void testAddMessageToList() {
        ArrayList<String> messages = new ArrayList<>();
        messages.add("Kaixo");
        messages.add("Agur");

        // Verifica que la lista contiene 2 elementos
        assertEquals(2, messages.size());

        // Verifica el contenido del primer mensaje
        assertEquals("Kaixo", messages.get(0));
    }

    @Test
    public void testPositionEvenOdd() {
        int positionEven = 0;
        int positionOdd = 1;

        // Comprueba la lógica de filas pares/impares para el chat
        assertTrue(positionEven % 2 == 0);
        assertFalse(positionOdd % 2 == 0);
    }
}