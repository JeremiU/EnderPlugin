package io.github.rookietec9.enderplugin.configs.associates;

import io.github.rookietec9.enderplugin.configs.Config;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.reference.Worlds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Jeremi
 * @version 17.7.4
 * @since 16.8.9
 */
public class Hub extends Associate {

    public Hub() {
        super(new Config(true, "", "hubs.yml", EnderPlugin.getInstance()));
    }

    public Location getLoc() {
        return new Location(getWorld(), getX(), getY(), getZ(), getYaw(), getPitch());
    }

    private World getWorld() {
        return Bukkit.getWorld(getString("world", Worlds.HUB));
    }

    private double getX() {
        return getDouble("x", -274.5);
    }

    private double getY() {
        return getDouble("y", 64);
    }

    private double getZ() {
        return getDouble("z", -57.5);
    }

    private float getYaw() {
        return getFloat("yaw", -218.5f);
    }

    private float getPitch() {
        return getFloat("pitch", 90);
    }

    public void setLocation(Location location) {
        set("x", location.getX());
        set("y", location.getY());
        set("z", location.getZ());
        set("yaw", location.getYaw());
        set("pitch", location.getPitch());
    }
}