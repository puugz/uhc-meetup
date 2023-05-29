package me.puugz.meetup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.LongSerializationPolicy;
import io.github.nosequel.scoreboard.ScoreboardHandler;
import lombok.Getter;
import me.puugz.meetup.command.ForceStartCommand;
import me.puugz.meetup.command.SetSpawnCommand;
import me.puugz.meetup.command.StatsCommand;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.config.ScoreboardsConfig;
import me.puugz.meetup.config.SettingsConfig;
import me.puugz.meetup.game.border.BorderHandler;
import me.puugz.meetup.game.kit.KitHandler;
import me.puugz.meetup.game.map.MapHandler;
import me.puugz.meetup.game.player.GamePlayer;
import me.puugz.meetup.game.player.PlayerHandler;
import me.puugz.meetup.game.player.listener.PlayerListener;
import me.puugz.meetup.game.scenario.ScenarioHandler;
import me.puugz.meetup.game.scoreboard.ScoreboardProvider;
import me.puugz.meetup.game.state.StateHandler;
import me.puugz.meetup.store.MongoHandler;
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

    public static final Gson GSON = new GsonBuilder()
            .setLongSerializationPolicy(LongSerializationPolicy.STRING)
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .serializeNulls()
            .create();

    private boolean ready;

    private ConfigFactory configFactory;
    private SettingsConfig settingsConfig;
    private MessagesConfig messagesConfig;
    private ScoreboardsConfig scoreboardsConfig;

    private MongoHandler mongoHandler;
    private PlayerHandler playerHandler;
    private BorderHandler borderHandler;
    private StateHandler stateHandler;
    private MapHandler mapHandler;
    private ScenarioHandler scenarioHandler;
    private KitHandler kitHandler;

    @Override
    public void onEnable() {
        instance = this;

        // Tried to use a class-based config for settings,
        // but had a stack overflow upon saving
        this.saveDefaultConfig();

        this.configFactory = ConfigFactory.newFactory(this);
        this.settingsConfig = this.configFactory.fromFile("settings", SettingsConfig.class);
        this.messagesConfig = this.configFactory.fromFile("messages", MessagesConfig.class);
        this.scoreboardsConfig = this.configFactory.fromFile("scoreboards", ScoreboardsConfig.class);

        this.mongoHandler = new MongoHandler();
        this.playerHandler = new PlayerHandler();
        this.borderHandler = new BorderHandler();
        this.stateHandler = new StateHandler();
        this.mapHandler = new MapHandler();
        this.scenarioHandler = new ScenarioHandler();
        this.kitHandler = new KitHandler();

        new ScoreboardHandler(this, new ScoreboardProvider(), 5L);

        this.getCommand("forcestart").setExecutor(new ForceStartCommand());
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        this.getCommand("stats").setExecutor(new StatsCommand());
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        this.ready = true;
    }

    @Override
    public void onDisable() {
        this.playerHandler
                .getPlayers()
                .values()
                .forEach(GamePlayer::save);

        this.mongoHandler.close();
        this.saveConfig();
    }
}
