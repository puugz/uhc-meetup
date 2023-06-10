package me.puugz.meetup.game.state;

import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;

/**
 * @author puugz
 * @since May 08, 2023
 */
public abstract class PassiveState implements GameState {

    @EventHandler
    public void handle(FoodLevelChangeEvent event) {
        event.setCancelled(true);
        event.setFoodLevel(20);
    }

    @EventHandler
    public void handle(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handle(EntityDamageEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handle(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handle(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handle(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handle(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handle(PlayerItemConsumeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void handle(VehicleEnterEvent event) {
        event.setCancelled(true);
    }
}
