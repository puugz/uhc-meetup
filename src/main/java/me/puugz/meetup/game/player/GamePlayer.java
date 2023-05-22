package me.puugz.meetup.game.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author puugz
 * @since May 08, 2023
 */
@Getter
@Setter
@RequiredArgsConstructor
public class GamePlayer {

    private final UUID uuid;
    private final String name;

    public State state = State.PLAYING;
    public Location deathLocation;

    public int kills;
    public int deaths;

    public Player asPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public enum State {
        PLAYING,
        SPECTATING
    }
}
