package me.matin.extracommands.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class SubCommand {

    public abstract String getName();
    public abstract String[] getAliases();
    public abstract String getDescription();
    public abstract String getSyntax();
    public abstract boolean getRequirements(CommandSender sender);
    public abstract void perform(CommandSender sender, String[] args);
    public abstract List<String> tabComplete(CommandSender sender, String[] args);
}
