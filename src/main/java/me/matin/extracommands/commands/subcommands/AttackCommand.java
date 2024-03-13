package me.matin.extracommands.commands.subcommands;

import me.matin.extracommands.Hooks;
import me.matin.extracommands.commands.SubCommand;
import me.matin.extracommands.methods.attackanimator.AttackAnimator;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AttackCommand extends SubCommand {
    @Override
    public String getName() {
        return "attackanimation";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"attack", "attackanim"};
    }

    @Override
    public String getDescription() {
        return "Make a player do attack animation with mainhand/offhand";
    }

    @Override
    public String getSyntax() {
        return "/extracommands attack [hand] [player]";
    }

    @Override
    public boolean getRequirements(CommandSender sender) {
        boolean permission = sender.hasPermission("extracommands.attack");
        boolean hook = Hooks.hasPlugin("ProtocolLib");
        return hook && permission;
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        AttackAnimator.HandType handType = null;
        switch (args[1].toLowerCase()) {
            case "mainhand", "main" -> handType = AttackAnimator.HandType.MAINHAND;
            case "offhand", "off" -> handType = AttackAnimator.HandType.OFFHAND;
        }
        Player target = Bukkit.getPlayer(args[2]);
        if (target != null && handType != null) {
            AttackAnimator.play(target, handType);
        }
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        List<String> result = null;
        if (args.length == 2) {
            List<String> types = new ArrayList<>();
            types.add(0, "mainhand");
            types.add(1, "offhand");
            result = types;
        } else if (args.length == 3) {
            List<String> playerNames = new ArrayList<>();
            Collection<? extends Player> players = Bukkit.getOnlinePlayers();
            for (Player p : players) {
                playerNames.add(p.getName());
            }
            result = playerNames;
        }
        return result;
    }
}
