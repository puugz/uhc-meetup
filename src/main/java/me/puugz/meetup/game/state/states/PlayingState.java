package me.puugz.meetup.game.state.states;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.player.GamePlayer;
import me.puugz.meetup.game.player.PlayerHandler;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * @author puugz
 * @since May 19, 2023
 */
public class PlayingState implements GameState {

//    @Getter
//    private final Countdown countdown = new Countdown(
//            120, "The border will shrink to ", () -> {
//        UHCMeetup.getInstance().getBorderHandler().shrinkBorder();
//        PlayerUtil.broadcast(ChatColor.RED + "The border has shrunk to ...");
//    });

    private final PlayerHandler playerHandler = UHCMeetup.getInstance().getPlayerHandler();

    @Override
    public void enable() {
        // TODO: Start border shrinking
        // TODO: Winner check runnable
        UHCMeetup.getInstance().getScenarioHandler().enable();
    }

    @Override
    public void disable() {
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

            victim.sendMessage(ChatColor.YELLOW + "You have been killed by "
                    + ChatColor.GOLD + killer.getName()
                    + ChatColor.YELLOW + ".");
            killer.sendMessage(ChatColor.YELLOW + "You have killed "
                    + ChatColor.GOLD + victim.getName()
                    + ChatColor.YELLOW + ".");

            event.setDeathMessage(ChatColor.GOLD + victim.getName()
                    + ChatColor.YELLOW + " was slain by "
                    + ChatColor.GOLD + killer.getName()
                    + ChatColor.YELLOW + ".");
        } else {
            event.setDeathMessage(ChatColor.GOLD + victim.getName()
                    + ChatColor.YELLOW + " has died.");
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

    private boolean isSpectating(Player player) {
        final GamePlayer gamePlayer = playerHandler.find(player.getUniqueId());
        return gamePlayer.state == GamePlayer.State.SPECTATING;
    }

    @Override
    public GameState next() {
        return ENDING;
    }
}
