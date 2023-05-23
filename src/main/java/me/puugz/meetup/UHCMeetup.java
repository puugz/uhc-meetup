package me.puugz.meetup;

import io.github.nosequel.scoreboard.ScoreboardHandler;
import lombok.Getter;
import me.puugz.meetup.command.ForceStartCommand;
import me.puugz.meetup.command.SetSpawnCommand;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.config.ScoreboardsConfig;
import me.puugz.meetup.game.border.BorderHandler;
import me.puugz.meetup.game.map.MapHandler;
import me.puugz.meetup.game.player.PlayerHandler;
import me.puugz.meetup.game.player.listener.PlayerListener;
import me.puugz.meetup.game.scenario.ScenarioHandler;
import me.puugz.meetup.game.scoreboard.ScoreboardProvider;
import me.puugz.meetup.game.state.StateHandler;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.mkotb.configapi.ConfigFactory;

/**
 * @author puugz
 * @since May 08, 2023
 */
@Getter
public class UHCMeetup extends JavaPlugin {

    @Getter
    private static UHCMeetup instance;

    private boolean ready;

    private ConfigFactory configFactory;
    private MessagesConfig messagesConfig;
    private ScoreboardsConfig scoreboardsConfig;

    private PlayerHandler playerHandler;
    private BorderHandler borderHandler;
    private StateHandler stateHandler;
    private MapHandler mapHandler;
    private ScenarioHandler scenarioHandler;

    @Override
    public void onEnable() {
        instance = this;

        // Tried to use a class-based config for settings,
        // but had a stack overflow upon saving
        this.saveDefaultConfig();

        this.configFactory = ConfigFactory.newFactory(this);
        this.messagesConfig = this.configFactory.fromFile("messages", MessagesConfig.class);
        this.scoreboardsConfig = this.configFactory.fromFile("scoreboards", ScoreboardsConfig.class);

        this.playerHandler = new PlayerHandler();
        this.borderHandler = new BorderHandler();
        this.stateHandler = new StateHandler();
        this.mapHandler = new MapHandler();
        this.scenarioHandler = new ScenarioHandler();

        new ScoreboardHandler(this, new ScoreboardProvider(), 10L);

        this.getCommand("forcestart").setExecutor(new ForceStartCommand());
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        this.ready = true;
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }
}
