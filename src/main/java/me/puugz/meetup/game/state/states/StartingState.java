package me.puugz.meetup.game.state.states;

import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.game.state.PassiveState;
import me.puugz.meetup.game.state.countdown.Countdown;
import me.puugz.meetup.util.LocationUtil;
import me.puugz.meetup.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author puugz
 * @since May 08, 2023
 */
public class StartingState extends PassiveState {

    // FIXME: remove "/ 6"
    @Getter
    private final Countdown countdown = new Countdown(
            60 / /*6*/2, "The game will begin", () -> {
        PlayerUtil.broadcast(ChatColor.YELLOW + "The game has started!");
        UHCMeetup.getInstance().getStateHandler().next();
    });

    @Override
    public void enable() {
        countdown.start();

        for (Player player : UHCMeetup.getInstance()
                .getPlayerHandler()
                .aliveAsPlayers()
        ) {
            this.preparePlayer(player);
        }
    }

    @Override
    public void disable() {
        Countdown.cancelIfActive(this.countdown);

        for (Player player : UHCMeetup.getInstance()
                .getPlayerHandler()
                .aliveAsPlayers()
        ) {
            PlayerUtil.unsit(player);
        }
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        event.setJoinMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has joined the game.");
        this.preparePlayer(player);
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final int numOfWaiting = UHCMeetup.getInstance()
                .getPlayerHandler().alive().size();

        event.setQuitMessage(ChatColor.GOLD + player.getName() + ChatColor.YELLOW + " has left the game.");

        if (numOfWaiting < 2 && Countdown.isActive(this.countdown)) {
            Bukkit.broadcastMessage(ChatColor.RED + "The game has been cancelled due to a player leaving.");
            UHCMeetup.getInstance().getStateHandler().setState(GameState.WAITING);
        }
    }

    private void preparePlayer(Player player) {
        PlayerUtil.clear(player);
        player.teleport(LocationUtil.getScatterLocation(
                UHCMeetup.getInstance().getBorderHandler().getBorderSize()
        ));
        PlayerUtil.sit(player);
        // TODO: Equip player with items
    }

    @Override
    public GameState next() {
        return PLAYING;
    }
}
