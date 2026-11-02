package com.code.wlu.noraibarguren.androidassignments;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class TestToolbar extends AppCompatActivity {

    // tag for Logcat
    protected static final String ACTIVITY_NAME = "TestToolbar";

    // message shown in Snackbar when choosing item 1(item3 can be changed)
    private String snackbarMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_test_toolbar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.i(ACTIVITY_NAME, "onCreate() called");

        // default message for item one
        snackbarMessage = getString(R.string.snackbar_item_one);

        // floating button: Snackbar with message
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view, getString(R.string.fab_message), Snackbar.LENGTH_LONG).show());

    }

    // creates toolbar inflating menu from XML
    @Override
    public boolean onCreateOptionsMenu(Menu m) {
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    // replies to selected item
    @Override
    public boolean onOptionsItemSelected(MenuItem mi) {
        int id = mi.getItemId();

        if (id == R.id.action_one) {
            Log.d("Toolbar", "Option 1 selected");
            Snackbar.make(findViewById(R.id.main), snackbarMessage, Snackbar.LENGTH_LONG).show();

        } else if (id == R.id.action_two) {
            Log.d("Toolbar", "Option 2 selected");
            showGoBackDialog();


        } else if (id == R.id.action_three) {
            Log.d("Toolbar", "Option 3 selected");
            showCustomDialog();


        } else if (id == R.id.action_about) {
            Log.d("Toolbar", "About selected");
            Toast.makeText(this, getString(R.string.about_message), Toast.LENGTH_LONG).show();

        } else {
            //managed by parent
            return super.onOptionsItemSelected(mi);
        }
        return true;
    }


    // item 2: dialog "Do you want to go back?"
    private void showGoBackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_back_title);

        // positive button: ends the current activity
        builder.setPositiveButton(R.string.dialog_back_yes, (dialog, id) -> finish());

        // negative button: does nothing
        builder.setNegativeButton(R.string.dialog_back_no, (dialog, id) -> {
            // user cancelled the dialog: if they select the negative button, do nothing
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // item 3: dialog with its own layout to type the new Snackbar message
    private void showCustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_custom_message, null);
        EditText newMessage = dialogView.findViewById(R.id.new_message);

        builder.setView(dialogView)
                .setTitle(R.string.dialog_new_message_title)
                .setPositiveButton(R.string.dialog_ok, (dialog, id) -> {
                    // the typed text becomes the message of item 1's Snackbar
                    String text = newMessage.getText().toString().trim();
                    if (!text.isEmpty()) {
                        snackbarMessage = text;
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, (dialog, id) -> dialog.dismiss());

        builder.create().show();
    }
}