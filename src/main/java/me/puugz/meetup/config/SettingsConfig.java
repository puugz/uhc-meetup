package me.puugz.meetup.config;

import xyz.mkotb.configapi.comment.Comment;

/**
 * @author puugz
 * @since Jun 10, 2023
 */
public class SettingsConfig {

    @Comment({"The amount of players the game needs to begin"})
    public int minRequiredPlayers = 2;

    @Comment({"How long it takes for the game to begin after there is enough players"})
    public int startingTime = 60;

    @Comment({"How long it takes for the server to restart after the game ends"})
    public int endingTime = 10;

    @Comment({"The command to be used to restart the server when the game ends"})
    public String restartCommand = "restart";

    @Comment({"The size of the first border"})
    public int initialBorderSize = 125;

    @Comment({"The height of the bedrock border walls"})
    public int borderWallHeight = 5;

    @Comment({"The amount of time between a border being shrunk"})
    public int borderShrinkTime = 120;

    @Comment({"The amount of time the no clean timer will be active"})
    public int noCleanTime = 15;

}
