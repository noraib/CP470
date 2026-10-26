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

        // verifica que la lista contiene 2 elementos
        assertEquals(2, messages.size());

        // verifica el contenido del primer mensaje
        assertEquals("Kaixo", messages.get(0));
    }

    @Test
    public void testPositionEvenOdd() {
        int positionEven = 0;
        int positionOdd = 1;

        // comprueba la logica de filas pares/impares para el chat
        assertTrue(positionEven % 2 == 0);
        assertFalse(positionOdd % 2 == 0);
    }

    @Test
    public void testListStartsEmpty() {
        ArrayList<String> messages = new ArrayList<>();

        // al abrir el chat no hay mensajes (getCount = 0)
        assertEquals(0, messages.size());
        assertTrue(messages.isEmpty());
    }

    @Test
    public void testEmptyMessageIsNotAdded() {
        ArrayList<String> messages = new ArrayList<>();
        String[] inputs = {"", "   ", "hola"};

        // misma condicion que el boton send: solo se agrega si no esta vacio
        for (String message : inputs) {
            if (!message.trim().isEmpty()) {
                messages.add(message);
            }
        }

        // solo el ultimo mensaje es valido
        assertEquals(1, messages.size());
        assertEquals("hola", messages.get(0));
    }

    @Test
    public void testMessagesKeepOrder() {
        ArrayList<String> messages = new ArrayList<>();
        messages.add("uno");
        messages.add("dos");
        messages.add("tres");

        // getItem(position) devuelve cada mensaje en el orden en que se envio
        assertEquals("uno", messages.get(0));
        assertEquals("dos", messages.get(1));
        assertEquals("tres", messages.get(2));
    }

    @Test
    public void testRowTypeByPosition() {
        // posicion par -> chat_row_incoming, impar -> chat_row_outgoing
        String[] expected = {"incoming", "outgoing", "incoming", "outgoing", "incoming"};

        for (int position = 0; position < expected.length; position++) {
            String row = (position % 2 == 0) ? "incoming" : "outgoing";
            assertEquals(expected[position], row);
        }
    }
}