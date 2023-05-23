package me.puugz.meetup.game.state.states;

import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.game.state.PassiveState;
import me.puugz.meetup.game.state.countdown.Countdown;
import me.puugz.meetup.util.LocationUtil;
import me.puugz.meetup.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author puugz
 * @since May 08, 2023
 */
public class StartingState extends PassiveState {

    private final MessagesConfig messages = UHCMeetup.getInstance()
            .getMessagesConfig();

    @Getter
    private final Countdown countdown = new Countdown(
            60, messages.gameStarting, () -> {
        PlayerUtil.broadcast(messages.gameStarted);
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

        event.setJoinMessage(messages.playerJoined
                .replace("{player}", player.getName()));
        this.preparePlayer(player);
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final int numOfWaiting = UHCMeetup.getInstance()
                .getPlayerHandler().alive().size();

        event.setQuitMessage(messages.playerQuit
                .replace("{player}", player.getName()));

        if (numOfWaiting < 2 && Countdown.isActive(this.countdown)) {
            Bukkit.broadcastMessage(messages.startingCancelled);
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
