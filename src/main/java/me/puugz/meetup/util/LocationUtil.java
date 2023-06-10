package me.puugz.meetup.util;

import lombok.experimental.UtilityClass;
import me.puugz.meetup.UHCMeetup;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import java.util.Random;

/**
 * @author puugz
 * @since April 04, 2022
 */
@UtilityClass
public class LocationUtil {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    /**
     * Finds an appropriate location to scatter the player at
     * @param borderSize The size of the current border
     * @return The scatter location
     */
    public Location getScatterLocation(int borderSize) {
        final World world = UHCMeetup.getInstance()
                .getMapHandler().getMapWorld();

        int x;
        int y;
        int z;

        // Prevents the player from spawning in a cave or above water/lava
        do {
            x = RANDOM.nextInt(borderSize * 2) - borderSize;
            z = RANDOM.nextInt(borderSize * 2) - borderSize;
            y = world.getHighestBlockYAt(x, z);
        } while (y < 63 || world.getBlockAt(x, y, z).getRelative(BlockFace.DOWN).isLiquid());

        return new Location(world, x + 0.5, y, z + 0.5);
    }

    /**
     * Serializes a location to a string
     * @param location The location
     * @return The serialized location
     */
    public String locationToString(Location location) {
        return location.getWorld().getName() +
                ", " + location.getX() +
                ", " + location.getY() +
                ", " + location.getZ() +
                ", " + location.getYaw() +
                ", " + location.getPitch();
    }

    /**
     * Deserializes a string to a location
     * @param locationString The location in a string form
     * @return The location
     */
    public Location stringToLocation(String locationString) {
        final String[] parts = locationString.split(", ");

        return new Location(
                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5])
        );
    }
}
