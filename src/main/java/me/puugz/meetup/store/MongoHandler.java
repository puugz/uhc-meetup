package me.puugz.meetup.store;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.SettingsConfig;
import me.puugz.meetup.game.player.GamePlayer;

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
        final SettingsConfig settings = UHCMeetup.getInstance().getSettingsConfig();

        final String host = settings.mongo.host;
        final int port = settings.mongo.port;
        final String database = settings.mongo.database;

        if (!settings.mongo.authentication.enabled) {
            this.client = new MongoClient(new MongoClientURI("mongodb://" + host + ":" + port));
        } else {
            final String user = settings.mongo.authentication.user;
            final String password = settings.mongo.authentication.password;

            this.client = new MongoClient(new MongoClientURI("mongodb://" + user + ":" + password + "@" + host + ":" + port));
        }

        this.database = this.client.getDatabase(database);
        this.playerRepository = new MongoRepository<>("meetup_players", GamePlayer.class, this.database);
    }

    public void close() {
        this.client.close();
    }
}
