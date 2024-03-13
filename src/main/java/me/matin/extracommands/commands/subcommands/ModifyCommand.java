package me.matin.extracommands.commands.subcommands;

import me.matin.extracommands.commands.SubCommand;
import me.matin.extracommands.methods.Extras;
import me.matin.extracommands.methods.itemmodifier.ItemModifier;
import me.matin.extracommands.methods.itemmodifier.SlotFinder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ModifyCommand extends SubCommand {
    @Override
    public String getName() {
        return "modifyitem";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"modify"};
    }

    @Override
    public String getDescription() {
        return "Modify durability/amount of an item in a player's inventory.";
    }

    @Override
    public String getSyntax() {
        return "/extracommands modify [type] [player] [slot] [modification]";
    }

    @Override
    public boolean getRequirements(CommandSender sender) {
        return sender.hasPermission("extracommands.modify");
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length >= 5) {
            ItemModifier.ModifyType modType = null;
            switch (args[1].toLowerCase()) {
                case "durability", "dur" -> modType = ItemModifier.ModifyType.DURABILITY;
                case "amount", "amt" -> modType = ItemModifier.ModifyType.AMOUNT;
            }
            Player target = Bukkit.getPlayer(args[2]);
            ItemModifier.Modification mod;
            int modAmount = Extras.parseInt(args[4].replace("+", "").replace("-", ""), 0);
            if (args[4].startsWith("+")) mod = ItemModifier.Modification.ADD;
            else if (args[4].startsWith("-")) mod = ItemModifier.Modification.TAKE;
            else mod = ItemModifier.Modification.SET;
            if (target != null) {
                SlotFinder.getSlot(target, args[3], modType, mod, modAmount);
            }
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> result = null;
        if (args.length == 2) {
            List<String> types = new ArrayList<>();
            types.add(0, "amount");
            types.add(1, "durability");
            result = types;
        } else if (args.length == 3) {
            List<String> playerNames = new ArrayList<>();
            Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (Player p : players) {
                playerNames.add(p.getName());
            }
            result = playerNames;
        } else if (args.length == 4) {
            List<String> slot = new ArrayList<>();
            slot.add("slot");
            slot.add("slot1,slot2,...");
            slot.add("mainhand");
            slot.add("offhand");
            slot.add("helmet");
            slot.add("chestplate");
            slot.add("leggings");
            slot.add("boots");
            result = slot;
        } else if (args.length == 5) {
            List<String> modification = new ArrayList<>();
            modification.add("modification");
            result = modification;
        }
        return result;
    }
}
