package me.puugz.meetup.command;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
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
                        sender.sendMessage(messages.fetchingDataError
                                .replace("{player}", finalTargetName)
                                .replace("{reason}", throwable.getMessage()));
                    } else {
                        if (gamePlayer != null) {
                            UHCMeetup.getInstance().getPlayerHandler()
                                    .getPlayers()
                                    .computeIfAbsent(gamePlayer.getUuid(), uuid -> gamePlayer);

                            for (String s : messages.stats) {
                                sender.sendMessage(s
                                        .replace("{player}", gamePlayer.getName())
                                        .replace("{kills}", "" + gamePlayer.getKills())
                                        .replace("{deaths}", "" + gamePlayer.getDeaths())
                                        .replace("{games_played}", "" + gamePlayer.getGamesPlayed())
                                        .replace("{games_won}", "" + gamePlayer.getGamesWon())
                                );
                            }

                            return;
                        }

                        sender.sendMessage(messages.noPlayerData);
                    }
                }));

        return true;
    }
}
