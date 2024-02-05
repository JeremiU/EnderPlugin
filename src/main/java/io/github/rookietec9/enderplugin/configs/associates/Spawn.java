package io.github.rookietec9.enderplugin.configs.associates;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Jeremi
 * @version 25.2.6
 * @since 17.7.4
 */
public class Spawn extends Associate {

    private final String name;

    public Spawn(String name) {
        super(new Config(false, "Worlds", name + ".yml", EnderPlugin.getInstance()));
        this.name = name;
        getConfig().modifyYaml();
    }

    public World getWorld() { //TODO: CONSIDER WORLD UUIDs
        return Bukkit.getWorld(getString("world", name));
    }

    private double getX() {
        return getDouble("x", Bukkit.getWorld(name).getSpawnLocation().getBlockX());
    }

    private double getY() {
        return getDouble("y", Bukkit.getWorld(name).getSpawnLocation().getBlockY());
    }

    private double getZ() {
        return getDouble("z", Bukkit.getWorld(name).getSpawnLocation().getBlockZ());
    }

    private float getYaw() {
        return getFloat("yaw", Bukkit.getWorld(name).getSpawnLocation().getYaw());
    }

    private float getPitch() {
        return getFloat("pitch", Bukkit.getWorld(name).getSpawnLocation().getPitch());
    }

    public void setLocation(Location loc) {
        set("x", loc.getX());
        set("y", loc.getY());
        set("z", loc.getZ());
        set("yaw", loc.getYaw());
        set("pitch", loc.getPitch());
    }

    public Location location() {return new Location(getWorld(), getX(), getY(), getZ(), getYaw(), getPitch());}
}