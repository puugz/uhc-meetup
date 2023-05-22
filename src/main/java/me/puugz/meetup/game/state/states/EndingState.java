package me.puugz.meetup.game.state.states;

import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.game.state.PassiveState;
import me.puugz.meetup.game.state.countdown.Countdown;
import me.puugz.meetup.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author puugz
 * @since May 19, 2023
 */
public class EndingState extends PassiveState {

    @Getter
    private final Countdown countdown = new Countdown(
            60, "The server will be restarting", () -> {
        PlayerUtil.broadcast(ChatColor.RED + "Server restarting...");
        Bukkit.shutdown();
    });

    @Override
    public void enable() {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(
                ChatColor.GOLD
                        + UHCMeetup.getInstance().getPlayerHandler().getWinnerName()
                        + ChatColor.GREEN + " wins!"
        );
        Bukkit.broadcastMessage("");

        // TODO: Save data to database
        countdown.start();
    }

    @Override
    public GameState next() {
        return null;
    }
}
