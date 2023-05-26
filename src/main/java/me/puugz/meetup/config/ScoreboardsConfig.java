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
    public final String title = ChatColor.BLUE + ChatColor.BOLD.toString() + "UHC Meetup";

    @Coloured
    public final List<String> waiting = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "Waiting for players",
            "",
            ChatColor.BLUE + "meetup.lol",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );

    @Coloured
    public final List<String> starting = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "The game will start in:",
            ChatColor.BLUE + "{time}",
            "",
            ChatColor.BLUE + "meetup.lol",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );

    @Coloured
    public final List<String> playing = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "Border: " + ChatColor.BLUE + "{border_size} {border_format}",
            "Players: " + ChatColor.BLUE + "{players}",
            "Ping: " + ChatColor.BLUE + "{ping} ms",
            "Kills: " + ChatColor.BLUE + "{kills}",
            "",
            ChatColor.BLUE + "meetup.lol",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );

    @Coloured
    public final List<String> ending = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "Kills: " + ChatColor.BLUE + "{kills}",
            "Winner: " + ChatColor.BLUE + "{winner}",
            "",
            ChatColor.BLUE + "meetup.lol",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );

    @Coloured
    public final String borderFormat = ChatColor.RESET + "(" + ChatColor.RED + "{border_time}" + ChatColor.RESET + ")";
}
