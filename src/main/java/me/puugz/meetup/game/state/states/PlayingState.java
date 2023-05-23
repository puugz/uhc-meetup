package me.puugz.meetup.game.state.states;

import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.game.border.BorderHandler;
import me.puugz.meetup.game.player.GamePlayer;
import me.puugz.meetup.game.player.PlayerHandler;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.game.state.countdown.Countdown;
import me.puugz.meetup.util.PlayerUtil;
import me.puugz.meetup.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author puugz
 * @since May 19, 2023
 */
public class PlayingState implements GameState {

    // TODO: Settings config
    private static final int BORDER_SHRINK_TIME = 120;

    @Getter
    private final Countdown countdown = new Countdown(BORDER_SHRINK_TIME);

    private final MessagesConfig messages = UHCMeetup.getInstance()
            .getMessagesConfig();

    private final PlayerHandler playerHandler = UHCMeetup.getInstance().getPlayerHandler();
    private final BorderHandler borderHandler = UHCMeetup.getInstance().getBorderHandler();

    @Override
    public void enable() {
        final AtomicInteger newBorderSize = new AtomicInteger(borderHandler.getBorderSize() > 25
                ? borderHandler.getBorderSize() - 25
                : borderHandler.getBorderSize() - 15);

        countdown.setDontCancel(true);
        countdown.setWhat(messages.borderShrink.replace("{size}", newBorderSize.toString()));
        countdown.setAction(() -> {
            borderHandler.shrinkBorder(newBorderSize.intValue());
            PlayerUtil.broadcast(messages.borderShrunk
                    .replace("{size}", newBorderSize.toString()));

            if (newBorderSize.intValue() > 10) {
                newBorderSize.set(borderHandler.getBorderSize() > 25
                        ? borderHandler.getBorderSize() - 25
                        : borderHandler.getBorderSize() - 15);
                countdown.setWhat(messages.borderShrink.replace("{size}", newBorderSize.toString()));
                countdown.setSeconds(BORDER_SHRINK_TIME);
            } else {
                countdown.cancel();
            }
        });
        countdown.start();

        // TODO: Winner check runnable
        UHCMeetup.getInstance().getScenarioHandler().enable();
    }

    @Override
    public void disable() {
        Countdown.cancelIfActive(this.countdown);
        UHCMeetup.getInstance().getScenarioHandler().disable();
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        PlayerUtil.clear(player);
        playerHandler.addSpectator(player);
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        final GamePlayer gamePlayer = playerHandler.find(event.getPlayer().getUniqueId());
        gamePlayer.state = GamePlayer.State.SPECTATING;

        event.setQuitMessage(messages.playerDisqualified
                .replace("{player}", gamePlayer.getName()));

        playerHandler.handleWinnerCheck();
    }

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {
        final Player victim = event.getEntity();
        final Player killer = victim.getKiller();

        final GamePlayer victimData = playerHandler.find(victim.getUniqueId());

        victimData.deathLocation = victim.getLocation();
        victimData.deaths++;

        if (killer != null) {
            final GamePlayer killerData = playerHandler.find(killer.getUniqueId());
            killerData.kills++;

            victim.sendMessage(messages.killedBy.replace("{killer}", killer.getName()));
            killer.sendMessage(messages.killedPlayer.replace("{victim}", victim.getName()));

            event.setDeathMessage(messages.slainByKiller
                    .replace("{victim}", victim.getName())
                    .replace("{killer}", killer.getName()));
        } else {
            event.setDeathMessage(messages.died.replace("{player}", victim.getName()));
        }

        victim.spigot().respawn();

        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCMeetup.getInstance(), () -> {
            playerHandler.addSpectator(victim);
            playerHandler.handleWinnerCheck();
        }, 10L);
    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {
        final GamePlayer gamePlayer = playerHandler.find(event.getPlayer().getUniqueId());

        if (gamePlayer.deathLocation != null)
            event.setRespawnLocation(gamePlayer.deathLocation);
    }

    /**
     * Spectator events
     */
    @EventHandler
    public void handleInteraction(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        event.setCancelled(this.isSpectating(player));
    }

    // TODO: Add arrow hit information
    @EventHandler
    public void handleEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        final Player player = (Player) event.getDamager();
        event.setCancelled(this.isSpectating(player));
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        event.setCancelled(this.isSpectating(player));
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        event.setCancelled(this.isSpectating(player));
    }

    @EventHandler
    public void handleItemDrop(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        event.setCancelled(this.isSpectating(player));
    }

    @EventHandler
    public void handleItemPickup(PlayerPickupItemEvent event) {
        final Player player = event.getPlayer();
        event.setCancelled(this.isSpectating(player));
    }

    private boolean isSpectating(Player player) {
        final GamePlayer gamePlayer = playerHandler.find(player.getUniqueId());
        return gamePlayer.state == GamePlayer.State.SPECTATING;
    }

    @Override
    public GameState next() {
        return ENDING;
    }
}
