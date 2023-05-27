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
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author puugz
 * @since May 19, 2023
 */
public class PlayingState implements GameState {

    // TODO: Settings config
    private static final int BORDER_SHRINK_TIME = 120;

    @Getter
    private final Countdown borderCountdown = new Countdown(BORDER_SHRINK_TIME);

    private final MessagesConfig messages = UHCMeetup.getInstance()
            .getMessagesConfig();

    private final PlayerHandler playerHandler = UHCMeetup.getInstance().getPlayerHandler();
    private final BorderHandler borderHandler = UHCMeetup.getInstance().getBorderHandler();

    @Override
    public void enable() {
        final AtomicInteger newBorderSize = new AtomicInteger(this.borderHandler.getBorderSize() > 25
                ? this.borderHandler.getBorderSize() - 25
                : this.borderHandler.getBorderSize() - 15);

        this.borderCountdown.setDontCancel(true);
        this.borderCountdown.setNoSound(true);

        this.borderCountdown.setMessage(this.messages.borderShrink.replace("{size}", newBorderSize.toString()));
        this.borderCountdown.setAction(() -> {
            this.borderHandler.shrinkBorder(newBorderSize.intValue());
            Bukkit.broadcastMessage(this.messages.borderShrunk
                    .replace("{size}", newBorderSize.toString()));

            if (newBorderSize.intValue() > 10) {
                newBorderSize.set(borderHandler.getBorderSize() > 25
                        ? this.borderHandler.getBorderSize() - 25
                        : this.borderHandler.getBorderSize() - 15);
                this.borderCountdown.setMessage(this.messages.borderShrink.replace("{size}", newBorderSize.toString()));
                this.borderCountdown.setSeconds(BORDER_SHRINK_TIME);
            } else {
                this.borderCountdown.cancel();
            }
        });
        this.borderCountdown.start();

        // TODO: Winner check runnable
        UHCMeetup.getInstance().getScenarioHandler().enable();
    }

    @Override
    public void disable() {
        Countdown.cancelIfActive(this.borderCountdown);

        UHCMeetup.getInstance().getScenarioHandler().disable();
        UHCMeetup.getInstance().getPlayerHandler().alive()
                .forEach(GamePlayer::stopNoCleanTimer);
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        event.setJoinMessage(null);

        PlayerUtil.clear(player);
        player.teleport(UHCMeetup.getInstance()
                .getMapHandler().getSpectatorLocation());

        this.playerHandler.addSpectator(player);
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        final GamePlayer gamePlayer = this.playerHandler.find(event.getPlayer().getUniqueId());

        if (gamePlayer.state == GamePlayer.State.PLAYING) {
            gamePlayer.state = GamePlayer.State.SPECTATING;
            gamePlayer.stopNoCleanTimer();

            event.setQuitMessage(this.messages.playerDisqualified
                    .replace("{player}", gamePlayer.getName()));
        } else {
            event.setQuitMessage(null);
        }

        this.playerHandler.handleWinnerCheck();
    }

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {
        final Player victim = event.getEntity();
        final Player killer = victim.getKiller();

        final GamePlayer victimData = this.playerHandler.find(victim.getUniqueId());

        victimData.deathLocation = victim.getLocation();
        victimData.deaths++;

        if (killer != null && !killer.getUniqueId().equals(victim.getUniqueId())) {
            final GamePlayer killerData = this.playerHandler.find(killer.getUniqueId());
            killerData.kills++;

            victim.sendMessage(this.messages.killedBy.replace("{killer}", killer.getName()));
            killer.sendMessage(this.messages.killedPlayer.replace("{victim}", victim.getName()));

            event.setDeathMessage(this.messages.slainByKiller
                    .replace("{victim}", victim.getName())
                    .replace("{killer}", killer.getName()));
        } else {
            event.setDeathMessage(this.messages.died.replace("{player}", victim.getName()));
        }

        victim.spigot().respawn();

        Bukkit.getScheduler().scheduleSyncDelayedTask(UHCMeetup.getInstance(), () -> {
            this.playerHandler.addSpectator(victim);
            this.playerHandler.handleWinnerCheck();
        }, 10L);
    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {
        final GamePlayer gamePlayer = this.playerHandler.find(event.getPlayer().getUniqueId());

        if (gamePlayer.deathLocation != null)
            event.setRespawnLocation(gamePlayer.deathLocation);
    }

    @EventHandler
    public void handleGoldenHead(PlayerItemConsumeEvent event) {
        final ItemStack item = event.getItem();

        if (item.isSimilar(PlayerUtil.GOLDEN_HEAD)) {
            final Player player = event.getPlayer();
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1), true);
        }
    }

    @EventHandler
    public void handleArrowDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        final Player player = (Player) event.getEntity();
        if (!(event.getDamager() instanceof Arrow)) return;

        final Arrow arrow = (Arrow) event.getDamager();
        if (!(arrow.getShooter() instanceof Player)) return;

        final Player shooter = (Player) arrow.getShooter();
        if (player.getUniqueId().equals(shooter.getUniqueId())) return;

        final double health = Math.ceil(player.getHealth() - event.getFinalDamage()) / 2.0D;

        if (health > 0.0D)
            shooter.sendMessage(this.messages.arrowHit
                    .replace("{player}", player.getName())
                    .replace("{health}", "" + health));
    }

    /**
     * Spectator events
     */
    @EventHandler
    public void handleInteraction(PlayerInteractEvent event) {
        event.setCancelled(this.isSpectating(event.getPlayer()));
    }

    @EventHandler
    public void handleEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        final Player player = (Player) event.getDamager();
        event.setCancelled(this.isSpectating(player));
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(this.isSpectating(event.getPlayer()));
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        event.setCancelled(this.isSpectating(event.getPlayer()));
    }

    @EventHandler
    public void handleItemDrop(PlayerDropItemEvent event) {
        event.setCancelled(this.isSpectating(event.getPlayer()));
    }

    @EventHandler
    public void handleItemPickup(PlayerPickupItemEvent event) {
        event.setCancelled(this.isSpectating(event.getPlayer()));
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
