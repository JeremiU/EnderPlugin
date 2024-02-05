package io.github.rookietec9.enderplugin.configs.associates;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.Config;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Jeremi
 * @version 25.7.3
 */
public class Warp extends Associate {
    private static final File files = new File(EnderPlugin.getInstance().getDataFolder().getPath(), "Warp");
    private final String name;

    public Warp(String name) {
        super(new Config(false, "Warp", name + ".yml", EnderPlugin.getInstance()));
        this.name = name;
        getConfig().modifyYaml();
    }

    public static boolean exists(String name) {
        return new File(EnderPlugin.getInstance().getDataFolder().getPath() + File.separator + "Warp", name + ".yml").exists();
    }

    public static List<String> getWarps() {
        List<String> warps = new ArrayList<>();
        for (File f : Objects.requireNonNull(files.listFiles())) warps.add(f.getName().replace(".yml", ""));
        return warps;
    }

    public static boolean delete(String warpName) {
        File file = new File(files, warpName + ".yml");
        return file.delete();
    }

    public String getName() {
        return name;
    }

    public World getWorld() { //TODO: CONSIDER WORLD UUIDs
        return Bukkit.getWorld(getString("world", "hub"));
    }

    public void setWorld(World w) {
        set("world", w.getName());
    }

    private double getX() {
        return getDouble("x", 0);
    }

    public void setX(double i) {
        set("x", i);
    }

    private double getY() {
        return getDouble("y", 0);
    }

    public void setY(double i) {
        set("y", i);
    }

    private double getZ() {
        return getDouble("z", 0);
    }

    public void setZ(double i) {
        set("z", i);
    }

    private float getYaw() {
        return getFloat("yaw", 0);
    }

    public void setYaw(float f) {
        set("yaw", f);
    }

    private float getPitch() {
        return getFloat("pitch", 0);
    }

    public void setPitch(float f) {
        set("pitch", f);
    }

    public Location location() {return new Location(getWorld(), getX(), getY(), getZ(), getYaw(), getPitch());}
}