package me.puugz.meetup.game.border;

import lombok.Getter;
import lombok.Setter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.config.SettingsConfig;
import me.puugz.meetup.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author puugz
 * @since May 19, 2023
 */
public class BorderHandler implements Listener {

    private final SettingsConfig settings = UHCMeetup.getInstance().getSettingsConfig();

    @Getter
    @Setter
    private int borderSize = this.settings.initialBorderSize;

    private static final List<Material> IGNORED_WALL_TYPES =
            Arrays.asList(
                    Material.WATER, Material.STATIONARY_WATER, Material.LAVA,
                    Material.STATIONARY_LAVA, Material.LEAVES, Material.LEAVES_2,
                    Material.LONG_GRASS, Material.DOUBLE_PLANT, Material.YELLOW_FLOWER,
                    Material.SNOW, Material.CACTUS, Material.DEAD_BUSH,
                    Material.BROWN_MUSHROOM, Material.RED_MUSHROOM, Material.HUGE_MUSHROOM_1,
                    Material.HUGE_MUSHROOM_2
            );

    public BorderHandler() {
        UHCMeetup.getInstance().getLogger().info("border size: " + this.borderSize);
    }

    @EventHandler
    public void handleMove(PlayerMoveEvent event) {
        if (event.getFrom().getX() == event.getTo().getX() ||
                event.getFrom().getZ() == event.getTo().getZ())
            return;

        if (this.isOutsideBorder(event.getTo(), this.borderSize)) {
            final Player player = event.getPlayer();

            player.setVelocity(event.getFrom().toVector().subtract(event.getTo().toVector()).normalize());
            player.sendMessage(UHCMeetup.getInstance().getMessagesConfig().reachedBorder);
        }
    }

    public void shrinkBorder(int newBorderSize) {
        final World world = UHCMeetup.getInstance()
                .getMapHandler().getMapWorld();
        final MessagesConfig messages = UHCMeetup.getInstance()
                .getMessagesConfig();

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> this.isOutsideBorder(player.getLocation(), newBorderSize))
                .forEach(player -> {
                    player.teleport(LocationUtil.getScatterLocation(newBorderSize));
                    player.sendMessage(messages.teleportInsideBorder);
                });

        this.borderSize = newBorderSize;
        world.getWorldBorder().setSize(this.borderSize * 2);
        this.generateBedrockWall(world);
    }

    private boolean isOutsideBorder(Location location, int borderSize) {
        return !(Math.abs(location.getX()) <= borderSize) || !(Math.abs(location.getZ()) <= borderSize);
    }

    public void generateBedrockWall(World world) {
        final int startX = -borderSize - 1;
        final int endX = borderSize;
        final int startZ = -borderSize - 1;
        final int endZ = borderSize;

        // Generate walls on X borders
        for (int x = startX; x <= endX; x++) {
            this.generateWallAt(world, x, startZ);
            this.generateWallAt(world, x, endZ);
        }

        // Generate walls on Z borders (excluding corners)
        for (int z = startZ + 1; z < endZ; z++) {
            this.generateWallAt(world, startX, z);
            this.generateWallAt(world, endX, z);
        }
    }

    private void generateWallAt(World world, int x, int z) {
        final int y = world.getHighestBlockYAt(x, z);

        for (int i = y + this.settings.borderWallHeight - 1; i > 50; i--) {
            final Block block = world.getBlockAt(x, i, z);
            if (block.isEmpty() || IGNORED_WALL_TYPES.contains(block.getType()))
                block.setType(Material.BEDROCK);
        }
    }
}
