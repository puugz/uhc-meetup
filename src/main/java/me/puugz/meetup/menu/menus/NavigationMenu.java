package me.puugz.meetup.menu.menus;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.config.MessagesConfig;
import me.puugz.meetup.menu.Menu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Collections;

/**
 * @author puugz
 * @since May 08, 2023
 */
public class NavigationMenu extends Menu {

    private final MessagesConfig messages = UHCMeetup.getInstance()
            .getMessagesConfig();

    public NavigationMenu() {
        super(27);
    }

    @Override
    public String getTitle() {
        return messages.navigationMenuTitle;
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
            event.getWhoClicked().sendMessage(this.messages.teleportedTo
                    .replace("{player}", target.getName()));
        }
    }

    @Override
    public void populateMenu() {
        UHCMeetup.getInstance()
                .getPlayerHandler()
                .players()
                .forEach(player -> {
                    final ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
                    final SkullMeta meta = (SkullMeta) head.getItemMeta();

                    meta.setOwner(player.getName());
                    meta.setDisplayName(ChatColor.GOLD + player.getName());
                    meta.setLore(Collections.singletonList(this.messages.clickToTeleport));
                    head.setItemMeta(meta);

                    this.inventory.addItem(head);
                });
    }
}
