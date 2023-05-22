package me.puugz.meetup.game.scenario;

import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

/**
 * @author puugz
 * @since May 22, 2023
 */
@Getter
public class Scenario implements Listener {

    private final String name;
    private final String[] description;

    private boolean isActive;

    public Scenario(String name, String... description) {
        this.name = name;
        this.description = description;
    }

    public void enable() {
        this.isActive = true;
        Bukkit.getPluginManager().registerEvents(this, UHCMeetup.getInstance());
    }

    public void disable() {
        this.isActive = false;
        HandlerList.unregisterAll(this);
    }
}
