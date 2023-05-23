package me.puugz.meetup.command;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.game.state.GameState;
import me.puugz.meetup.game.state.states.StartingState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * @author puugz
 * @since May 22, 2023
 */
public class ForceStartCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        final GameState currentState = UHCMeetup.getInstance()
                .getStateHandler()
                .getCurrentState();
        final MessagesConfig messages = UHCMeetup.getInstance()
                .getMessagesConfig();

        if (!(currentState instanceof StartingState)) {
            sender.sendMessage(messages.cantForcestart);
            return true;
        }

        final StartingState startingState = (StartingState) currentState;

        if (startingState.getCountdown().getSeconds() < 5) {
            sender.sendMessage(messages.cantForcestart2);
            return true;
        }

        startingState.getCountdown().setSeconds(5);
        sender.sendMessage(messages.forcestarted);

        return true;
    }
}
