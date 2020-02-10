package io.github.rookietec9.enderplugin.API.configs.associates;

import io.github.rookietec9.enderplugin.API.configs.Config;
import org.bukkit.ChatColor;

public class Associate {
    protected Config config;

    Associate(Config config) {
        this.config = config;
        config.modifyYaml();
    }

    public Config getConfig() {
        return config;
    }

    public void set(String path, Object o) {
        config.modifyYaml();
        config.getYaml().set(path, o);
    }

    Object getPath(String path, Object defaultObj) {
        config.modifyYaml();
        if (config.getYaml().get(path) == null) {
            set(path, defaultObj);
        }
        return config.getYaml().get(path);
    }

    String getPathColored(String path, Object defaultObj) {
        return ChatColor.translateAlternateColorCodes('&', (String) getPath(path, defaultObj));
    }
}