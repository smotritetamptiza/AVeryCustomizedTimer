package com.deadanddeceased.timeloop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



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

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE);
        Set<String> timersStringSet = new HashSet<>();
        timersStringSet.clear();
        timersStringSet = sharedPref.getStringSet("timers", timersStringSet);
        stringsToTimers(timersStringSet);

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
                    setAlarm(timers.size() - 1);
                } else {
                    timers.get(position).edit(name, interval, wasActive);
                }
                adapter.notifyDataSetChanged();
            }
        }
        for (int i = 0; i < timers.size(); i++) {
            setAlarm(i);
        }
    }

    private void stringsToTimers(Set<String> timersStringSet) {
        timers.ensureCapacity(timersStringSet.size());
        for (String timer : timersStringSet) {
            String[] timerParts = timer.split(";");
            int ind = Integer.parseInt(timerParts[0]);
            String name = timerParts[1];
            int secondsTotal = Integer.parseInt(timerParts[2]);
            int secondsLeft = Integer.parseInt(timerParts[3]);
            boolean wasActive = Boolean.parseBoolean(timerParts[4]);
            Timer newTimer = new Timer(name, secondsTotal);
            if (!wasActive) {
                newTimer.toggleActive();
            }
            newTimer.setSecondsLeft(secondsLeft);
            timers.add(newTimer);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        HashSet<String> stringTimers = timersToString();
        editor.putStringSet("timers", stringTimers);
        editor.apply();
    }

    private HashSet<String> timersToString() {
        HashSet<String> stringTimers = new HashSet<>();
        for (int i = 0; i < timers.size(); i++) {
            stringTimers.add(i + ";" + timers.get(i).toString());
        }
        return stringTimers;
    }

    public void addNewTimer(View view) {
        Intent intent = new Intent(this, EditTimerActivity.class);
        startActivity(intent);
    }

    private void setAlarm(int i) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        String name = timers.get(i).getName();
        intent.putExtra("name", name);
        intent.putExtra("id", i);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + timers.get(i).getSecondsTotal() * 1000,
                    timers.get(i).getSecondsTotal() * 1000, pendingIntent);
    }

    private void cancelAlarm(int i) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, 0);
        alarmManager.cancel(pendingIntent);
    }
}