package com.deadanddeceased.timeloop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
        if (intent != null && intent.hasExtra("name") && intent.hasExtra("interval")) {
            String name = intent.getStringExtra("name");
            int interval = intent.getIntExtra("interval", 1800);
            nameEdit.setText(name);
            int hours = interval / 3600;
            int minutes = (interval % 3600) / 60;
            int seconds = interval % 60;
            intervalEdit.setText(String.format(Locale.getDefault(), getString(R.string.timeFormat),
                    hours, minutes, seconds));
        }
    }

    public void saveTimer(View view) {
        // the actual saving
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}