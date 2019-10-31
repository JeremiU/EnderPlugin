package io.github.rookietec9.enderplugin.API.configs.associates;

import io.github.rookietec9.enderplugin.API.configs.Config;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.ChatColor;

/**
 * @author Jeremi
 * @version 16.2.6
 * @since ?.?.?
 */
public class Lang {
    private final Config langConfig;
    private final String plugPath = "Main.plugMsg";
    private final String errorPath = "Main.error";
    private final String onlyUserPath = "Main.onlyUser";
    private final String syntaxPath = "Main.syntax";
    private final String offlinePath = "Main.offline";
    private final String lightPath = "Main.color.light";
    private final String darkPath = "Main.color.dark";
    private final String txtPath = "Main.color.text";
    private final String numPath = "Main.numberFormatException";
    private final String cmdExPath = "Main.color.cmd";
    private final String blockNoPath = "Main.blockNotAllowed";
    private final String consoleNoPath = "Main.consoleNotAllowed";

    public Lang(Langs l) {
        langConfig = new Config(false, "Texts", l.toString() + ".yml", EnderPlugin.getInstance());
        langConfig.modifyYaml();
        String[] allPaths = {plugPath, errorPath, onlyUserPath, syntaxPath, offlinePath, lightPath, darkPath, txtPath, numPath, cmdExPath, blockNoPath, consoleNoPath};
        for (int i = 0; i < allPaths.length; i++) {
            if (langConfig.getYaml().get(allPaths[i]) == null) {
                langConfig.getYaml().createSection(allPaths[i]);
                String[] name = {"&7[&3&lE&b&lC&f]&7 ", "&4Error: &c ", "Only users can run this command.", "&3SYNTAX: &B", "The request player is offline.", "AQUA", "DARK_AQUA", "GRAY", "That's not a number!"
                        , "WHITE", "Command blocks cannot use this command", "Consoles cannot use this command."
                };
                langConfig.getYaml().set(allPaths[i], name[i]);
            }
        }
        langConfig.modifyYaml();
    }

    public ChatColor getDarkColor() {
        return ChatColor.valueOf(langConfig.getYaml().getString(darkPath));
    }

    public ChatColor getLightColor() {
        return (ChatColor) get(ChatColor.valueOf(langConfig.getYaml().getString(lightPath)));
    }

    public ChatColor getTxtColor() {
        return (ChatColor) get(ChatColor.valueOf(langConfig.getYaml().getString(txtPath)));
    }

    public ChatColor getCmdExColor() {
        return (ChatColor) get(ChatColor.valueOf(langConfig.getYaml().getString(cmdExPath)));
    }

    public String getPlugMsg() {
        return (String) get(ChatColor.translateAlternateColorCodes('&', langConfig.getYaml().getString(plugPath) + getTxtColor()) + getTxtColor());
    }

    public String getErrorMsg() {
        return (String) get(ChatColor.translateAlternateColorCodes('&', langConfig.getYaml().getString(errorPath)));
    }

    public String getNumFormatMsg() {
        return (String) get(getErrorMsg() + ChatColor.translateAlternateColorCodes('&', langConfig.getYaml().getString(numPath)));
    }

    public String getOnlyUserMsg() {
        return (String) get(getErrorMsg() + ChatColor.translateAlternateColorCodes('&', langConfig.getYaml().getString(onlyUserPath)));
    }

    public String getOfflineMsg() {
        return (String) get(getErrorMsg() + ChatColor.translateAlternateColorCodes('&', langConfig.getYaml().getString(offlinePath)));
    }

    public String getSyntaxMsg() {
        return (String) get(getPlugMsg() + ChatColor.translateAlternateColorCodes('&', langConfig.getYaml().getString(syntaxPath)));
    }

    private Object get(Object object) {
        langConfig.modifyYaml();
        return object;
    }
}