package me.matin.extracommands.methods;

import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import de.tr7zw.nbtapi.NBTType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import java.util.List;
import java.util.Set;

public class Extras {
    public static int parseInt(String text, int errorInt) {
        int result;
        try {
            result = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            result = errorInt;
        }
        return result;
    }

    public static Integer parseInt(String text) {
        Integer result = null;
        try {
            result = Integer.parseInt(text);
        } catch (NumberFormatException ignored) {}
        return result;
    }

    private static Block lookingBlock(Player p) {
        BlockIterator iter = new BlockIterator(p, 10);
        Block lastBlock = iter.next();
        while (iter.hasNext()) {
            lastBlock = iter.next();
            if (lastBlock.getType() == Material.AIR) {
                continue;
            }
            break;
        }
        return lastBlock;
    }

    public enum BlockLocationType {
        X, Y, Z
    }

    public static List<String> getLookingBlockTC(BlockLocationType type, Player player, List<String> location) {
        Block lastBlock = lookingBlock(player);
        String X = String.valueOf(lastBlock.getX());
        String Y = String.valueOf(lastBlock.getY());
        String Z = String.valueOf(lastBlock.getZ());
        String world = lastBlock.getWorld().getName().toLowerCase();
        switch (type) {
            case X -> {
                location.add(X);
                location.add(X + " " + Y);
                location.add(X + " " + Y  + " " + Z);
                location.add(X + " " + Y  + " " + Z + " " + world);
            }
            case Y -> {
                location.add(Y);
                location.add(Y + " " + Z);
                location.add(Z + " " + world);
            }
            case Z -> {
                location.add(Z);
                location.add(Z + " " + world);
            }
        }
        return location;
    }
}
