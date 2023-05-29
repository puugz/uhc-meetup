package me.puugz.meetup.game.player;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.scenario.scenarios.noclean.NoCleanTimer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author puugz
 * @since May 08, 2023
 */
@Getter
@Setter
public class GamePlayer {

    @Expose private final UUID uuid;
    @Expose private String name;

    private State state = State.PLAYING;
    private NoCleanTimer noCleanTimer;

    private int localKills;

    @Expose private long firstJoin;
    @Expose private int kills;
    @Expose private int deaths;
    @Expose private int gamesPlayed;
    @Expose private int gamesWon;

    public GamePlayer(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public Player asPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }

    public boolean stopNoCleanTimer() {
        if (this.getNoCleanTimer() != null) {
            this.getNoCleanTimer().cancel();
            this.setNoCleanTimer(null);
            return true;
        }
        return false;
    }

    public void setLocalKills(int localKills) {
        this.localKills = localKills;
        this.kills += localKills;
    }

    public void save() {
        UHCMeetup.getInstance().getMongoHandler()
                .getPlayerRepository()
                .store(this.uuid, this);
    }

    public void saveAsync() {
        CompletableFuture.supplyAsync(() -> {
            this.save();
            return null;
        });
    }

    public enum State {
        PLAYING,
        SPECTATING
    }
}
