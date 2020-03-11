package fr.ethan.cachecache.GameElements;

import org.bukkit.scheduler.BukkitRunnable;

public class Timer extends BukkitRunnable {
    public int time;

    public Timer() {
        time = 0;
    }

    public Timer(int initTime) {
        time = initTime;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void run() {
        time--;
        if(time == 0) { cancel(); }
    }
}
