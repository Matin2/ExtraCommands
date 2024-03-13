package me.matin.extracommands.methods.itemmodifier;

import me.matin.extracommands.methods.Extras;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotFinder {

    public static void getSlot(@NotNull Player p, @NotNull String input, ItemModifier.ModifyType modType, ItemModifier.Modification mod, int modAmount) {
        String[] slots = input.split(",");
        if (slots.length > 1) {
            for (String slotString : slots) {
                int slot = getPresetSlot(p, slotString);
                ItemStack item = p.getInventory().getItem(slot);
                if (item != null && modType != null && slot != -1) {
                    ItemModifier.modify(mod, modAmount, p, slot, modType);
                }
            }
        } else {
            int slot = getPresetSlot(p, input);
            ItemStack item = p.getInventory().getItem(slot);
            if (item != null && modType != null && slot != -1) {
                ItemModifier.modify(mod, modAmount, p, slot, modType);
            }
        }
    }

    private static int getPresetSlot(@NotNull Player p, @NotNull String slot) {
        int result = Extras.parseInt(slot, -1);
        switch (slot) {
            case "mainhand", "main" -> result = p.getInventory().getHeldItemSlot();
            case "offhand", "off" -> result = 40;
            case "helmet", "helm" -> result = 39;
            case "chestplate", "chest" -> result = 38;
            case "leggings", "leggs" -> result = 37;
            case "boots" -> result = 36;
        }
        return result;
    }
}
