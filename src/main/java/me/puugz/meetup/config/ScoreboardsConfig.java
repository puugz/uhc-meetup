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
    public final String title = ChatColor.GOLD + "UHC Meetup";

    @Coloured
    public final List<String> waiting = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "Waiting for players",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );

    @Coloured
    public final List<String> starting = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "The game will start in:",
            ChatColor.GOLD + "{time}",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );

    @Coloured
    public final List<String> playing = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "Border: " + ChatColor.GOLD + "{border_size} " + ChatColor.RESET + "(" + ChatColor.GOLD + "{border_time}" + ChatColor.RESET + ")",
            "Players: " + ChatColor.GOLD + "{players}",
            "Ping: " + ChatColor.GOLD + "{ping} ms",
            "Kills: " + ChatColor.GOLD + "{kills}",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );

    @Coloured
    public final List<String> ending = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------",
            "Kills: " + ChatColor.GOLD + "{kills}",
            "Winner: " + ChatColor.GOLD + "{winner}",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "--------------------"
    );
}
