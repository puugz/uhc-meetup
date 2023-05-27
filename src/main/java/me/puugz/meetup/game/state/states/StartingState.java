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
    private Countdown countdown;

    @Override
    public void enable() {
        this.countdown = new Countdown(
                60, this.messages.gameStarting, () -> {
            PlayerUtil.broadcast(this.messages.gameStarted);
            UHCMeetup.getInstance().getStateHandler().next();
        });
        this.countdown.start();

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

        event.setJoinMessage(this.messages.playerJoined
                .replace("{player}", player.getName()));
        this.preparePlayer(player);
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final int numOfWaiting = UHCMeetup.getInstance()
                .getPlayerHandler().alive().size();

        event.setQuitMessage(this.messages.playerQuit
                .replace("{player}", player.getName()));

        if (numOfWaiting < 2 && Countdown.isActive(this.countdown)) {
            Bukkit.broadcastMessage(this.messages.startingCancelled);
            UHCMeetup.getInstance().getStateHandler().setState(GameState.WAITING);
        }
    }

    private void preparePlayer(Player player) {
        PlayerUtil.clear(player);
        player.teleport(LocationUtil.getScatterLocation(
                UHCMeetup.getInstance().getBorderHandler().getBorderSize()
        ));
        PlayerUtil.sit(player);
        UHCMeetup.getInstance().getKitHandler().handleKit(player);
    }

    @Override
    public GameState next() {
        return PLAYING;
    }
}
