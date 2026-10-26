package com.code.wlu.noraibarguren.androidassignments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    // declarar variables de clase
    private ListView listView;
    private EditText chatEditText;
    private Button sendButton;
    private ArrayList<String> messageList;
    private ChatAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        // inicializar componentes mediante findViewById()
        listView = findViewById(R.id.chatView);
        chatEditText = findViewById(R.id.chatEditText);
        sendButton = findViewById(R.id.sendButton);

        // inicializar el ArrayList para almacenar los mensajes
        messageList = new ArrayList<>();

        // instanciar y asignar el adaptador al ListView
        messageAdapter = new ChatAdapter(this);
        listView.setAdapter(messageAdapter);

        // evento onClick para el boton Send
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // obtener el texto del EditText
                String message = chatEditText.getText().toString();

                // agregar el texto a la lista si no esta vacio
                if (!message.trim().isEmpty()) {
                    messageList.add(message);
                    messageAdapter.notifyDataSetChanged(); // notifica cambios a la lista
                    chatEditText.setText(""); // limpia el campo
                }
            }
        });
    }
    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0); // pasamos 0 porque usaremos layouts personalizados en getView
        }

        @Override
        public int getCount() {
            return messageList.size(); // devuelve la cantidad de mensajes
        }

        @Override
        public String getItem(int position) {
            return messageList.get(position); // devuelve el mensaje en la posición requerida
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result;

            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }

            TextView message = result.findViewById(R.id.message_text);
            message.setText(getItem(position));

            return result;
        }
    }
}