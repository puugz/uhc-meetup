package me.puugz.meetup.util;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * @author puugz
 * @since May 24, 2023
 */
public class ItemBuilder {

    private final ItemStack stack;

    public ItemBuilder(Material material) {
        this.stack = new ItemStack(material);
    }

    public ItemBuilder name(String name) {
        final ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.stack.setAmount(amount);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        this.stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemStack build() {
        return this.stack;
    }
}
