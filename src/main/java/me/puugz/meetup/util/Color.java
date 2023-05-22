package me.puugz.meetup.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

/**
 * @author puugz
 * @since May 08, 2023
 */
@UtilityClass
public class Color {

    public String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}