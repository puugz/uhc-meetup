package me.puugz.meetup.command;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.util.LocationUtil;
import org.bukkit.ChatColor;
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
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players may execute this command!");
            return true;
        }

        final Location spawnLocation = ((Player) sender).getLocation();
        final UHCMeetup plugin = UHCMeetup.getInstance();

        plugin.getConfig().set("spawn-location", LocationUtil.locationToString(spawnLocation));
        plugin.getMapHandler().setSpawnLocation(spawnLocation);

        sender.sendMessage(ChatColor.GREEN + "Spawn location has been set.");

        return true;
    }
}
