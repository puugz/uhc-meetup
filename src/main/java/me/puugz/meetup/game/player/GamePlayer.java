package me.puugz.meetup.game.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.puugz.meetup.game.scenario.scenarios.noclean.NoCleanTimer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author puugz
 * @since May 08, 2023
 */
@RequiredArgsConstructor
public class GamePlayer {

    @Getter
    private final UUID uuid;
    @Getter
    private final String name;

    public State state = State.PLAYING;
    public Location deathLocation;
    public NoCleanTimer noCleanTimer;

    public int kills;
    public int deaths;

    public Player asPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public boolean stopNoCleanTimer() {
        if (this.noCleanTimer != null) {
            this.noCleanTimer.cancel();
            this.noCleanTimer = null;
            return true;
        }
        return false;
    }

    public enum State {
        PLAYING,
        SPECTATING
    }
}
