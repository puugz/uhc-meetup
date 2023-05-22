package me.puugz.meetup.command;

import me.puugz.meetup.UHCMeetup;
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

        if (!(currentState instanceof StartingState)) {
            sender.sendMessage(ChatColor.RED + "You can only force-start while in the starting state.");
            return true;
        }

        final StartingState startingState = (StartingState) currentState;

        if (startingState.getCountdown().getSeconds() < 5) {
            sender.sendMessage(ChatColor.RED + "The countdown is already at or below 5 seconds.");
            return true;
        }

        startingState.getCountdown().setSeconds(5);
        sender.sendMessage(ChatColor.GREEN + "Forced the countdown to 5 seconds!");

        return true;
    }
}
