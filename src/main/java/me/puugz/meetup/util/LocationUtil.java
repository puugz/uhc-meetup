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

    public Location getScatterLocation() {
        final int borderSize = UHCMeetup.getInstance()
                .getBorderHandler().getBorderSize();
        final World world = UHCMeetup.getInstance()
                .getMapHandler().getMapWorld();

        int x;
        int y;
        int z;

        // Prevents the player from spawning in a cave or above water/lava
        // TODO: Make it so you don't spawn too close to other players
        do {
            x = RANDOM.nextInt(borderSize * 2) - borderSize;
            z = RANDOM.nextInt(borderSize * 2) - borderSize;
            y = world.getHighestBlockYAt(x, z);
        } while (y < 63 || world.getBlockAt(x, y, z).getRelative(BlockFace.DOWN).isLiquid());

        return new Location(world, x, y, z);
    }

    public String locationToString(Location location) {
        return location.getWorld().getName() +
                ", " + location.getX() +
                ", " + location.getY() +
                ", " + location.getZ() +
                ", " + location.getYaw() +
                ", " + location.getPitch();
    }

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
