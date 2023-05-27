package me.puugz.meetup.game.state.states;

import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.game.state.PassiveState;
import me.puugz.meetup.game.state.countdown.Countdown;
import me.puugz.meetup.util.PlayerUtil;
import org.bukkit.Bukkit;

/**
 * @author puugz
 * @since May 19, 2023
 */
public class EndingState extends PassiveState {

    private final MessagesConfig messages = UHCMeetup.getInstance()
            .getMessagesConfig();

    @Getter
    private final Countdown countdown = new Countdown(
            60, this.messages.serverRestarting, () -> {
        PlayerUtil.broadcast(this.messages.serverRestart);
        Bukkit.shutdown();
    });

    @Override
    public void enable() {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(this.messages.win
                .replace("{winner}", UHCMeetup.getInstance().getPlayerHandler().getWinnerName()));
        Bukkit.broadcastMessage("");

        // TODO: Save data to database
        this.countdown.start();
    }

    @Override
    public GameState next() {
        return null;
    }
}
