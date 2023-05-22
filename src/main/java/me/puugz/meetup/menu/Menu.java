package me.puugz.meetup.menu;

import lombok.RequiredArgsConstructor;
import me.puugz.meetup.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author puugz
 * @since April 13, 2022
 */
@RequiredArgsConstructor
public abstract class Menu implements InventoryHolder {

    protected Inventory inventory;
    private final String title;
    private final int size;

    public abstract void handleClick(InventoryClickEvent event);

    public abstract void populateMenu();

    public void openMenu(Player player) {
        if (this.inventory == null) {
            this.inventory = Bukkit.createInventory(this, this.size, this.title);
        }
        this.inventory.clear();
        this.populateMenu();

        player.openInventory(this.inventory);
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }
}
