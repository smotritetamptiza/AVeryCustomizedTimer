package com.deadanddeceased.timeloop;

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

        Timer exampleTimer = new Timer("Попей водички", 1800);
        timers.add(exampleTimer);
        adapter.notifyDataSetChanged();
    }

    public void addNewTimer(View view) {
        // we need to create an empty timer here, I guess
        Intent intent = new Intent(this, EditTimerActivity.class);
        startActivity(intent);
    }
}