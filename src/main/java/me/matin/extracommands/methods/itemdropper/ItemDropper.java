package me.matin.extracommands.methods.itemdropper;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemDropper {
    public static void drop(@NotNull ItemStack item, int x, int y, int z, @NotNull World world, @NotNull BlockFace blockFace) {
        if (item.getType() != Material.AIR && item.getAmount() != 0) {
            Block block = world.getBlockAt(x, y, z);
            world.dropItemNaturally(block.getRelative(blockFace).getLocation(), item);
        }
    }
}
