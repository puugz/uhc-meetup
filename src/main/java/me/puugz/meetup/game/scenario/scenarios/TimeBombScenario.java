package me.puugz.meetup.game.scenario.scenarios;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.scenario.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author puugz
 * @since May 22, 2023
 */
public class TimeBombScenario extends Scenario {

    public TimeBombScenario() {
        super(
                "Time Bomb",
                "After death, the player inventory will be stored into a chest.",
                "The chest will explode 30 seconds later."
        );
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void handleDeath(PlayerDeathEvent event) {
        final Player victim = event.getEntity();
        final String victimName = victim.getName();
        final Location location = victim.getLocation();

        final List<ItemStack> items = new ArrayList<>();

        Stream.of(victim.getInventory().getArmorContents())
                .filter(item -> item != null && item.getType() != Material.AIR)
                .forEach(items::add);
        Stream.of(victim.getInventory().getContents())
                .filter(item -> item != null && item.getType() != Material.AIR)
                .forEach(items::add);

        event.getDrops().clear();
        location.getBlock().setType(Material.CHEST);

        final Chest chest = (Chest) location.getBlock().getState();

        location.add(1, 0, 0).getBlock().setType(Material.CHEST);
        location.add(0, 1, 0).getBlock().setType(Material.AIR);
        location.add(1, 1, 0).getBlock().setType(Material.AIR);

        chest.getBlockInventory().addItem(items.toArray(new ItemStack[0]));
        // TODO: Add golden head
        chest.update();

        Bukkit.getScheduler().runTaskLater(UHCMeetup.getInstance(), () -> {
            chest.getBlockInventory().clear();
            chest.getBlock().setType(Material.AIR);
            chest.update();

            location.getWorld().createExplosion(location, 3.0F);
            Bukkit.broadcastMessage(ChatColor.GOLD + victimName + ChatColor.YELLOW + "'s corpse has exploded!");
        }, 20L * 30L);
    }
}
