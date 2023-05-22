package me.puugz.meetup.util;

import lombok.experimental.UtilityClass;
import me.puugz.meetup.UHCMeetup;
import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * @author puugz
 * @since May 08, 2023
 */
@UtilityClass
public class PlayerUtil {

    public void clear(Player player) {
        clear(player, true);
    }

    public void clear(Player player, boolean updateInventory) {
        player.setHealth(player.getMaxHealth());
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.setLevel(0);
        player.setExp(0);
        player.getActivePotionEffects()
                .forEach(effect -> player.removePotionEffect(effect.getType()));

        if (updateInventory) {
            player.updateInventory();
        }
    }

    public void broadcast(String message) {
        Bukkit.broadcastMessage(message);
        Bukkit.getOnlinePlayers()
                .forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1f, 1f));
    }

    public int getPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }

    // TODO: Use reflection
    public void sit(Player player) {
        final CraftPlayer craftPlayer = (CraftPlayer) player;
        final Location location = player.getLocation();
        final EntityBat bat = new EntityBat(((CraftWorld) location.getWorld()).getHandle());

        bat.setLocation(location.getX(), location.getY() + 0.5, location.getZ(), 0, 0);
        bat.setInvisible(true);
        bat.setHealth(6);

        final PacketPlayOutSpawnEntityLiving spawnEntityPacket = new PacketPlayOutSpawnEntityLiving(bat);
        craftPlayer.getHandle().playerConnection.sendPacket(spawnEntityPacket);

        player.setMetadata("sitting", new FixedMetadataValue(UHCMeetup.getInstance(), bat.getId()));

        final PacketPlayOutAttachEntity sitPacket = new PacketPlayOutAttachEntity(0, craftPlayer.getHandle(), bat);
        craftPlayer.getHandle().playerConnection.sendPacket(sitPacket);
    }

    public void unsit(Player player) {
        if (player.hasMetadata("sitting")) {
            final CraftPlayer craftPlayer = (CraftPlayer) player;
            final PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(player.getMetadata("sitting").get(0).asInt());

            craftPlayer.getHandle().playerConnection.sendPacket(packet);
            player.removeMetadata("sitting", UHCMeetup.getInstance());
        }
    }
}
