package me.puugz.meetup.game.state.states;

import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.config.SettingsConfig;
import me.puugz.meetup.game.border.BorderHandler;
import me.puugz.meetup.game.event.CustomDeathEvent;
import me.puugz.meetup.game.player.GamePlayer;
import me.puugz.meetup.game.player.PlayerHandler;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.game.state.countdown.Countdown;
import me.puugz.meetup.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author puugz
 * @since May 19, 2023
 */
public class PlayingState implements GameState {

    private final MessagesConfig messages = UHCMeetup.getInstance().getMessagesConfig();
    private final SettingsConfig settings = UHCMeetup.getInstance().getSettingsConfig();

    @Getter
    private final Countdown borderCountdown = new Countdown(this.settings.borderShrinkTime);

    private final PlayerHandler playerHandler = UHCMeetup.getInstance().getPlayerHandler();
    private final BorderHandler borderHandler = UHCMeetup.getInstance().getBorderHandler();

    private final Map<UUID, UUID> lastHitByMap = new HashMap<>();

    @Override
    public void enable() {
        UHCMeetup.getInstance()
                .getPlayerHandler()
                .players()
                .forEach(it -> it.setGamesPlayed(it.getGamesPlayed() + 1));

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
                this.borderCountdown.setSeconds(this.settings.borderShrinkTime);
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
        UHCMeetup.getInstance().getPlayerHandler().players()
                .forEach(GamePlayer::stopNoCleanTimer);
    }

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        event.setJoinMessage(null);
        PlayerUtil.clear(player);

        final World world = UHCMeetup.getInstance().getMapHandler().getMapWorld();
        player.teleport(new Location(world, 0.5D, world.getHighestBlockYAt(0, 0), 0.5D));

        this.playerHandler.addSpectator(player);
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        final GamePlayer gamePlayer = this.playerHandler.find(event.getPlayer().getUniqueId());

        if (gamePlayer.getState() == GamePlayer.State.PLAYING) {
            gamePlayer.setState(GamePlayer.State.SPECTATING);
            gamePlayer.stopNoCleanTimer();

            event.setQuitMessage(this.messages.playerDisqualified
                    .replace("{player}", gamePlayer.getName()));
        } else {
            event.setQuitMessage(null);
        }

        this.playerHandler.handleWinnerCheck();
    }

    @EventHandler
    public void handleDeath(CustomDeathEvent event) {
        final Player victim = event.getVictim();
        final Player killer = event.getKiller();

        final GamePlayer victimData = this.playerHandler.find(victim.getUniqueId());
        victimData.setDeaths(victimData.getDeaths() + 1);

        this.playerHandler.addSpectator(victim);

        if (killer != null && !killer.getUniqueId().equals(victim.getUniqueId())) {
            final GamePlayer killerData = this.playerHandler.find(killer.getUniqueId());
            killerData.setLocalKills(killerData.getLocalKills() + 1);

            victim.sendMessage(this.messages.killedBy.replace("{killer}", killer.getName()));
            killer.sendMessage(this.messages.killedPlayer.replace("{victim}", victim.getName()));

            Bukkit.broadcastMessage(this.messages.slainByKiller
                    .replace("{victim}", victim.getName())
                    .replace("{killer}", killer.getName()));
        } else {
            Bukkit.broadcastMessage(this.messages.died.replace("{player}", victim.getName()));
        }

        this.playerHandler.handleWinnerCheck();
    }

    @EventHandler
    public void handleDeath(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        final Player victim = (Player) event.getEntity();
        final double health = victim.getHealth() - event.getFinalDamage();

        if (health < 0) {
            event.setCancelled(true);

            Player killer = null;
            if (this.lastHitByMap.containsKey(victim.getUniqueId())) {
                killer = Bukkit.getPlayer(this.lastHitByMap.get(victim.getUniqueId()));
            }

            final CustomDeathEvent deathEvent = new CustomDeathEvent(victim, killer);
            Bukkit.getPluginManager().callEvent(deathEvent);
        }
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

    @EventHandler
    public void handleEntityDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        final Player damager = (Player) event.getDamager();

        if (this.isSpectating(damager)) {
            event.setCancelled(true);
            return;
        }

        if (!(event.getEntity() instanceof Player))
            return;

        final Player player = (Player) event.getEntity();
        this.lastHitByMap.put(player.getUniqueId(), damager.getUniqueId());
    }

    @EventHandler
    public void handleInteraction(PlayerInteractEvent event) {
        event.setCancelled(this.isSpectating(event.getPlayer()));
    }

    @EventHandler
    public void handleBlockPlace(BlockPlaceEvent event) {
        if (this.isSpectating(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        final Location blockLocation = event.getBlock().getLocation();
        final int borderSize = borderHandler.getBorderSize();

        final double x = blockLocation.getX();
        final double z = blockLocation.getZ();

        if (x >= borderSize - 1 || x <= -borderSize
                || z >= borderSize - 1 || z <= -borderSize
        ) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(messages.cantPlaceBlocksHere);
        }
    }

    @EventHandler
    public void handleBlockBreak(BlockBreakEvent event) {
        if (this.isSpectating(event.getPlayer())) {
            event.setCancelled(true);
            return;
        }

        final Location blockLocation = event.getBlock().getLocation();
        final int borderSize = borderHandler.getBorderSize();

        final double x = blockLocation.getX();
        final double z = blockLocation.getZ();

        if (x >= borderSize - 1 || x <= -borderSize
                || z >= borderSize - 1 || z <= -borderSize
        ) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(messages.cantBreakBlocksHere);
        }
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
        return gamePlayer.getState() == GamePlayer.State.SPECTATING;
    }

    @Override
    public GameState next() {
        return ENDING;
    }
}
