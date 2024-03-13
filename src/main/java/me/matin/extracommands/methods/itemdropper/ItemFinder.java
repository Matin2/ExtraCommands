package me.matin.extracommands.methods.itemdropper;


import com.ssomar.score.api.executableitems.ExecutableItemsAPI;
import com.ssomar.score.api.executableitems.config.ExecutableItemInterface;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.items.ItemBuilder;
import me.matin.extracommands.Hooks;
import me.matin.extracommands.methods.Extras;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ItemFinder {

    public static ItemStack getItem(String item) {
        ItemStack result;
        String[] id = item.split(":", 2);
        if (id.length < 1) return null;
        if (id[0].equalsIgnoreCase("oxn")) {
            result = id.length == 2 ? getOXNItem(id[1]) : null;
        } else if (id[0].equalsIgnoreCase("ei")) {
            result = id.length == 2 ? getEIItem(id[1]) : null;
        } else if (id[0].equalsIgnoreCase("mm")) {
            result = id.length == 2 ? getMMItem(id[1]) : null;
        } else {
            result = getMCItem(item);
        }
        return result != null ? result : new ItemStack(Material.AIR);
    }
    public static ItemStack getMCItem(String item) {
        Material mat;
        try {
            mat = Material.valueOf(item.toUpperCase());
        }catch (IllegalArgumentException e) {
            mat = Material.AIR;
        }
        return new ItemStack(mat, 1);
    }
    public static ItemStack getOXNItem(String id) {
        if (!Hooks.hasPlugin("Oraxen")) return null;
        Set<String> ids = OraxenItems.getNames();
        for (String i : ids) {
            if (i.equalsIgnoreCase(id)) id = i;
        }
        if (!OraxenItems.exists(id)) return null;
        ItemBuilder item = OraxenItems.getItemById(id);
        return item != null ? item.build(): null;
    }

    public static ItemStack getEIItem(String values) {
        if (!Hooks.hasPlugin("ExecutableItems")) return null;
        String[] args = values.split(":", 3);
        if (args.length < 2) return null;
        String id = args[0];
        List<String> ids = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItemIdsList();
        for (String i : ids) {
            if (i.equalsIgnoreCase(id)) id = i;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) return null;
        Integer usage;
        if (args.length == 3) usage = Extras.parseInt(args[2]);
        else usage = null;
        if (!ExecutableItemsAPI.getExecutableItemsManager().isValidID(id)) return null;
        Optional<ExecutableItemInterface> executableItem = ExecutableItemsAPI.getExecutableItemsManager().getExecutableItem(id);
        ItemStack item = null;
        if (executableItem.isPresent()) {
            item = executableItem.get().buildItem(1, usage == null ? Optional.empty() : Optional.of(usage), Optional.of(player));
        }
        return item;
    }

    public static ItemStack getMMItem(String name) {
        if (!Hooks.hasPlugin("MythicMobs")) return null;
        Collection<String> names = MythicBukkit.inst().getItemManager().getItemNames();
        for (String n : names) {
            if (n.equalsIgnoreCase(name)) name = n;
        }
        ItemStack item = MythicBukkit.inst().getItemManager().getItemStack(name);
        return MythicBukkit.inst().getItemManager().isMythicItem(item) ? item : null;
    }
}
