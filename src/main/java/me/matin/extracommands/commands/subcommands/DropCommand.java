package me.matin.extracommands.commands.subcommands;

import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.th0rgal.oraxen.api.OraxenItems;
import me.matin.extracommands.Hooks;
import me.matin.extracommands.commands.SubCommand;
import me.matin.extracommands.methods.Extras;
import me.matin.extracommands.methods.itemdropper.ItemDropper;
import me.matin.extracommands.methods.itemdropper.ItemFinder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DropCommand extends SubCommand {
    @Override
    public String getName() {
        return "dropitem";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"drop"};
    }

    @Override
    public String getDescription() {
        return "Drop item(s) at a location with some extra features.";
    }

    @Override
    public String getSyntax() {
        return "/extracommands drop [item] [amount] [x] [y] [z] [world] (face) (nbt)";
    }

    @Override
    public boolean getRequirements(CommandSender sender) {
        return sender.hasPermission("extracommands.drop");
    }

    BlockFace face = BlockFace.SELF;

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length >= 7) {
            String item = args[1];
            int amount = Extras.parseInt(args[2], -1);
            int x = Extras.parseInt(args[3], 0);
            int y = Extras.parseInt(args[4], 0);
            int z = Extras.parseInt(args[5], 0);
            World world = Bukkit.getWorld(args[6]);
            face = getBlockFace(args);
            ItemStack itemStack = ItemFinder.getItem(item) != null ? ItemFinder.getItem(item) : new ItemStack(Material.AIR);
            if (itemStack != null && world != null && amount != -1) {
                int skipNumber = 8;
                if (args.length >= 8 && args[7].startsWith("{")) skipNumber = 7;
                ItemStack nbtItem = getNBTItem(skipNumber, args, itemStack);
                nbtItem.setAmount(amount);
                ItemDropper.drop(nbtItem, x, y, z, world, face);
            }
        }
    }

    private static ItemStack getNBTItem(int argsToSkip, String[] args, ItemStack item) {
        if (!Hooks.hasPlugin("NBTAPI")) return item;
        if (item == null || item.isEmpty() || item.getType().isAir()) return item;
        String nbtData = Arrays.stream(args).skip(argsToSkip).collect(Collectors.joining(" ")).strip();
        if (nbtData.startsWith("{") && nbtData.endsWith("}")) {
            NBTItem nbtItem = new NBTItem(item);
            nbtItem.mergeCompound(new NBTContainer(nbtData));
            return nbtItem.getItem();
        } else {
            return item;
        }
    }

    private static BlockFace getBlockFace(String[] args) {
        BlockFace face = BlockFace.SELF;
        if (args.length >= 8) {
            switch (args[7].toLowerCase()) {
                case "n", "north" -> face = BlockFace.NORTH;
                case "s", "south" -> face = BlockFace.SOUTH;
                case "e", "east" -> face = BlockFace.EAST;
                case "w", "west" -> face = BlockFace.WEST;
                case "u", "up" -> face = BlockFace.UP;
                case "d", "down" -> face = BlockFace.DOWN;
            }
        }
        return face;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> result = null;
        if (args.length == 2) {
            List<String> items = new ArrayList<>();
            Stream<Material> materials = Arrays.stream(Material.values()).filter(Material::isItem).filter(mat -> !mat.isAir() || mat.isLegacy());
            materials.forEach(str -> items.add(str.name().toLowerCase()));
            if (Hooks.hasPlugin("Oraxen")) {
                Stream<String> oraxenItems = OraxenItems.getNames().stream();
                oraxenItems.forEach(str -> items.add("oxn:" + str.toLowerCase()));
            }
            if (Hooks.hasPlugin("ExecutableItems")) {
                OfflinePlayer[] players = Bukkit.getOfflinePlayers();
                for (OfflinePlayer player : players) {
                    if (player.getName() != null) {
                        Stream<String> eiItems = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItemIdsList().stream();
                        eiItems.forEach(str -> items.add("ei:" + str.toLowerCase() + ":" + player.getName().toLowerCase()));
                    }
                }
            }
            if (Hooks.hasPlugin("MythicMobs")) {
                Stream<String> mythicItems = MythicBukkit.inst().getItemManager().getItemNames().stream();
                mythicItems.forEach(str -> items.add("mm:" + str.toLowerCase()));
            }
            result = items;
        } else if (args.length == 3) {
            List<String> amount = new ArrayList<>();
            amount.add("amount");
            result = amount;
        } else if (args.length == 4) {
            List<String> location = new ArrayList<>();
            if (sender instanceof Player p) {
                result = Extras.getLookingBlockTC(Extras.BlockLocationType.X, p, location);
            } else {
                location.add("X");
                result = location;
            }
        } else if (args.length == 5) {
            List<String> location = new ArrayList<>();
            if (sender instanceof Player p) {
                result = Extras.getLookingBlockTC(Extras.BlockLocationType.Y, p, location);
            } else {
                location.add("Y");
                result = location;
            }
        } else if (args.length == 6) {
            List<String> location = new ArrayList<>();
            if (sender instanceof Player p) {
                result = Extras.getLookingBlockTC(Extras.BlockLocationType.Z, p, location);
            } else {
                location.add("Z");
                result = location;
            }
        } else if (args.length == 7) {
            List<String> worlds = new ArrayList<>();
            for (World world : Bukkit.getWorlds()) {
                worlds.add(world.getName());
            }
            result = worlds;
        } else if (args.length == 8) {
            List<String> faces = new ArrayList<>();
            faces.add(0, "north");
            faces.add(1, "south");
            faces.add(2, "east");
            faces.add(3, "west");
            faces.add(4, "up");
            faces.add(5, "down");
            faces.add(6, "{nbt}");
            result = faces;
        } else if (args.length == 9) {
            List<String> nbt = new ArrayList<>();
            if (!args[7].startsWith("{")) {
                nbt.add("{nbt}");
            }
            result = nbt;
        }
        return result;
    }
}
