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
    public final String navigationMenuTitle = ChatColor.GRAY + "Navigation";

    @Coloured
    public final String navigationItem = ChatColor.DARK_BLUE + "Navigator";

    @Coloured
    public final String nowSpectating = ChatColor.GRAY + "You are now a spectator of this game.";

    @Coloured
    public final String teleportInsideBorder = ChatColor.RED + "You have been teleported inside the border.";

    @Coloured
    public final String playerJoined = ChatColor.DARK_BLUE + "{player} " + ChatColor.GRAY + "has joined the game.";

    @Coloured
    public final String playerQuit = ChatColor.DARK_BLUE + "{player} " + ChatColor.GRAY + "has left the game.";

    @Coloured
    public final String playerDisqualified = ChatColor.DARK_BLUE + "{player} " + ChatColor.RED + "has left and been disqualified!";

    @Coloured
    public final String startingCancelled = ChatColor.RED + "The game has been cancelled due to a player leaving.";

    @Coloured
    public final String minimumRequiredPlayers = ChatColor.GRAY + "Minimum of {min} players is required for the game to start.";

    @Coloured
    public final String borderShrink = ChatColor.GRAY + "The border will shrink to " + ChatColor.DARK_BLUE + "{size} " + ChatColor.GRAY + "in " + ChatColor.DARK_BLUE + "{time}" + ChatColor.GRAY + ".";

    @Coloured
    public final String borderShrunk = ChatColor.GRAY + "The border has shrunk to " + ChatColor.DARK_BLUE + "{size}" + ChatColor.GRAY + ".";

    @Coloured
    public final String killedBy = ChatColor.GRAY + "You have been killed by " + ChatColor.DARK_BLUE + "{killer}" + ChatColor.GRAY + ".";

    @Coloured
    public final String killedPlayer = ChatColor.GRAY + "You have killed " + ChatColor.DARK_BLUE + "{victim}" + ChatColor.GRAY + ".";

    @Coloured
    public final String slainByKiller = ChatColor.DARK_BLUE + "{victim} " + ChatColor.GRAY + "was slain by " + ChatColor.DARK_BLUE + "{killer}" + ChatColor.GRAY + ".";

    @Coloured
    public final String died = ChatColor.DARK_BLUE + "{player} " + ChatColor.GRAY + "has died.";

    @Coloured
    public final String serverRestarting = ChatColor.GRAY + "The server will be restarting in " + ChatColor.DARK_BLUE + "{time}" + ChatColor.GRAY + ".";

    @Coloured
    public final String serverRestart = ChatColor.RED + "Server restarting...";

    // TODO: Make the whole win message configurable
    @Coloured
    public final String win = ChatColor.DARK_BLUE + "{winner} " + ChatColor.GREEN + "wins!";

    @Coloured
    public final String spectatorPrefix = ChatColor.GRAY + "[Spectator] " + ChatColor.RESET + "{format}";

    @Coloured
    public final String cantForcestart = ChatColor.RED + "You can only force-start while in the starting state.";

    @Coloured
    public final String cantForcestart2 = ChatColor.RED + "The countdown is already at or below 5 seconds.";

    @Coloured
    public final String forcestarted = ChatColor.GREEN + "Forced the countdown to 5 seconds!";

    @Coloured
    public final String gameStarting = ChatColor.GRAY + "The game will begin in " + ChatColor.DARK_BLUE + "{time}" + ChatColor.GRAY + ".";

    @Coloured
    public final String gameStarted = ChatColor.GRAY + "The game has started!";

    @Coloured
    public final String playerOnlyCommand = ChatColor.RED + "Only players may execute this command!";

    @Coloured
    public final String spawnSet = ChatColor.GREEN + "Spawn location has been set.";

    @Coloured
    public final String timeBombExplosion = ChatColor.DARK_BLUE + "{player}" + ChatColor.GRAY + "'s corspe has exploded!";

    @Coloured
    public final String teleportedTo = ChatColor.GRAY + "You have teleported to " + ChatColor.DARK_BLUE + "{player}" + ChatColor.GRAY + ".";

    @Coloured
    public final String clickToTeleport = ChatColor.GREEN + "Click to teleport.";

    @Coloured
    public final String notReady = ChatColor.RED + "The server is currently booting up. Please try again in a bit.";

}
