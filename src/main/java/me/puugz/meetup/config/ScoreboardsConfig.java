package me.puugz.meetup.config;

import org.bukkit.ChatColor;
import xyz.mkotb.configapi.Coloured;

import java.util.Arrays;
import java.util.List;

/**
 * @author puugz
 * @since May 22, 2023
 */
public class ScoreboardsConfig {

    @Coloured
    public final String title = ChatColor.DARK_BLUE + ChatColor.BOLD.toString() + "UHC Meetup";

    @Coloured
    public final List<String> waiting = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "Waiting for players",
            "",
            ChatColor.DARK_BLUE + "meetup.lol",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );

    @Coloured
    public final List<String> starting = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "The game will start in:",
            ChatColor.DARK_BLUE + "{time}",
            "",
            ChatColor.DARK_BLUE + "meetup.lol",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );

    @Coloured
    public final List<String> playing = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "Border: " + ChatColor.DARK_BLUE + "{border_size} {border_format}",
            "Players: " + ChatColor.DARK_BLUE + "{players}",
            "Ping: " + ChatColor.DARK_BLUE + "{ping} ms",
            "Kills: " + ChatColor.DARK_BLUE + "{kills}",
            "",
            ChatColor.DARK_BLUE + "meetup.lol",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );

    @Coloured
    public final List<String> ending = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "Kills: " + ChatColor.DARK_BLUE + "{kills}",
            "Winner: " + ChatColor.DARK_BLUE + "{winner}",
            "",
            ChatColor.DARK_BLUE + "meetup.lol",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );

    @Coloured
    public final String borderFormat = ChatColor.RESET + "(" + ChatColor.DARK_BLUE + "{border_time}" + ChatColor.RESET + ")";
}
