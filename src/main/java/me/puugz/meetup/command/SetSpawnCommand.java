package me.puugz.meetup.command;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.util.LocationUtil;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author puugz
 * @since May 23, 2023
 */
public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        final MessagesConfig messages = UHCMeetup.getInstance()
                .getMessagesConfig();

        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.playerOnlyCommand);
            return true;
        }

        final Location spawnLocation = ((Player) sender).getLocation();

        if (spawnLocation.getWorld().getName().equals("uhc_meetup")) {
            sender.sendMessage(messages.cantSetSpawnHere);
            return true;
        }

        final UHCMeetup plugin = UHCMeetup.getInstance();

        plugin.getConfig().set("spawn-location", LocationUtil.locationToString(spawnLocation));
        plugin.getMapHandler().setSpawnLocation(spawnLocation);

        sender.sendMessage(messages.spawnSet);

        return true;
    }
}
