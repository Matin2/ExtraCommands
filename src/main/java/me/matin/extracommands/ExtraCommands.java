package me.matin.extracommands;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.matin.extracommands.commands.CommandManager;
import me.matin.extracommands.methods.attackanimator.PlayerLocationManager;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ExtraCommands extends JavaPlugin {

    public static ExtraCommands plugin = null;

    public static ProtocolManager protocolManager;

    public static Map<World, Integer> playerTrackingRange = new HashMap<>();

    public static String[] depends = new String[]{};

    @Override
    public void onEnable() {
        plugin = this;
        Hooks.dependencies(depends);
        if (Hooks.hasPlugin("ProtocolLib")) protocolManager = ProtocolLibrary.getProtocolManager();
        PlayerLocationManager.setTrackingRange(playerTrackingRange);
        Objects.requireNonNull(getCommand("extracommands")).setExecutor(new CommandManager());
        System.out.println("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        System.out.println("Plugin disabled.");
    }
}
