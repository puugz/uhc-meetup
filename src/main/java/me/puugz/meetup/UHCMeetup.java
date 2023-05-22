package me.puugz.meetup;

import io.github.nosequel.scoreboard.ScoreboardHandler;
import lombok.Getter;
import me.puugz.meetup.game.border.BorderHandler;
import me.puugz.meetup.game.map.MapHandler;
import me.puugz.meetup.game.player.PlayerHandler;
import me.puugz.meetup.game.player.listener.PlayerListener;
import me.puugz.meetup.game.scoreboard.ScoreboardProvider;
import me.puugz.meetup.game.state.StateHandler;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author puugz
 * @since May 08, 2023
 */
@Getter
public class UHCMeetup extends JavaPlugin {

    @Getter
    private static UHCMeetup instance;

    private boolean ready;

    private PlayerHandler playerHandler;
    private StateHandler stateHandler;
    private BorderHandler borderHandler;
    private MapHandler mapHandler;

    @Override
    public void onEnable() {
        instance = this;

        this.getConfig().options().copyDefaults(true);
        this.saveDefaultConfig();

        new ScoreboardHandler(this, new ScoreboardProvider(), 10L);

        this.playerHandler = new PlayerHandler();
        this.stateHandler = new StateHandler();
        this.borderHandler = new BorderHandler();
        this.mapHandler = new MapHandler();
        this.mapHandler.generateMap();

        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.ready = true;
    }
}
