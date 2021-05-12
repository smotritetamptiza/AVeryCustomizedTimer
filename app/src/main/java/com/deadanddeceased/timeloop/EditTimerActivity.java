package com.deadanddeceased.timeloop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Time;
import java.util.Locale;

public class EditTimerActivity extends AppCompatActivity {
    private EditText nameEdit;
    private EditText intervalEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_timer);

        nameEdit = findViewById(R.id.editTextTimerName);
        intervalEdit = findViewById(R.id.editTextInterval);

        Intent intent = getIntent();
    }

    public void saveTimer(View view) {
        // the actual saving
        String name = nameEdit.getText().toString();
        String intervalString = intervalEdit.getText().toString();
        if (name.isEmpty() || intervalString.isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.edit_error, Toast.LENGTH_SHORT).show();
            return;
        }
        int interval = 0;
        try {
            interval = Integer.parseInt(intervalString);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), R.string.interval_error, Toast.LENGTH_SHORT).show();
            return;
        }
        if (interval <= 0 || interval >= 1440) {
            Toast.makeText(getApplicationContext(), R.string.interval_size_error, Toast.LENGTH_SHORT).show();
            return;
        }
        interval *= 60;
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("interval", interval);
        startActivity(intent);
    }
}