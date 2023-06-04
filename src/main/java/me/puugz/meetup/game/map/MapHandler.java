package me.puugz.meetup.game.map;

import lombok.Getter;
import lombok.Setter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.map.task.MapGeneratorTask;
import me.puugz.meetup.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.IOException;

/**
 * @author puugz
 * @since May 19, 2023
 */
@Getter
@Setter
public class MapHandler {

    private Location spawnLocation;

    public MapHandler() {
        this.spawnLocation = LocationUtil.stringToLocation(
                UHCMeetup.getInstance()
                        .getConfig().getString("spawn-location")
        );
        this.generateMap();
    }

    /**
     * Generates a clean world with only these biomes:
     * Plains, Desert, Savanna
     */
    private void generateMap() {
        try {
            new MapGeneratorTask().runTaskTimer(UHCMeetup.getInstance(), 0L, 20L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public World getMapWorld() {
        return Bukkit.getWorld("uhc_meetup");
    }
}
