package me.matin.extracommands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class Hooks {

    public static boolean hasPlugin(String pluginName) {
        Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }

    public static boolean hasPlugins(String pluginNames) {
        String[] pluginNames1 = pluginNames.split(",");
        return hasPlugs(pluginNames1);
    }

    public static boolean hasPlugins(String[] pluginNames) {
        return hasPlugs(pluginNames);
    }

    private static boolean hasPlugs(String[] pluginNames) {
        List<Boolean> statues = new ArrayList<>();
        for (String pluginName : pluginNames) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName.trim());
            if (plugin != null && plugin.isEnabled()) {
                statues.add(true);
            } else {
                statues.add(false);
            }
        }
        for(boolean b : statues) if(!b) return false;
        return true;
    }

    public static Plugin getPlugin(String pluginName) {
        return Bukkit.getPluginManager().getPlugin(pluginName);
    }

    public static void dependencies(String[] depends) {
        for (String plugin : depends) {
            if (!hasPlugin(plugin)) {
                System.out.println(plugin + "is required but not installed, plugin disabled.");
                Bukkit.getPluginManager().disablePlugin(ExtraCommands.plugin);
            }
        }
    }
}
