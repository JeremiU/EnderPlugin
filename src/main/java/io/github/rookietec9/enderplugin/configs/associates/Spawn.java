package io.github.rookietec9.enderplugin.configs.associates;

import io.github.rookietec9.enderplugin.configs.Config;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Jeremi
 * @version 17.7.7
 * @since 17.7.4
 */
public class Spawn extends Associate {

    private final String name;

    public Spawn(String name) {
        super(new Config(false, "Worlds", name + ".yml", EnderPlugin.getInstance()));
        this.name = name;
        getConfig().modifyYaml();
    }

    public String getName() {
        return name;
    }

    private World getWorld() { //TODO: CONSIDER WORLD UUIDs
        return Bukkit.getWorld(getString("world", name));
    }

    public void setWorld(World w) {
        set("world", w.getName());
    }

    private double getX() {
        return getDouble("x", Bukkit.getWorld(name).getSpawnLocation().getBlockX());
    }

    public void setX(double i) {
        set("x", i);
    }

    private double getY() {
        return getDouble("y", Bukkit.getWorld(name).getSpawnLocation().getBlockY());
    }

    public void setY(double i) {
        set("y", i);
    }

    private double getZ() {
        return getDouble("z", Bukkit.getWorld(name).getSpawnLocation().getBlockZ());
    }

    public void setZ(double i) {
        set("z", i);
    }

    private float getYaw() {
        return getFloat("yaw", Bukkit.getWorld(name).getSpawnLocation().getYaw());
    }

    public void setYaw(float f) {
        set("yaw", f);
    }

    private float getPitch() {
        return getFloat("pitch", Bukkit.getWorld(name).getSpawnLocation().getPitch());
    }

    public void setPitch(float f) {
        set("pitch", f);
    }

    public Location location() {return new Location(getWorld(), getX(), getY(), getZ(), getYaw(), getPitch());}
}