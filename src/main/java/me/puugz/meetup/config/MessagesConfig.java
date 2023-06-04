package me.puugz.meetup.config;

import org.bukkit.ChatColor;
import xyz.mkotb.configapi.Coloured;

import java.util.Arrays;
import java.util.List;

/**
 * @author puugz
 * @since May 22, 2023
 */
public class MessagesConfig {

    @Coloured
    public final String navigationMenuTitle = ChatColor.GRAY + "Navigation";

    @Coloured
    public final String navigationItem = ChatColor.BLUE + "Navigator";

    @Coloured
    public final String nowSpectating = ChatColor.GRAY + "You are now a spectator of this game.";

    @Coloured
    public final String teleportInsideBorder = ChatColor.RED + "You have been teleported inside the border.";

    @Coloured
    public final String playerJoined = ChatColor.BLUE + "{player} " + ChatColor.GRAY + "has joined the game.";

    @Coloured
    public final String playerQuit = ChatColor.BLUE + "{player} " + ChatColor.GRAY + "has left the game.";

    @Coloured
    public final String playerDisqualified = ChatColor.BLUE + "{player} " + ChatColor.RED + "has left and been disqualified!";

    @Coloured
    public final String startingCancelled = ChatColor.RED + "The game has been cancelled due to a player leaving.";

    @Coloured
    public final String minimumRequiredPlayers = ChatColor.GRAY + "Minimum of {min} players is required for the game to start.";

    @Coloured
    public final String borderShrink = ChatColor.GRAY + "The border will shrink to " + ChatColor.BLUE + "{size} " + ChatColor.GRAY + "in " + ChatColor.BLUE + "{time}" + ChatColor.GRAY + ".";

    @Coloured
    public final String borderShrunk = ChatColor.GRAY + "The border has shrunk to " + ChatColor.BLUE + "{size}" + ChatColor.GRAY + ".";

    @Coloured
    public final String killedBy = ChatColor.GRAY + "You have been killed by " + ChatColor.BLUE + "{killer}" + ChatColor.GRAY + ".";

    @Coloured
    public final String killedPlayer = ChatColor.GRAY + "You have killed " + ChatColor.BLUE + "{victim}" + ChatColor.GRAY + ".";

    @Coloured
    public final String slainByKiller = ChatColor.BLUE + "{victim} " + ChatColor.GRAY + "was slain by " + ChatColor.BLUE + "{killer}" + ChatColor.GRAY + ".";

    @Coloured
    public final String died = ChatColor.BLUE + "{player} " + ChatColor.GRAY + "has died.";

    @Coloured
    public final String serverRestarting = ChatColor.GRAY + "The server will be restarting in " + ChatColor.BLUE + "{time}" + ChatColor.GRAY + ".";

    @Coloured
    public final String serverRestart = ChatColor.RED + "Server restarting...";

    // TODO: Make the whole win message configurable
    @Coloured
    public final String win = ChatColor.BLUE + "{winner} " + ChatColor.GREEN + "wins!";

    @Coloured
    public final String spectatorPrefix = ChatColor.GRAY + "[Spectator] " + ChatColor.RESET + "{format}";

    @Coloured
    public final String cantForcestart = ChatColor.RED + "You can only force-start while in the starting state.";

    @Coloured
    public final String cantForcestart2 = ChatColor.RED + "The countdown is already at or below 5 seconds.";

    @Coloured
    public final String forcestarted = ChatColor.GREEN + "Forced the countdown to 5 seconds!";

    @Coloured
    public final String gameStarting = ChatColor.GRAY + "The game will begin in " + ChatColor.BLUE + "{time}" + ChatColor.GRAY + ".";

    @Coloured
    public final String gameStarted = ChatColor.GRAY + "The game has started!";

    @Coloured
    public final String playerOnlyCommand = ChatColor.RED + "Only players may execute this command!";

    @Coloured
    public final String spawnSet = ChatColor.GREEN + "Spawn location has been set.";

    @Coloured
    public final String cantSetSpawnHere = ChatColor.RED + "You can't set the spawn in this world.";

    @Coloured
    public final String timeBombExplosion = ChatColor.BLUE + "{player}" + ChatColor.GRAY + "'s corspe has exploded!";

    @Coloured
    public final String teleportedTo = ChatColor.GRAY + "You have teleported to " + ChatColor.BLUE + "{player}" + ChatColor.GRAY + ".";

    @Coloured
    public final String clickToTeleport = ChatColor.GREEN + "Click to teleport.";

    @Coloured
    public final String notReady = ChatColor.RED + "The server is currently booting up. Please try again in a bit.";

    @Coloured
    public final String arrowHit = ChatColor.BLUE + "{player} " + ChatColor.GRAY + "is now at " + ChatColor.RED + "{health}\u2764" + ChatColor.GRAY + ".";

    @Coloured
    public final String noCleanHostileAction = ChatColor.RED + "Your no clean timer has expired due to hostile action!";

    @Coloured
    public final String noCleanTargetExpiresIn = ChatColor.RED + "{player}'s no clean timer expires in {time}.";

    @Coloured
    public final String noCleanExpiresIn = ChatColor.RED + "Your no clean timer expires in {time}.";

    @Coloured
    public final String noCleanExpired = ChatColor.RED + "Your no clean timer has expired!";

    @Coloured
    public final String fetchingDataError = ChatColor.RED + "Couldn't fetch data for {player}: {reason}";

    @Coloured
    public final String noPlayerData = ChatColor.RED + "A player with that name does not exist in the database!";

    @Coloured
    public final List<String> stats = Arrays.asList(
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-----------------",
            ChatColor.BLUE + "{player}'s " + ChatColor.RESET + "stats:",
            "",
            ChatColor.GRAY + "- " + ChatColor.BLUE + "Kills: " + ChatColor.RESET + "{kills}",
            ChatColor.GRAY + "- " + ChatColor.BLUE + "Deaths: " + ChatColor.RESET + "{deaths}",
            ChatColor.GRAY + "- " + ChatColor.BLUE + "Games Played: " + ChatColor.RESET + "{games_played}",
            ChatColor.GRAY + "- " + ChatColor.BLUE + "Games Won: " + ChatColor.RESET + "{games_won}",
            ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-----------------"
    );

    @Coloured
    public final String cantPlaceBlocksHere = ChatColor.RED + "You can't place blocks here!";

    @Coloured
    public final String cantBreakBlocksHere = ChatColor.RED + "You can't break blocks here!";
}
