package me.puugz.meetup.store;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.player.GamePlayer;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author puugz
 * @since May 29, 2023
 */
@Getter
public class MongoHandler {

    private final MongoClient client;
    private final MongoDatabase database;

    private final MongoRepository<GamePlayer> playerRepository;

    public MongoHandler() {
        final ConfigurationSection mongo = UHCMeetup.getInstance().getConfig()
                .getConfigurationSection("mongo");

        final String host = mongo.getString("host");
        final int port = mongo.getInt("port");
        final String database = mongo.getString("database");

        if (!mongo.getBoolean("authentication.enabled")) {
            this.client = new MongoClient(new MongoClientURI("mongodb://" + host + ":" + port));
        } else {
            final String user = mongo.getString("authentication.user");
            final String password = mongo.getString("authentication.password");

            this.client = new MongoClient(new MongoClientURI("mongodb://" + user + ":" + password + "@" + host + ":" + port));
        }

        this.database = this.client.getDatabase(database);
        this.playerRepository = new MongoRepository<>("meetup_players", GamePlayer.class, this.database);
    }

    public void close() {
        this.client.close();
    }
}
