package io.github.rookietec9.enderplugin.configs;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

/**
 * @author bwfcwalshy
 * @author TheEnderCrafter9
 * @version 21.4.9
 * @since 13.4.4
 */
public class Config {
    private final JavaPlugin plugin;
    private final File configFile;
    private FileConfiguration fileConfiguration;

    public Config(boolean isMain, String folderName, String fileName, JavaPlugin plugin) {
        String folderPath;

        if (plugin == null) throw new IllegalArgumentException("Plugin cannot be null");
        this.plugin = plugin;
        if (fileName == null || fileName.isEmpty()) throw new IllegalArgumentException("Name cannot be null");
        if (!plugin.getDataFolder().exists()) System.out.println("Making Config (1): " + plugin.getDataFolder().mkdir());

        if (isMain || folderName.length() == 0) folderPath = plugin.getDataFolder().getPath();
        else folderPath = plugin.getDataFolder().getPath() + File.separator + folderName;

        File dataFolder = new File(folderPath);
        this.configFile = new File(dataFolder, fileName);
        if (!dataFolder.exists()) System.out.println("Making Config (2): " + dataFolder.mkdir());
        saveDefaultYaml();
    }

    public void reloadYaml() {
        fileConfiguration = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = plugin.getResource(configFile.getPath());
        if (null == defConfigStream) {
            return;
        }

        final YamlConfiguration defConfig;
        final byte[] contents;
        defConfig = new YamlConfiguration();
        try {
            contents = ByteStreams.toByteArray(defConfigStream);
        } catch (final IOException e) {
            EnderPlugin.getInstance().getLogger().log(Level.SEVERE, "Unexpected failure reading config.yml", e);
            return;
        }

        final String text = new String(contents, Charset.defaultCharset());
        if (!text.equals(new String(contents, Charsets.UTF_8))) {
            EnderPlugin.getInstance().getLogger().warning("Default system encoding may have misread config.yml from plugin jar");
        }

        try {
            defConfig.loadFromString(text);
        } catch (final InvalidConfigurationException e) {
            EnderPlugin.getInstance().getLogger().log(Level.SEVERE, "Cannot load configuration from jar", e);
        }
        fileConfiguration.setDefaults(defConfig);
    }

    public FileConfiguration getYaml() {
        if (null == fileConfiguration) this.reloadYaml();
        return fileConfiguration;
    }

    private void saveYaml() {
        if (null != fileConfiguration && null != configFile) {
            try {
                getYaml().save(configFile);
            } catch (IOException ex) {
                plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
            }
        }
    }

    public void saveDefaultYaml() {
        if (!configFile.exists()) {
            Path file = configFile.toPath();
            try {
                Files.createFile(file);
            } catch (FileAlreadyExistsException x) {
                Bukkit.getLogger().log(Level.INFO, "Tried to create a new Player Config, but it already existed! ");
            } catch (IOException x) {
                if (Bukkit.broadcast(ChatColor.DARK_RED + "AN ERROR OCCURRED WHILE ATTEMPTING TO CREATE FILE: " + this.configFile.getName(), "enderplugin.debug") == 0) {
                    Bukkit.getLogger().log(Level.SEVERE, "AN ERROR OCCURRED WHILE ATTEMPTING TO CREATE FILE: " + this.configFile.getName(), x);
                }
            }
        }
    }

    public void modifyYaml() {
        saveYaml();
        reloadYaml();
    }
}