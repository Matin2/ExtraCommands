package me.matin.extracommands.methods.itemmodifier;


import me.matin.extracommands.ExtraCommands;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class ItemModifier {

    public enum Modification {
        SET,
        ADD,
        TAKE
    }

    public enum ModifyType {
        DURABILITY,
        AMOUNT,
    }

    public static void modify(Modification modification, int amount, Player player, int slot, ModifyType type)  {
        switch (type) {
            case AMOUNT -> modifyAmount(modification, amount, player, slot);
            case DURABILITY -> modifyDurability(modification, amount, player, slot);
        }
    }

    public static void modifyAmount(Modification modification, int amount, Player player, int slot)  {
        int mod = 0;
        ItemStack item = player.getInventory().getItem(slot);
        int itemAmount = item == null || item.getType() == Material.AIR ? 0 : item.getAmount();
        int itemMaxAmount = item == null || item.getType() == Material.AIR ? 0 : item.getMaxStackSize();
        switch (modification) {
            case SET -> mod = amount;
            case ADD -> mod = itemAmount + amount;
            case TAKE -> mod = itemAmount - amount;
        }
        if (mod > itemMaxAmount) mod = itemMaxAmount;
        if (itemAmount != 0) {
            item.setAmount(mod);
        }
    }

    public static void modifyDurability(Modification modification, int amount, Player player, int slot)  {
        ItemStack item = player.getInventory().getItem(slot);
        if (item != null && item.getType() != Material.AIR && !item.getItemMeta().isUnbreakable() && item.getItemMeta() instanceof Damageable damageable) {
            int itemMaxDurability = item.getType().getMaxDurability();
            int mod = 0;
            int itemDamage = damageable.hasDamage() ? damageable.getDamage() : 0;
            switch (modification) {
                case SET -> mod = itemMaxDurability - amount;
                case ADD -> mod = itemDamage - amount;
                case TAKE -> mod = itemDamage + amount;
            }
            if (mod != 0 && item.getType() != Material.AIR) {
                damageable.setDamage(mod);
                Bukkit.getScheduler().runTaskAsynchronously(ExtraCommands.plugin, () -> item.setItemMeta(damageable));
            }
        }
    }
}
