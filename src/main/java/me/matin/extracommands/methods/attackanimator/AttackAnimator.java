package me.matin.extracommands.methods.attackanimator;

import me.matin.extracommands.Hooks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;

public class AttackAnimator {

    public enum HandType {
        MAINHAND,
        OFFHAND
    }

    private static Collection<Player> getPlayers() {
        return new HashSet<>(Bukkit.getOnlinePlayers());
    }

    public static void play(Player player, HandType hand_type) {
        if (Hooks.hasPlugin("ProtocolLib")) {
            PacketManager.sendHandMovement(getPlayers(), player, hand_type);
        }
    }

    public static HandType getHandType(String type) {
        if (type.equalsIgnoreCase("offhand")) {
            return HandType.OFFHAND;
        } else if (type.equalsIgnoreCase("mainhand")){
            return HandType.MAINHAND;
        } else {
            return null;
        }
    }
}
