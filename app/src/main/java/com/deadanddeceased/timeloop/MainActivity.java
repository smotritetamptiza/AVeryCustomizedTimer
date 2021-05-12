package com.deadanddeceased.timeloop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Timer> timers;
    private RecyclerView timersView;
    private TimerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timersView = findViewById(R.id.timersRecyclerView);
        timers = new ArrayList<>();

        adapter = new TimerAdapter(this, timers);
        timersView.setAdapter(adapter);
        timersView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("position")) {
            if (intent.hasExtra("name") && intent.hasExtra("interval")) {
                String name = intent.getStringExtra("name");
                int interval = intent.getIntExtra("interval", 1800);
                int position = intent.getIntExtra("position", -1);
                boolean wasActive = intent.getBooleanExtra("wasActive", true);
                if (position < 0) {
                    timers.add(new Timer(name, interval));
                } else {
                    timers.get(position).edit(name, interval, wasActive);
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void addNewTimer(View view) {
        Intent intent = new Intent(this, EditTimerActivity.class);
        startActivity(intent);
    }
}