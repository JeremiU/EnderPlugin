package io.github.rookietec9.enderplugin.configs.associates;

import io.github.rookietec9.enderplugin.configs.Config;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.ChatColor;

/**
 * @author Jeremi
 * @version 22.4.1
 */
public class Associate {
    private final Config config;

    public Associate(Config config) {
        this.config = config;
        config.modifyYaml();
    }

    public Config getConfig() {
        return config;
    }

    public Associate set(String path, Object o) {
        config.getYaml().set(path, o);

        config.modifyYaml();
        return this;
    }

    public Object getPath(String path, Object defaultObj) {
        config.reloadYaml();
        if (null == config.getYaml().get(path)) {
            set(path, defaultObj);
        }
        return config.getYaml().get(path);
    }

    public boolean getBoolean(String path, boolean defaultBool) {
        return Boolean.parseBoolean(getPath(path, defaultBool).toString());
    }

    public int getInteger(String path, int defaultInt) {
        return Integer.parseInt(getPath(path, defaultInt).toString());
    }

    public double getDouble(String path, double defaultDouble) {
        return Double.parseDouble(getPath(path, defaultDouble).toString());
    }

    public float getFloat(String path, float defaultFloat) {
        return Float.parseFloat(getPath(path, defaultFloat).toString());
    }

    public byte getByte(String path, byte defaultByte) {
        return Byte.parseByte(getPath(path, defaultByte).toString());
    }

    public char getChar(String path, char defaultChar) {
        return getPath(path, defaultChar).toString().charAt(0);
    }

    public String getString(String path, Object defaultObject) {
        return getPath(path, defaultObject.toString()).toString();
    }

    public int getInteger(String path, Object defaultObject) {
        return getInteger(path, Integer.parseInt(defaultObject.toString()));
    }

    String getPathColored(String path, Object defaultObj) {
        return Minecraft.tacc((String) getPath(path, defaultObj));
    }
}