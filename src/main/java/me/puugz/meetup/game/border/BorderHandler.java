package me.puugz.meetup.game.border;

import lombok.Getter;
import lombok.Setter;
import me.puugz.meetup.UHCMeetup;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.List;

/**
 * @author puugz
 * @since May 19, 2023
 */
public class BorderHandler {

    /**
     * TODO: Implement glass border for 1.7
     */

    @Getter
    @Setter
    private int borderSize = 125;

    private static final int WALL_HEIGHT = 5;
    private static final List<Material> IGNORED_WALL_TYPES =
            Arrays.asList(
                    Material.WATER, Material.STATIONARY_WATER, Material.LAVA,
                    Material.STATIONARY_LAVA, Material.LEAVES, Material.LEAVES_2,
                    Material.LONG_GRASS, Material.DOUBLE_PLANT, Material.YELLOW_FLOWER
            );

    public void shrinkBorder() {
        final World world = UHCMeetup.getInstance()
                .getMapHandler().getMapWorld();

        final int newBorderSize = borderSize > 25 ? borderSize - 25 : borderSize - 15;
        this.borderSize = newBorderSize;

        world.getWorldBorder().setSize(newBorderSize * 2);
        this.generateBedrockWall(world);
    }

    public void generateBedrockWall(World world) {
        final int startX = -borderSize - 1;
        final int endX = borderSize;
        final int startZ = -borderSize - 1;
        final int endZ = borderSize;

        // Generate walls on X borders
        for (int x = startX; x <= endX; x++) {
            this.generateWallAt(world, x, startZ);
            this.generateWallAt(world, x, endZ);
        }

        // Generate walls on Z borders (excluding corners)
        for (int z = startZ + 1; z < endZ; z++) {
            this.generateWallAt(world, startX, z);
            this.generateWallAt(world, endX, z);
        }
    }

    private void generateWallAt(World world, int x, int z) {
        final int y = world.getHighestBlockYAt(x, z);

        for (int i = y + WALL_HEIGHT; i > 50; i--) {
            final Block block = world.getBlockAt(x, i, z);
            if (IGNORED_WALL_TYPES.contains(block.getType()) || block.isEmpty())
                block.setType(Material.BEDROCK);
        }
    }
}
