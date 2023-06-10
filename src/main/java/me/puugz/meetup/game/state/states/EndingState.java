package me.puugz.meetup.game.state.states;

import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.config.SettingsConfig;
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

    private final MessagesConfig messages = UHCMeetup.getInstance().getMessagesConfig();
    private final SettingsConfig settings = UHCMeetup.getInstance().getSettingsConfig();

    @Getter
    private final Countdown countdown = new Countdown(
            this.settings.endingTime, this.messages.serverRestarting, () -> {
        PlayerUtil.broadcast(this.messages.serverRestart);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.settings.restartCommand);
    });

    @Override
    public void enable() {
        for (String s : this.messages.win)
            Bukkit.broadcastMessage(s
                    .replace("{player}", UHCMeetup.getInstance().getPlayerHandler().getWinnerName())
            );

        this.countdown.start();
    }

    @Override
    public GameState next() {
        return null;
    }
}
