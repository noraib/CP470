package com.code.wlu.noraibarguren.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    // Etiqueta para identificar esta actividad en el Logcat
    protected static final String ACTIVITY_NAME = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // obtener los botones creados en activity_main.xml
        Button btnGoToLogin = findViewById(R.id.btnGoToLogin);
        Button btnGoToList = findViewById(R.id.btnGoToList);

        // abrir LoginActivity al hacer click
        btnGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // abrir ListItemsActivity al hacer click
        btnGoToList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListItemsActivity.class);
            startActivityForResult(intent, 10);
        });

        // abrir chat al hacer click
        Button startChatButton = findViewById(R.id.startChatButton);
        startChatButton.setOnClickListener(v -> {
            Log.i(ACTIVITY_NAME, "User clicked Start Chat");
            Intent intent = new Intent(MainActivity.this, ChatWindow.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "onResume() called");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "onStart() called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "onPause() called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "onStop() called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "onDestroy() called");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.i(ACTIVITY_NAME, "onSaveInstanceState() called");
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(ACTIVITY_NAME, "onRestoreInstanceState() called");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verificar si el requestCode es 10 y si el resultCode es Activity.RESULT_OK
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {

            // Verificar que los datos de retorno no sean nulos
            if (data != null) {
                // Extraer el mensaje pasado con la clave "Response"
                String messagePassed = data.getStringExtra("Response");

                // Mostrar el Toast con el mensaje requerido
                String toastText = "ListItemsActivity passed: " + messagePassed;
                Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
            }
        }
    }
}