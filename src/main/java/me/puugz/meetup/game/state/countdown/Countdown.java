package me.puugz.meetup.game.state.countdown;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.util.PlayerUtil;
import me.puugz.meetup.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author puugz
 * @since May 08, 2023
 */
@AllArgsConstructor
public class Countdown extends BukkitRunnable {

    @Getter
    @Setter
    private int seconds;
    private String what;

    protected Runnable action;

    public Countdown(int seconds, String what) {
        this.seconds = seconds;
        this.what = what;
    }

    public void start() {
//        if (!isActive(this))
        this.runTaskTimer(UHCMeetup.getInstance(), 0L, 20L);
    }

    @Override
    public void run() {
        if (this.seconds == 0) {
            this.cancel();
            this.action.run();
            return;
        }

        switch (this.seconds) {
            case 300: case 240: case 180: case 120: case 60: case 30: case 15: case 10: case 5: case 4: case 3: case 2: case 1:
                PlayerUtil.broadcast(
                        ChatColor.YELLOW + this.what + " in " + ChatColor.GOLD + TimeUtil.formatSeconds(seconds) + ChatColor.YELLOW + "."
                );
        }

        this.seconds--;
    }

    public static void cancelIfActive(Countdown countdown) {
        if (isActive(countdown))
            countdown.cancel();
    }

    public static boolean isActive(Countdown countdown) {
        return countdown != null && Bukkit.getScheduler().isCurrentlyRunning(countdown.getTaskId());
    }
}
