package io.github.rookietec9.enderplugin.API.configs.associates;

import io.github.rookietec9.enderplugin.API.configs.Config;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

/**
 * @deprecated
 */
public class Warp {
    private final Config warpConfig;
    private final String name;

    public Warp(String name) {
        this.warpConfig = new Config(false,"Warp",name + ".yml", EnderPlugin.getInstance());
        this.name = name;
        this.warpConfig.modifyYaml();
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        if (warpConfig.getYaml().get("world") == null) {
            set("world", "hub");
        }
        return Bukkit.getWorld(warpConfig.getYaml().getString("world"));
    }

    public void setWorld(World w) {
        set("world", w.getName());
    }

    public double getX() {
        if (warpConfig.getYaml().get("x") == null) {
            set("x", 0);
        }
        return warpConfig.getYaml().getDouble("x");
    }

    public void setX(double i) {
        set("x", i);
    }

    public double getY() {
        if (warpConfig.getYaml().get("y") == null) {
            set("y", 0);
        }
        return warpConfig.getYaml().getDouble("y");
    }

    public void setY(double i) {
        set("y", i);
    }

    public double getZ() {
        if (warpConfig.getYaml().get("z") == null) {
            set("z", 0);
        }
        return warpConfig.getYaml().getDouble("z");
    }

    public void setZ(double i) {
        set("z", i);
    }

    public float getYaw() {
        if (warpConfig.getYaml().get("yaw") == null) {
            set("yaw", 0);
        }
        return (float) warpConfig.getYaml().getLong("yaw");
    }

    public void setYaw(long l) {
        set("yaw", l);
    }

    public float getPitch() {
        if (warpConfig.getYaml().get("pitch") == null) {
            set("pitch", 0);
        }
        return (float) warpConfig.getYaml().getLong("pitch");
    }

    public void setPitch(long l) {
        set("pitch", l);
    }

    private void set(String path, Object o) {
        this.warpConfig.modifyYaml();
        this.warpConfig.getYaml().set(path, o);
        this.warpConfig.modifyYaml();
    }
}