package me.puugz.meetup.game.map;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.map.thread.MapGenerator;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.IOException;

/**
 * @author puugz
 * @since May 19, 2023
 */
public class MapHandler {

    /**
     * Generates a clean world with only these biomes:
     * Plains, Desert, Savanna
     */
    public void generateMap() {
        try {
            new MapGenerator().runTaskTimer(UHCMeetup.getInstance(), 0L, 20L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public World getMapWorld() {
        return Bukkit.getWorld("uhc_meetup");
    }
}
