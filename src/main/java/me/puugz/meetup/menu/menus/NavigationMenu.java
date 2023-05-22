package me.puugz.meetup.menu.menus;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.player.GamePlayer;
import me.puugz.meetup.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;
import java.util.Collections;

/**
 * @author puugz
 * @since May 08, 2023
 */
public class NavigationMenu extends Menu {

    public NavigationMenu() {
        super(ChatColor.YELLOW + "Navigation", 27);
    }

    @Override
    public void handleClick(InventoryClickEvent event) {
        final ItemStack item = event.getCurrentItem();
        final ItemMeta meta = item.getItemMeta();

        if (!meta.hasDisplayName())
            return;

        final String name = ChatColor.stripColor(meta.getDisplayName());
        final Player target = Bukkit.getPlayer(name);

        if (target != null) {
            event.getWhoClicked().teleport(target);
            event.getWhoClicked().sendMessage(
                    ChatColor.GRAY + "You have teleported to "
                            + ChatColor.GOLD + target.getName()
                            + ChatColor.GRAY + "."
            );
        }
    }

    @Override
    public void populateMenu() {
        for (GamePlayer gamePlayer : UHCMeetup.getInstance().getPlayerHandler().alive()) {
            final ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            final SkullMeta meta = (SkullMeta) head.getItemMeta();

            meta.setOwner(gamePlayer.getName());
            meta.setDisplayName(ChatColor.GOLD + gamePlayer.getName());
            meta.setLore(Collections.singletonList(ChatColor.GREEN + "Click to teleport."));
            head.setItemMeta(meta);

            this.inventory.addItem(head);
        }
    }
}
