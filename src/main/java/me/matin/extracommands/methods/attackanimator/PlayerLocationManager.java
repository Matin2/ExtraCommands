

package me.matin.extracommands.methods.attackanimator;

import me.matin.extracommands.ExtraCommands;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Predicate;

import static org.bukkit.Bukkit.getServer;

public class PlayerLocationManager {

    public static void setTrackingRange(Map<World, Integer> playerTrackingRange) {
        playerTrackingRange.clear();
        int defaultRange = getServer().spigot().getConfig().getInt("world-settings.default.entity-tracking-range.players", 64);
        for (World world : getServer().getWorlds()) {
            int range = getServer().spigot().getConfig().getInt("world-settings." + world.getName() + ".entity-tracking-range.players", defaultRange);
            playerTrackingRange.put(world, range);
        }
    }

    public static Location getPlayerLocation(Player player) {
        return player.getLocation();
    }

    public static Collection<Player> filterOutOfRange(Collection<Player> players, Entity entity) {
        return filterOutOfRange(players, entity.getLocation());
    }

    public static Collection<Player> filterOutOfRange(Collection<Player> players, Location location) {
        return filterOutOfRange(players, location, player -> true);
    }

    public static Collection<Player> filterOutOfRange(Collection<Player> players, Location location, Predicate<Player> predicate) {
        Collection<Player> playersInRange = new HashSet<>();
        int range = ExtraCommands.playerTrackingRange.getOrDefault(location.getWorld(), 64);
        range *= range;
        for (Player player : players) {
            Location playerLocation = PlayerLocationManager.getPlayerLocation(player);
            if (playerLocation.getWorld().equals(location.getWorld()) && (playerLocation.distanceSquared(location) <= range) && predicate.test(player)) {
                playersInRange.add(player);
            }
        }
        return playersInRange;
    }

}
