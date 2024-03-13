package me.matin.extracommands.commands.subcommands;

import io.th0rgal.oraxen.api.OraxenBlocks;
import me.matin.extracommands.Hooks;
import me.matin.extracommands.commands.SubCommand;
import me.matin.extracommands.methods.Extras;
import me.matin.extracommands.methods.blockchanger.BlockChanger;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class BlockCommand extends SubCommand {
    @Override
    public String getName() {
        return "changeblock";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"changeb", "cblock"};
    }

    @Override
    public String getDescription() {
        return "Change a block at a location to another block with directions support.";
    }

    @Override
    public String getSyntax() {
        return "/extracommands block [block] [x] [y] [z] [world]";
    }

    @Override
    public boolean getRequirements(CommandSender sender) {
        return sender.hasPermission("extracommands.block");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length >= 6) {
            String blockText = args[1];
            int x = Extras.parseInt(args[2], 0);
            int y = Extras.parseInt(args[3], 0);
            int z = Extras.parseInt(args[4], 0);
            World world = Bukkit.getWorld(args[5]);
            Block oldBlock = world != null ? world.getBlockAt(x, y ,z) : null;
            BlockData newBlock = oldBlock != null ? BlockChanger.getNewBlockData(blockText, oldBlock) : null;
            if (newBlock != null && newBlock != oldBlock.getBlockData()) {
                world.setBlockData(x, y ,z, newBlock);
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> result = null;
        if (args.length == 2) {
            List<String> items = new ArrayList<>();
            Stream<Material> materials = Arrays.stream(Material.values()).filter(Material::isBlock);
            materials.forEach(str -> items.add(str.name().toLowerCase()));
            if (Hooks.hasPlugin("Oraxen")) {
                Stream<String> oraxenBlocks = OraxenBlocks.getBlockIDs().stream();
                oraxenBlocks.forEach(str -> items.add("oxn:" + str));
            }
            result = items;
        } else if (args.length == 3) {
            List<String> location = new ArrayList<>();
            if (sender instanceof Player p) {
                result = Extras.getLookingBlockTC(Extras.BlockLocationType.X, p, location);
            } else {
                location.add("X");
                result = location;
            }
        } else if (args.length == 4) {
            List<String> location = new ArrayList<>();
            if (sender instanceof Player p) {
                result = Extras.getLookingBlockTC(Extras.BlockLocationType.Y, p, location);
            } else {
                location.add("Y");
                result = location;
            }
        } else if (args.length == 5) {
            List<String> location = new ArrayList<>();
            if (sender instanceof Player p) {
                result = Extras.getLookingBlockTC(Extras.BlockLocationType.Z, p, location);
            } else {
                location.add("Z");
                result = location;
            }
        } else if (args.length == 6) {
            List<String> worlds = new ArrayList<>();
            for (World world : Bukkit.getWorlds()) {
                worlds.add(world.getName());
            }
            result = worlds;
        }
        return result;
    }
}
