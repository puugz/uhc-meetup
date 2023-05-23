package me.puugz.meetup.game.state.states;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.game.state.PassiveState;
import me.puugz.meetup.util.PlayerUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author puugz
 * @since May 08, 2023
 */
public class WaitingState extends PassiveState {

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final int numOfWaiting = UHCMeetup.getInstance()
                .getPlayerHandler().alive().size();

        player.teleport(UHCMeetup.getInstance()
                .getMapHandler().getSpawnLocation());

        PlayerUtil.clear(player);
        event.setJoinMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has joined the game.");

        if (numOfWaiting >= 2) {
            UHCMeetup.getInstance().getStateHandler().next();
        } else {
            player.sendMessage(ChatColor.YELLOW + "Minimum of 2 players is required for the game to start.");
        }
    }

    @Override
    public GameState next() {
        return STARTING;
    }
}
