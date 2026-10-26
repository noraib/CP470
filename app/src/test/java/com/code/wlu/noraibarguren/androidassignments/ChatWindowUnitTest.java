package com.code.wlu.noraibarguren.androidassignments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class ChatWindowUnitTest {

    private ListView listView;
    private EditText chatEditText;
    private Button sendButton;

    @Before
    public void setUp() {
        // crear ChatWindow y obtener sus vistas
        ChatWindow activity = Robolectric.buildActivity(ChatWindow.class).setup().get();
        listView = activity.findViewById(R.id.chatView);
        chatEditText = activity.findViewById(R.id.chatEditText);
        sendButton = activity.findViewById(R.id.sendButton);
    }

    private void send(String text) {
        chatEditText.setText(text);
        sendButton.performClick();
    }

    @Test
    public void adapterStartsEmpty() {
        // al abrir el chat no hay mensajes
        assertEquals(0, listView.getAdapter().getCount());
    }

    @Test
    public void sendMessageAddsItemAndClearsEditText() {
        send("hola");

        // getCount() y getItem() del ChatAdapter
        assertEquals(1, listView.getAdapter().getCount());
        assertEquals("hola", listView.getAdapter().getItem(0));

        // el EditText queda limpio
        assertEquals("", chatEditText.getText().toString());
    }

    @Test
    public void emptyOrBlankMessageIsNotAdded() {
        send("");
        send("   ");
        assertEquals(0, listView.getAdapter().getCount());
    }

    @Test
    public void multipleMessagesKeepOrder() {
        send("uno");
        send("dos");
        send("tres");

        ListAdapter adapter = listView.getAdapter();
        assertEquals(3, adapter.getCount());
        assertEquals("uno", adapter.getItem(0));
        assertEquals("dos", adapter.getItem(1));
        assertEquals("tres", adapter.getItem(2));
    }

    @Test
    public void getViewAlternatesIncomingAndOutgoingRows() {
        send("a");
        send("b");
        ListAdapter adapter = listView.getAdapter();

        // posicion par -> chat_row_incoming (el TextView va primero)
        View even = adapter.getView(0, null, listView);
        assertTrue(((ViewGroup) even).getChildAt(0) instanceof TextView);
        assertEquals("a", ((TextView) even.findViewById(R.id.message_text)).getText().toString());

        // posicion impar -> chat_row_outgoing (el ImageView va primero)
        View odd = adapter.getView(1, null, listView);
        assertTrue(((ViewGroup) odd).getChildAt(0) instanceof ImageView);
        assertEquals("b", ((TextView) odd.findViewById(R.id.message_text)).getText().toString());
    }
}
