package me.puugz.meetup.game.player.task;

import lombok.RequiredArgsConstructor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author puugz
 * @since Jun 04, 2023
 */
@RequiredArgsConstructor
public class WinnerFireworkTask extends BukkitRunnable {

    private final Player winner;

    @Override
    public void run() {
        if (winner == null || !winner.isOnline()) {
            this.cancel();
            return;
        }

        final Firework firework = (Firework) winner.getWorld()
                .spawnEntity(winner.getLocation(), EntityType.FIREWORK);
        final FireworkMeta meta = firework.getFireworkMeta();

        meta.setPower(2);
        meta.addEffect(FireworkEffect
                .builder()
                .withColor(Color.LIME)
                .withFlicker()
                .withTrail()
                .build());

        firework.setFireworkMeta(meta);
    }
}
