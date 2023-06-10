package me.puugz.meetup.game.state.states;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.config.SettingsConfig;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.game.state.PassiveState;
import me.puugz.meetup.util.PlayerUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author puugz
 * @since May 08, 2023
 */
public class WaitingState extends PassiveState {

    private final MessagesConfig messages = UHCMeetup.getInstance().getMessagesConfig();
    private final SettingsConfig settings = UHCMeetup.getInstance().getSettingsConfig();

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final int numOfWaiting = (int) UHCMeetup.getInstance()
                .getPlayerHandler().players().count();

        player.teleport(UHCMeetup.getInstance()
                .getMapHandler().getSpawnLocation());

        PlayerUtil.clear(player);
        event.setJoinMessage(this.messages.playerJoined
                .replace("{player}", player.getName()));

        if (numOfWaiting >= this.settings.minRequiredPlayers) {
            UHCMeetup.getInstance().getStateHandler().next();
        } else {
            player.sendMessage(this.messages.minimumRequiredPlayers
                    .replace("{min}", "2"));
        }
    }

    @Override
    public GameState next() {
        return STARTING;
    }
}
