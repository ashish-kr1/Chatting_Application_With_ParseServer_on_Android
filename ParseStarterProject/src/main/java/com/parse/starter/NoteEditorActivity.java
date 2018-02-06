package com.parse.starter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {
    int noteId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.crct) {

            Intent intent = new Intent(getApplicationContext(), notepad.class);

            startActivity(intent);

            return true;

        }

        return false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

setTitle("Write Here....");
        EditText editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);

        if (noteId != -1) {

            editText.setText(notepad.notes.get(noteId));

        } else {

            notepad.notes.add("");
            noteId = notepad.notes.size() - 1;
            notepad.arrayAdapter.notifyDataSetChanged();

        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                notepad.notes.set(noteId, String.valueOf(charSequence));
                notepad.arrayAdapter.notifyDataSetChanged();

                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.parse.starter", Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet(notepad.notes);

                sharedPreferences.edit().putStringSet("notes", set).apply();



            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
