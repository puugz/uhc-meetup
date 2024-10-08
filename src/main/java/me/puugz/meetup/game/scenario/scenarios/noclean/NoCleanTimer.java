package me.puugz.meetup.game.scenario.scenarios.noclean;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.game.player.GamePlayer;
import me.puugz.meetup.game.state.countdown.Countdown;
import org.bukkit.entity.Player;

/**
 * @author puugz
 * @since May 26, 2023
 */
public class NoCleanTimer extends Countdown {

    public NoCleanTimer(GamePlayer gamePlayer, int time) {
        super(time);

        final Player player = gamePlayer.asPlayer();
        final MessagesConfig messages = UHCMeetup.getInstance()
                .getMessagesConfig();

        this.setNoSound(true);
        this.setMessage(messages.noCleanExpiresIn);
        this.setTick(player::sendMessage);
        this.setAction(() -> {
            player.sendMessage(messages.noCleanExpired);
            gamePlayer.setNoCleanTimer(null);
        });

        this.start();
    }
}
