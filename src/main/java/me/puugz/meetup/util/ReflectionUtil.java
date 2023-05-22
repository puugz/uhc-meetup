package me.puugz.meetup.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

/**
 * @author puugz
 * @since May 19, 2023
 */
@UtilityClass
public class ReflectionUtil {

    private static final String VERSION = Bukkit.getServer()
            .getClass().getPackage()
            .getName().split("\\.")[3];

    private static final String CRAFT_BUKKIT_PGK = "org.bukkit.craftbukkit." + VERSION;

}
