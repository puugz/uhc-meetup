package me.puugz.meetup.game.kit;

import me.puugz.meetup.util.ItemBuilder;
import me.puugz.meetup.util.PlayerUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Random;
import java.util.stream.Stream;

/**
 * @author puugz
 * @since May 24, 2023
 */
public class KitHandler {

    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public void handleKit(Player player) {
        final PlayerInventory inventory = player.getInventory();
        inventory.clear();

        boolean isAllDiamond;

        do {
            inventory.setHelmet(new ItemBuilder(this.getHelmet())
                    .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, this.getLevel())
                    .build()
            );
            inventory.setChestplate(new ItemBuilder(this.getChestplate())
                    .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, this.getLevel())
                    .build()
            );
            inventory.setLeggings(new ItemBuilder(this.getLeggings())
                    .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, this.getLevel())
                    .build()
            );
            inventory.setBoots(new ItemBuilder(this.getBoots())
                    .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, this.getLevel())
                    .build()
            );
            isAllDiamond = Stream.of(inventory.getArmorContents())
                    .allMatch(stack -> stack.getType().name().startsWith("DIAMOND_"));
        } while (isAllDiamond);

        final ItemStack sword = new ItemStack(this.getSword());
        sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, getLevel());

        final ItemStack bow = new ItemStack(Material.BOW);
        bow.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, RANDOM.nextInt(3) + 1);

        final ItemStack head = PlayerUtil.GOLDEN_HEAD;
        head.setAmount(RANDOM.nextInt(3) + 2);

        inventory.setItem(0, sword);
        inventory.setItem(1, new ItemStack(Material.FISHING_ROD));
        inventory.setItem(2, bow);
        inventory.setItem(3, new ItemStack(RANDOM.nextInt(2) == 0 ? Material.COBBLESTONE : Material.WOOD, 64));
        inventory.setItem(4, new ItemStack(Material.GOLDEN_APPLE, RANDOM.nextInt(4) + 4));
        inventory.setItem(5, head);
        inventory.setItem(6, new ItemStack(Material.DIAMOND_AXE));
        inventory.setItem(7, new ItemStack(Material.FLINT_AND_STEEL));
        inventory.setItem(8, new ItemStack(Material.COOKED_BEEF, 64));

        inventory.setItem(9, new ItemStack(Material.ARROW, 64));
        inventory.setItem(10, new ItemBuilder(Material.LAVA_BUCKET).amount(1).build());
        inventory.setItem(11, new ItemBuilder(Material.WATER_BUCKET).amount(1).build());

        inventory.setItem(14, new ItemStack(Material.DIAMOND_PICKAXE));
        inventory.setItem(15, new ItemStack(Material.ENCHANTMENT_TABLE));
        inventory.setItem(16, new ItemStack(Material.ANVIL, RANDOM.nextInt(2) + 1));
        inventory.setItem(17, new ItemStack(Material.EXP_BOTTLE, 64));

        inventory.setItem(inventory.firstEmpty(), new ItemBuilder(Material.LAVA_BUCKET).amount(1).build());
        inventory.setItem(inventory.firstEmpty(), new ItemBuilder(Material.WATER_BUCKET).amount(1).build());

        player.updateInventory();
    }

    private Material getHelmet() {
        return RANDOM.nextInt(100) >= 50
                ? Material.DIAMOND_HELMET
                : Material.IRON_HELMET;
    }

    private Material getChestplate() {
        return RANDOM.nextInt(100) >= 60
                ? Material.DIAMOND_CHESTPLATE
                : Material.IRON_CHESTPLATE;
    }

    private Material getLeggings() {
        return RANDOM.nextInt(100) >= 60
                ? Material.DIAMOND_LEGGINGS
                : Material.IRON_LEGGINGS;
    }

    private Material getBoots() {
        return RANDOM.nextInt(100) >= 50
                ? Material.DIAMOND_BOOTS
                : Material.IRON_BOOTS;
    }

    private Material getSword() {
        return RANDOM.nextInt(100) >= 50
                ? Material.DIAMOND_SWORD
                : Material.IRON_SWORD;
    }

    private int getLevel() {
        final int r = RANDOM.nextInt(100);
        return r >= 65 ? 3 : r >= 35 ? 2 : 1;
    }
}
