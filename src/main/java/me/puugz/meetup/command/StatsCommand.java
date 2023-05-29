package me.puugz.meetup.command;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author puugz
 * @since May 29, 2023
 */
public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        final MessagesConfig messages = UHCMeetup.getInstance()
                .getMessagesConfig();

        if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(messages.playerOnlyCommand);
            return true;
        }

        String targetName = sender.getName();

        if (args.length > 0)
            targetName = args[0];

        final String finalTargetName = targetName;
        UHCMeetup.getInstance()
                .getPlayerHandler()
                .find(targetName)
                .whenComplete(((gamePlayer, throwable) -> {
                    if (throwable != null) {
                        sender.sendMessage(ChatColor.RED + "Couldn't fetch data for " + finalTargetName + ": " + throwable.getMessage());
                    } else {
                        if (gamePlayer != null) {
                            UHCMeetup.getInstance().getPlayerHandler()
                                    .getPlayers()
                                    .computeIfAbsent(gamePlayer.getUuid(), uuid -> gamePlayer);

                            sender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-----------------");
                            sender.sendMessage(ChatColor.BLUE + gamePlayer.getName() +  "'s " + ChatColor.RESET + "stats:");
                            sender.sendMessage("");
                            sender.sendMessage(ChatColor.GRAY + "» " + ChatColor.BLUE + "Kills: " + ChatColor.RESET + gamePlayer.getKills());
                            sender.sendMessage(ChatColor.GRAY + "» " + ChatColor.BLUE + "Deaths: " + ChatColor.RESET + gamePlayer.getDeaths());
                            sender.sendMessage(ChatColor.GRAY + "» " + ChatColor.BLUE + "Games Played: " + ChatColor.RESET + gamePlayer.getGamesPlayed());
                            sender.sendMessage(ChatColor.GRAY + "» " + ChatColor.BLUE + "Games Won: " + ChatColor.RESET + gamePlayer.getGamesWon());
                            sender.sendMessage(ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "-----------------");

                            return;
                        }

                        sender.sendMessage(ChatColor.RED + "There is not a player with that name in the database!");
                    }
                }));

        return true;
    }
}
