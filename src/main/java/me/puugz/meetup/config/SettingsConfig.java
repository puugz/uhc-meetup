package me.puugz.meetup.config;

/**
 * @author puugz
 * @since May 29, 2023
 */
public class SettingsConfig {

    public final Mongo mongo = new Mongo();

    public static class Mongo {

        public final String host = "127.0.0.1";
        public final int port = 27017;
        public final String database = "uhc_meetup";
        public final Authentication authentication = new Authentication();

        public static class Authentication {
            public final boolean enabled = false;
            public final String user = "admin";
            public final String password = "admin";
        }
    }
}
