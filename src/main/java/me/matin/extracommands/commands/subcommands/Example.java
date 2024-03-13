package me.matin.extracommands.commands.subcommands;

import me.matin.extracommands.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class Example extends SubCommand {
    @Override
    public String getName() {
        return "example";
    }

    @Override
    public String[] getAliases() {
        return new String[]{""};
    }

    @Override
    public String getDescription() {
        return "Example command";
    }

    @Override
    public String getSyntax() {
        return "/prank example <playername>";
    }

    @Override
    public boolean getRequirements(CommandSender sender) {
        return true;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
