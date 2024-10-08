package me.puugz.meetup.game.state.countdown;

import lombok.Getter;
import lombok.Setter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.util.PlayerUtil;
import me.puugz.meetup.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

/**
 * @author puugz
 * @since May 08, 2023
 */
@Setter
public class Countdown extends BukkitRunnable {

    @Getter
    private int seconds;
    private String message;

    private boolean dontCancel;
    private boolean noSound;

    protected Runnable action;
    protected Consumer<String> tick = new Consumer<String>() {
        @Override
        public void accept(String message) {
            if (noSound)
                Bukkit.broadcastMessage(message);
            else
                PlayerUtil.broadcast(message);
        }
    };

    public Countdown(int seconds) {
        this.seconds = seconds;
    }

    public Countdown(int seconds, String what, Runnable action) {
        this.seconds = seconds;
        this.message = what;
        this.action = action;
    }

    public void start() {
        this.runTaskTimer(UHCMeetup.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
        if (this.seconds == 0) {
            if (!this.dontCancel)
                this.cancel();
            this.action.run();
            return;
        }

        switch (this.seconds) {
            case 300: case 240: case 180: case 120: case 60: case 30: case 15: case 10: case 5: case 4: case 3: case 2: case 1: {
                this.tick.accept(this.message.replace("{time}", TimeUtil.formatSeconds(this.seconds)));
            }
        }

        this.seconds--;
    }

    public static void cancelIfActive(Countdown countdown) {
        if (isActive(countdown))
            countdown.cancel();
    }

    public static boolean isActive(Countdown countdown) {
        return countdown != null && Bukkit.getScheduler().isQueued(countdown.getTaskId());
    }
}
