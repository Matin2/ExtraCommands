package me.matin.extracommands.commands;

import me.matin.extracommands.commands.subcommands.AttackCommand;
import me.matin.extracommands.commands.subcommands.BlockCommand;
import me.matin.extracommands.commands.subcommands.DropCommand;
import me.matin.extracommands.commands.subcommands.ModifyCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public CommandManager() {
        subCommands.add(new DropCommand());
        subCommands.add(new ModifyCommand());
        subCommands.add(new AttackCommand());
        subCommands.add(new BlockCommand());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            for (int i = 0 ; i < getSubCommands().size(); i++) {
                if (isSubCommand(args[0], i) && hasRequirements(sender, i)) {
                    getSubCommands().get(i).perform(sender, args);
                }
            }
        }
        return true;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }

    private boolean isSubCommand(String alias, int i) {
        if (getSubCommands().get(i).getName().equalsIgnoreCase(alias)) return true;
        String[] aliases = getSubCommands().get(i).getAliases();
        for (String s : aliases) {
            if (s.equalsIgnoreCase(alias)) return true;
        }
        return false;
    }

    private boolean hasRequirements(CommandSender sender, int i) {
        return getSubCommands().get(i).getRequirements(sender);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> result = null;
        if (args.length == 1) {
            List<String> allSubCommands = new ArrayList<>();
            for (int i = 0 ; i < getSubCommands().size(); i++) {
                if (hasRequirements(sender, i)) allSubCommands.add(getSubCommands().get(i).getName());
                allSubCommands.addAll(List.of(getSubCommands().get(i).getAliases()));
            }
            result = allSubCommands;
        } else if (args.length >= 2) {
            for (int i = 0 ; i < getSubCommands().size(); i++) {
                if (isSubCommand(args[0], i) && hasRequirements(sender, i)) {
                    result = getSubCommands().get(i).tabComplete(sender, args);
                }
            }
        }
        for (int i = 2 ; i <= args.length; i++) {
            if (args.length == i && args[i - 2].isEmpty()) {
                result = new ArrayList<>();
                break;
            }
        }
        if (result == null) result = new ArrayList<>();
        return result;
    }
}
