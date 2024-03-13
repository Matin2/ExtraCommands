package me.matin.extracommands.methods.attackanimator;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import me.matin.extracommands.ExtraCommands;
import me.matin.extracommands.Hooks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public class PacketManager {
    private static final Plugin plugin = ExtraCommands.plugin;

    private static final ProtocolManager protocolManager = ExtraCommands.protocolManager;

    public static void sendMainhandMovement(Collection<Player> players, Player entity) {

        if (!Hooks.hasPlugin("ProtocolLib")) return;

        PacketContainer packet1 = protocolManager.createPacket(PacketType.Play.Server.ANIMATION);
        packet1.getIntegers().write(0, entity.getEntityId());
        packet1.getIntegers().write(1, 0);

        if (!plugin.isEnabled()) {
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            if (entity.isOnline()) {
                protocolManager.sendServerPacket(entity, packet1);
            }
        });
    }

    public static void sendOffhandMovement(Collection<Player> players, Player entity) {

        if (!Hooks.hasPlugin("ProtocolLib")) return;

        PacketContainer packet1 = protocolManager.createPacket(PacketType.Play.Server.ANIMATION);
        packet1.getIntegers().write(0, entity.getEntityId());
        packet1.getIntegers().write(1, 3);

        if (!plugin.isEnabled()) {
            return;
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            if (entity.isOnline()) {
                protocolManager.sendServerPacket(entity, packet1);
            }
        });
    }

    public static void sendHandMovement(Collection<Player> players, Player entity, AttackAnimator.HandType type) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Collection<Player> playersInRange = PlayerLocationManager.filterOutOfRange(players, entity);
            if (type == AttackAnimator.HandType.MAINHAND) {
                sendMainhandMovement(playersInRange, entity);
            } else if (type == AttackAnimator.HandType.OFFHAND) {
                sendOffhandMovement(playersInRange, entity);
            }
        });
    }
}