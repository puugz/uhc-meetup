package me.puugz.meetup.game.map.thread;

import me.puugz.meetup.UHCMeetup;
import me.puugz.meetup.game.border.BorderHandler;
import net.minecraft.server.v1_8_R3.BiomeBase;
import org.apache.commons.io.FileUtils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;

/**
 * @author puugz
 * @since May 20, 2023
 */
public class MapGenerator extends BukkitRunnable {

    private boolean isGenerating;
    private final long startTime = System.currentTimeMillis();

    public MapGenerator() throws IOException {
        FileUtils.deleteDirectory(new File("uhc_meetup"));
        this.swapBiomes();
    }

    @Override
    public void run() {
        if (!this.isGenerating)
            this.generateWorld();
    }

    private void generateWorld() {
        this.isGenerating = true;

        final BorderHandler borderHandler = UHCMeetup.getInstance().getBorderHandler();

        final WorldCreator worldCreator = new WorldCreator("uhc_meetup");
        worldCreator.generateStructures(false);

        final World world = worldCreator.createWorld();

        final int startX = -borderHandler.getBorderSize();
        final int endX = borderHandler.getBorderSize();
        final int startZ = -borderHandler.getBorderSize();
        final int endZ = borderHandler.getBorderSize();

        // Load chunks inside the border region
        for (int x = startX; x <= endX; x += 16) {
            for (int z = startZ; z <= endZ; z += 16) {
                world.loadChunk(x >> 4, z >> 4);
            }
        }

        // Set bedrock at Y level 50
        for (int cx = startX >> 4; cx <= endX >> 4; cx++) {
            for (int cz = startZ >> 4; cz <= endZ >> 4; cz++) {
                final Chunk chunk = world.getChunkAt(cx, cz);

                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        final Block block = chunk.getBlock(x, 50, z);
                        block.setType(Material.BEDROCK);
                    }
                }
            }
        }

        // TODO: Check if there is too much water, if so, re-generate the world

        // Set the world border & generate the bedrock walls
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(borderHandler.getBorderSize() * 2);
        borderHandler.generateBedrockWall(world);

        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("naturalRegeneration", "false");

        this.cancel();
        UHCMeetup.getInstance().getLogger().info("Generated game world in "
                + (System.currentTimeMillis() - startTime) + " ms.");
    }

    private void swapBiomes() {
        this.swapBiome(Biome.FOREST, Biome.PLAINS, 0);
        this.swapBiome(Biome.OCEAN, Biome.PLAINS, 0);

        this.swapBiome(Biome.SWAMPLAND, Biome.PLAINS, 0);
        this.swapBiome(Biome.TAIGA, Biome.PLAINS, 0);
        this.swapBiome(Biome.RIVER, Biome.PLAINS, 0);
        this.swapBiome(Biome.EXTREME_HILLS, Biome.PLAINS, 0);
        this.swapBiome(Biome.FROZEN_OCEAN, Biome.PLAINS, 0);
        this.swapBiome(Biome.FROZEN_RIVER, Biome.PLAINS, 0);
        this.swapBiome(Biome.ICE_PLAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.ICE_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.MUSHROOM_ISLAND, Biome.PLAINS, 0);
        this.swapBiome(Biome.MUSHROOM_SHORE, Biome.PLAINS, 0);
        this.swapBiome(Biome.BEACH, Biome.PLAINS, 0);
        this.swapBiome(Biome.DESERT_HILLS, Biome.PLAINS, 0);
        this.swapBiome(Biome.FOREST_HILLS, Biome.PLAINS, 0);
        this.swapBiome(Biome.TAIGA_HILLS, Biome.PLAINS, 0);
        this.swapBiome(Biome.SMALL_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.JUNGLE, Biome.PLAINS, 0);
        this.swapBiome(Biome.JUNGLE_HILLS, Biome.PLAINS, 0);
        this.swapBiome(Biome.JUNGLE_EDGE, Biome.PLAINS, 0);
        this.swapBiome(Biome.DEEP_OCEAN, Biome.PLAINS, 0);
        this.swapBiome(Biome.STONE_BEACH, Biome.PLAINS, 0);
        this.swapBiome(Biome.COLD_BEACH, Biome.PLAINS, 0);
        this.swapBiome(Biome.BIRCH_FOREST, Biome.PLAINS, 0);
        this.swapBiome(Biome.BIRCH_FOREST_HILLS, Biome.PLAINS, 0);
        this.swapBiome(Biome.ROOFED_FOREST, Biome.PLAINS, 0);
        this.swapBiome(Biome.COLD_TAIGA, Biome.PLAINS, 0);

        this.swapBiome(Biome.COLD_TAIGA_HILLS, Biome.PLAINS, 0);
        this.swapBiome(Biome.MEGA_TAIGA, Biome.PLAINS, 0);
        this.swapBiome(Biome.MEGA_TAIGA_HILLS, Biome.PLAINS, 0);
        this.swapBiome(Biome.EXTREME_HILLS_PLUS, Biome.PLAINS, 0);
//        this.swapBiome(Biome.SAVANNA, Biome.PLAINS, 0);
        this.swapBiome(Biome.SAVANNA_PLATEAU, Biome.PLAINS, 0);
        this.swapBiome(Biome.MESA, Biome.PLAINS, 0);
        this.swapBiome(Biome.MESA_PLATEAU_FOREST, Biome.PLAINS, 0);
        this.swapBiome(Biome.MESA_PLATEAU, Biome.PLAINS, 0);
        this.swapBiome(Biome.SUNFLOWER_PLAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.DESERT_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.FLOWER_FOREST, Biome.PLAINS, 0);
        this.swapBiome(Biome.TAIGA_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.SWAMPLAND_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.ICE_PLAINS_SPIKES, Biome.PLAINS, 0);

        this.swapBiome(Biome.JUNGLE_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.JUNGLE_EDGE_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.COLD_TAIGA_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.SAVANNA_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.SAVANNA_PLATEAU_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.MESA_BRYCE, Biome.PLAINS, 0);
        this.swapBiome(Biome.MESA_PLATEAU_FOREST_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.MESA_PLATEAU_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.BIRCH_FOREST_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.BIRCH_FOREST_HILLS_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.ROOFED_FOREST_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.MEGA_SPRUCE_TAIGA, Biome.PLAINS, 0);
        this.swapBiome(Biome.EXTREME_HILLS_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.EXTREME_HILLS_PLUS_MOUNTAINS, Biome.PLAINS, 0);
        this.swapBiome(Biome.MEGA_SPRUCE_TAIGA_HILLS, Biome.PLAINS, 0);
    }

    private void swapBiome(Biome from, Biome to, int offset) {
        BiomeBase.getBiomes()[CraftBlock.biomeToBiomeBase(from).id + offset]
                = CraftBlock.biomeToBiomeBase(to);
    }
}
