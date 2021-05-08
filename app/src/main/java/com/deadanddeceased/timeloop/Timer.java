package com.deadanddeceased.timeloop;

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
        tick();
    }

    public void edit(String name, int interval, boolean wasActive) {
        this.name = name;
        this.secondsTotal = interval;
        this.secondsLeft = interval;
        this.isActive = wasActive;
    }

    private void tick() {
        if (isActive) {
            secondsLeft--;
        }
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getSecondsTotal() {
        return secondsTotal;
    }
}
