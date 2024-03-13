package me.matin.extracommands.methods.blockchanger;

import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.mechanics.Mechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.directional.DirectionalBlock;
import me.matin.extracommands.Hooks;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;

import java.util.Objects;

public class DirectionIdentifier {

    // Check if the block data is about a directional block.
    public static boolean isDirectional(BlockData blockData) {
        if (!Hooks.hasPlugin("Oraxen")) return isMCDirectional(blockData);
        Mechanic mechanic = OraxenBlocks.getOraxenBlock(blockData);
        if (mechanic != null) {
            String id = mechanic.getItemID();
            if (OraxenBlocks.isOraxenBlock(id)) return isOXNDirectional(id);
        }
        return isMCDirectional(blockData);
    }

    private static boolean isMCDirectional(BlockData blockData) {
        return blockData instanceof Directional || blockData instanceof Orientable;
    }

    private static boolean isOXNDirectional(String itemID) {
        if (!OraxenBlocks.isOraxenNoteBlock(itemID)) return false;
        NoteBlockMechanic noteBlock = OraxenBlocks.getNoteBlockMechanic(itemID);
        return noteBlock == null || noteBlock.isDirectional();
    }

    // Get the block data directional type.
    public enum DirectionType {
        ORIENTABLE,
        DIRECTIONAL,
    }

    public static DirectionType getDirectionType(BlockData blockData) {
        if (!Hooks.hasPlugin("Oraxen")) return getMCDirectionType(blockData);
        Mechanic mechanic = OraxenBlocks.getOraxenBlock(blockData);
        if (mechanic != null) {
            String id = mechanic.getItemID();
            if (OraxenBlocks.isOraxenBlock(id)) return getOXNDirectionType(id);
        }
        return getMCDirectionType(blockData);
    }

    private static DirectionType getMCDirectionType(BlockData blockData) {
        if (blockData instanceof Orientable) return DirectionType.ORIENTABLE;
        return DirectionType.DIRECTIONAL;
    }

    private static DirectionType getOXNDirectionType(String itemID) {
        DirectionalBlock block = Objects.requireNonNull(OraxenBlocks.getNoteBlockMechanic(itemID)).getDirectional();
        if (!block.isParentBlock()) block = Objects.requireNonNull(OraxenBlocks.getNoteBlockMechanic(block.getParentBlock())).getDirectional();
        if (block.isLog()) {
            return DirectionType.ORIENTABLE;
        } else {
            return DirectionType.DIRECTIONAL;
        }
    }

    //Finally get the block data direction.
    public enum Direction {
        X,
        Y,
        Z,
        NORTH,
        SOUTH,
        EAST,
        WEST,
        UP,
        DOWN
    }

    public static Direction getDirection(BlockData blockData) {
        if (Objects.requireNonNull(getDirectionType(blockData)) != DirectionType.ORIENTABLE) {
            return getDirectionalDirection(blockData);
        }
        return getOrientableDirection(blockData);
    }

    private static Direction getOrientableDirection(BlockData blockData) {
        if (!Hooks.hasPlugin("Oraxen")) return getMCOrientableDirection(blockData);
        Mechanic mechanic = OraxenBlocks.getOraxenBlock(blockData);
        if (mechanic != null) {
            String id = mechanic.getItemID();
            if (OraxenBlocks.isOraxenBlock(id)) return getOXNOrientableDirection(id);
        }
        return getMCOrientableDirection(blockData);
    }

    private static Direction getMCOrientableDirection(BlockData blockData) {
        if (blockData instanceof Orientable orientable) {
            switch (orientable.getAxis()) {
                case X -> {
                    return Direction.X;
                }
                case Y -> {
                    return Direction.Y;
                }
                case Z -> {
                    return Direction.Z;
                }
            }
        }
        return null;
    }

    private static Direction getOXNOrientableDirection(String itemID) {
        DirectionalBlock block = Objects.requireNonNull(OraxenBlocks.getNoteBlockMechanic(itemID)).getDirectional();
        if (!block.isParentBlock()) block = Objects.requireNonNull(OraxenBlocks.getNoteBlockMechanic(block.getParentBlock())).getDirectional();
        if (block.isLog()) {
            if (block.getZBlock().equals(itemID)) return Direction.X;
            else if (block.getYBlock().equals(itemID)) return Direction.Y;
            else if (block.getXBlock().equals(itemID)) return Direction.Z;
        }
        return null;
    }

    private static Direction getDirectionalDirection(BlockData blockData) {
        if (!Hooks.hasPlugin("Oraxen")) return getMCDirectionalDirection(blockData);
        Mechanic mechanic = OraxenBlocks.getOraxenBlock(blockData);
        if (mechanic != null) {
            String id = mechanic.getItemID();
            if (OraxenBlocks.isOraxenBlock(id)) return getOXNDirectionalDirection(id);
        }
        return getMCDirectionalDirection(blockData);
    }

    private static Direction getMCDirectionalDirection(BlockData blockData) {
        if (blockData instanceof Directional directional) {
            switch (directional.getFacing()) {
                case NORTH -> {
                    return Direction.NORTH;
                }
                case SOUTH -> {
                    return Direction.SOUTH;
                }
                case EAST -> {
                    return Direction.EAST;
                }
                case WEST -> {
                    return Direction.WEST;
                }
                case UP -> {
                    return Direction.UP;
                }
                case DOWN -> {
                    return Direction.DOWN;
                }
            }
        }
        return null;
    }

    private static Direction getOXNDirectionalDirection(String itemID) {
        DirectionalBlock block = Objects.requireNonNull(OraxenBlocks.getNoteBlockMechanic(itemID)).getDirectional();
        if (!block.isParentBlock()) block = Objects.requireNonNull(OraxenBlocks.getNoteBlockMechanic(block.getParentBlock())).getDirectional();
        if (!block.isLog()) {
            if (block.getNorthBlock().equals(itemID)) return Direction.NORTH;
            else if (block.getSouthBlock().equals(itemID)) return Direction.SOUTH;
            else if (block.getEastBlock().equals(itemID)) return Direction.EAST;
            else if (block.getWestBlock().equals(itemID)) return Direction.WEST;
            else if (block.isDropper() && block.getUpBlock().equals(itemID)) return Direction.UP;
            else if (block.isDropper() && block.getDownBlock().equals(itemID)) return Direction.DOWN;
        }
        return null;
    }
}
