package me.matin.extracommands.methods.blockchanger;

import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanic;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.NoteBlockMechanicFactory;
import io.th0rgal.oraxen.mechanics.provided.gameplay.noteblock.directional.DirectionalBlock;
import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;

import java.util.Objects;

public class BlockChanger {

    public static BlockData getNewBlockData(String newBlock, Block oldBlock) {
        String[] id = newBlock.split(":", 2);
        if (id[0].equalsIgnoreCase("oxn")) {
            BlockData oxnBlock = id.length == 2 ? getNewOXNBlockData(id[1], oldBlock) : null;
            if (oxnBlock != null) return oxnBlock;
        }
        return getNewMCBlockData(newBlock, oldBlock);
    }

    public static BlockData getNewMCBlockData(String newBlock, Block oldBlock) {
        Material mat;
        try {
            mat = Material.valueOf(newBlock.toUpperCase());
        } catch (IllegalArgumentException e) {
            mat = oldBlock.getType();
        }
        if (!mat.isBlock()) mat = oldBlock.getType();
        BlockData oldData = oldBlock.getBlockData();
        oldBlock.setType(mat);
        BlockData newData = oldBlock.getBlockData();
        if (DirectionIdentifier.isDirectional(oldData)) {
            if (Objects.requireNonNull(DirectionIdentifier.getDirectionType(oldData)) == DirectionIdentifier.DirectionType.ORIENTABLE) {
                if (newData instanceof Orientable orientable) {
                    switch (DirectionIdentifier.getDirection(oldData)) {
                        case X -> orientable.setAxis(Axis.X);
                        case Y -> orientable.setAxis(Axis.Y);
                        case Z -> orientable.setAxis(Axis.Z);
                    }
                }
            } else if (Objects.requireNonNull(DirectionIdentifier.getDirectionType(oldData)) == DirectionIdentifier.DirectionType.DIRECTIONAL){
                if (newData instanceof Directional directional) {
                    switch (DirectionIdentifier.getDirection(oldData)) {
                        case NORTH -> directional.setFacing(BlockFace.NORTH);
                        case SOUTH -> directional.setFacing(BlockFace.SOUTH);
                        case EAST -> directional.setFacing(BlockFace.EAST);
                        case WEST -> directional.setFacing(BlockFace.WEST);
                        case UP -> directional.setFacing(BlockFace.UP);
                        case DOWN -> directional.setFacing(BlockFace.DOWN);
                    }
                }
            } else {
                if (newData instanceof Directional directional) {
                    switch (DirectionIdentifier.getDirection(oldData)) {
                        case NORTH -> directional.setFacing(BlockFace.NORTH);
                        case SOUTH -> directional.setFacing(BlockFace.SOUTH);
                        case EAST -> directional.setFacing(BlockFace.EAST);
                        case WEST -> directional.setFacing(BlockFace.WEST);
                    }
                }
            }
        }
        return newData;
    }

    public static BlockData getNewOXNBlockData(String newBlock, Block oldBlock) {
        if (!OraxenBlocks.isOraxenBlock(newBlock) || !OraxenBlocks.isOraxenNoteBlock(newBlock)) return oldBlock.getBlockData();
        NoteBlockMechanic noteBlockMechanic = OraxenBlocks.getNoteBlockMechanic(newBlock);
        if (DirectionIdentifier.isDirectional(oldBlock.getBlockData()) && noteBlockMechanic != null && noteBlockMechanic.isDirectional() && noteBlockMechanic.getDirectional().isParentBlock()) {
            DirectionalBlock block = noteBlockMechanic.getDirectional();
            if (block.isLog() && DirectionIdentifier.getDirectionType(oldBlock.getBlockData()) == DirectionIdentifier.DirectionType.ORIENTABLE) {
                switch (DirectionIdentifier.getDirection(oldBlock.getBlockData())) {
                    case X -> newBlock = block.getZBlock();
                    case Z -> newBlock = block.getXBlock();
                    default -> newBlock = block.getYBlock();
                }
            } else if (block.isFurnace() && DirectionIdentifier.getDirectionType(oldBlock.getBlockData()) == DirectionIdentifier.DirectionType.DIRECTIONAL) {
                switch (DirectionIdentifier.getDirection(oldBlock.getBlockData())) {
                    case SOUTH -> newBlock = block.getSouthBlock();
                    case EAST -> newBlock = block.getEastBlock();
                    case WEST -> newBlock = block.getWestBlock();
                    default -> newBlock = block.getNorthBlock();
                }
            } else if (block.isDropper() && DirectionIdentifier.getDirectionType(oldBlock.getBlockData()) == DirectionIdentifier.DirectionType.DIRECTIONAL) {
                switch (DirectionIdentifier.getDirection(oldBlock.getBlockData())) {
                    case SOUTH -> newBlock = block.getSouthBlock();
                    case EAST -> newBlock = block.getEastBlock();
                    case WEST -> newBlock = block.getWestBlock();
                    case UP -> newBlock = block.getUpBlock();
                    case DOWN -> newBlock = block.getDownBlock();
                    default -> newBlock = block.getNorthBlock();
                }
            }
        }
        NoteBlockMechanicFactory.setBlockModel(oldBlock, newBlock);
        return oldBlock.getBlockData();
    }
}
