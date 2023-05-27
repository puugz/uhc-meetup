package me.puugz.meetup.game.scenario.scenarios.noclean;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.game.player.GamePlayer;
import me.puugz.meetup.game.scenario.Scenario;
import me.puugz.meetup.util.TimeUtil;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * @author puugz
 * @since May 22, 2023
 */
public class NoCleanScenario extends Scenario {

    public NoCleanScenario() {
        super("No Clean", "Killing a player gives you a 15 seconds protection against PvP.",
                "Attacking an opponent player removes the PvP protection.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleDeath(PlayerDeathEvent event) {
        final Player victim = event.getEntity();
        final Player killer = victim.getKiller();

        if (killer != null && !killer.getUniqueId().equals(victim.getUniqueId())) {
            final GamePlayer killerData = UHCMeetup.getInstance()
                    .getPlayerHandler().find(killer.getUniqueId());

            killerData.noCleanTimer = new NoCleanTimer(killerData);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player))
            return;

        Player damager = null;

        if (event.getDamager() instanceof Projectile) {
            final Projectile source = (Projectile) event.getDamager();

            if (source.getShooter() instanceof Player)
                damager = (Player) source.getShooter();
        }

        if (event.getDamager() instanceof Player)
            damager = (Player) event.getDamager();

        if (damager == null)
            return;

        final GamePlayer damagerData = UHCMeetup.getInstance()
                .getPlayerHandler().find(damager.getUniqueId());

        if (damagerData.state == GamePlayer.State.SPECTATING)
            return;

        final Player victim = (Player) event.getEntity();
        final GamePlayer victimData = UHCMeetup.getInstance()
                .getPlayerHandler().find(victim.getUniqueId());

        final MessagesConfig messages = UHCMeetup.getInstance()
                .getMessagesConfig();

        if (damagerData.stopNoCleanTimer()) {
            damager.sendMessage(messages.noCleanHostileAction);
        } else if (victimData.noCleanTimer != null) {
            event.setCancelled(true);
            damager.sendMessage(messages.noCleanTargetExpiresIn
                    .replace("{player}", victim.getName())
                    .replace("{time}", "" + TimeUtil.formatSeconds(victimData.noCleanTimer.getSeconds())));
        }
    }
}
