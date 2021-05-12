package com.deadanddeceased.timeloop;

import android.app.AlarmManager;

import androidx.annotation.NonNull;

import java.util.Calendar;
import java.util.Date;

public class Timer {
    private String name;
    private boolean isActive;
    private int secondsTotal;
    private int secondsLeft;

    public Timer(String name, int interval) {
        this.name = name;
        this.secondsTotal = interval;
        this.secondsLeft = interval;
        this.isActive = true;
    }

    public void toggleActive() {
        isActive = !isActive;
    }

    public void edit(String name, int interval, boolean wasActive) {
        this.name = name;
        this.secondsTotal = interval;
        this.secondsLeft = interval;
        this.isActive = wasActive;
    }

    public Calendar getAlarmTime() {
        Calendar c = Calendar.getInstance();
        Date date = new Date();
        date.setTime(date.getTime() + secondsLeft * 1000);
        c.setTime(date);
        return c;
    }

    public void setSecondsLeft(int secondsLeft) {
        this.secondsLeft = secondsLeft;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    @NonNull
    @Override
    public String toString() {
        return name + ";" + secondsTotal + ";" +
                secondsLeft +";" + isActive + ";";
    }

    public int getSecondsTotal() {
        return secondsTotal;
    }
}
