package me.puugz.meetup.config;

import org.bukkit.ChatColor;
import xyz.mkotb.configapi.Coloured;

/**
 * @author puugz
 * @since May 22, 2023
 */
public class MessagesConfig {

    // TODO: Organize (preferably into sections)

    @Coloured
    public final String navigationMenuTitle = ChatColor.YELLOW + "Navigation";

    @Coloured
    public final String navigationItem = ChatColor.GOLD + "Navigator";

    @Coloured
    public final String nowSpectating = ChatColor.YELLOW + "You are now a spectator of this game.";

    @Coloured
    public final String teleportInsideBorder = ChatColor.RED + "You have been teleported inside the border.";

    @Coloured
    public final String playerJoined = ChatColor.GOLD + "{player} " + ChatColor.YELLOW + "has joined the game.";

    @Coloured
    public final String playerQuit = ChatColor.GOLD + "{player} " + ChatColor.YELLOW + "has left the game.";

    @Coloured
    public final String playerDisqualified = ChatColor.GOLD + "{player} " + ChatColor.RED + "has left and been disqualified!";

    @Coloured
    public final String startingCancelled = ChatColor.RED + "The game has been cancelled due to a player leaving.";

    @Coloured
    public final String minimumRequiredPlayers = ChatColor.YELLOW + "Minimum of {min} players is required for the game to start.";

    @Coloured
    public final String borderShrink = ChatColor.YELLOW + "The border will shrink to " + ChatColor.GOLD + "{size} " + ChatColor.YELLOW + "in " + ChatColor.GOLD + "{time}" + ChatColor.YELLOW + ".";

    @Coloured
    public final String borderShrunk = ChatColor.YELLOW + "The border has shrunk to " + ChatColor.GOLD + "{size}" + ChatColor.YELLOW + ".";

    @Coloured
    public final String killedBy = ChatColor.YELLOW + "You have been killed by " + ChatColor.GOLD + "{killer}" + ChatColor.YELLOW + ".";

    @Coloured
    public final String killedPlayer = ChatColor.YELLOW + "You have killed " + ChatColor.GOLD + "{victim}" + ChatColor.YELLOW + ".";

    @Coloured
    public final String slainByKiller = ChatColor.GOLD + "{victim} " + ChatColor.YELLOW + "was slain by " + ChatColor.GOLD + "{killer}" + ChatColor.YELLOW + ".";

    @Coloured
    public final String died = ChatColor.GOLD + "{player} " + ChatColor.YELLOW + "has died.";

    @Coloured
    public final String serverRestarting = ChatColor.YELLOW + "The server will be restarting in " + ChatColor.GOLD + "{time}" + ChatColor.YELLOW + ".";

    @Coloured
    public final String serverRestart = ChatColor.RED + "Server restarting...";

    // TODO: Make the whole win message configurable
    @Coloured
    public final String win = ChatColor.GOLD + "{winner} " + ChatColor.GREEN + "wins!";

    @Coloured
    public final String spectatorPrefix = ChatColor.GRAY + "[Spectator] " + ChatColor.RESET + "{format}";

    @Coloured
    public final String cantForcestart = ChatColor.RED + "You can only force-start while in the starting state.";

    @Coloured
    public final String cantForcestart2 = ChatColor.RED + "The countdown is already at or below 5 seconds.";

    @Coloured
    public final String forcestarted = ChatColor.GREEN + "Forced the countdown to 5 seconds!";

    @Coloured
    public final String gameStarting = ChatColor.YELLOW + "The game will begin in " + ChatColor.GOLD + "{time}" + ChatColor.YELLOW + ".";

    @Coloured
    public final String gameStarted = ChatColor.YELLOW + "The game has started!";

    @Coloured
    public final String playerOnlyCommand = ChatColor.RED + "Only players may execute this command!";

    @Coloured
    public final String spawnSet = ChatColor.GREEN + "Spawn location has been set.";

    @Coloured
    public final String timeBombExplosion = ChatColor.GOLD + "{player}" + ChatColor.YELLOW + "'s corspe has exploded!";

    @Coloured
    public final String teleportedTo = ChatColor.YELLOW + "You have teleported to " + ChatColor.GOLD + "{player}" + ChatColor.YELLOW + ".";

    @Coloured
    public final String clickToTeleport = ChatColor.GREEN + "Click to teleport.";

    @Coloured
    public final String notReady = ChatColor.RED + "The server is currently booting up. Please try again in a bit.";

}
