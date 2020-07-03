package io.github.rookietec9.enderplugin.configs.associates;

import io.github.rookietec9.enderplugin.configs.Config;
import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.utils.datamanagers.Pair;
import org.bukkit.ChatColor;

/**
 * @author Jeremi
 * @version 21.5.2
 * @since ?.?.?
 */
public class Lang extends Associate {
    private final Pair<String, String> plug = new Pair<>("Main.plugMsg", "&7[&3&lE&b&lC&7] ");
    private final Pair<String, String> error = new Pair<>("Main.error", "&4Error: &c ");
    private final Pair<String, String> onlyUser = new Pair<>("Main.onlyUser", "Only users can run this command.");
    private final Pair<String, String> syntax = new Pair<>("Main.syntax", "&3SYNTAX: &B");
    private final Pair<String, String> offline = new Pair<>("Main.offline", "The request player is offline.");
    private final Pair<String, String> light = new Pair<>("Main.color.light", "AQUA");
    private final Pair<String, String> dark = new Pair<>("Main.color.dark", "DARK_AQUA");
    private final Pair<String, String> txt = new Pair<>("Main.color.text", "GRAY");
    private final Pair<String, String> cmd = new Pair<>("Main.color.cmd", "WHITE");
    private final Pair<String, String> num = new Pair<>("Main.numberFormatException", "That's not a number!");

    public Lang() {
        super(new Config(false, "Texts", "EN_US" + ".yml", EnderPlugin.getInstance()));
    }

    public ChatColor getDarkColor() {
        return (ChatColor.valueOf(getString(dark.getKey(), dark.getValue())));
    }

    public ChatColor getLightColor() {
        return (ChatColor.valueOf(getString(light.getKey(), light.getValue())));
    }

    public ChatColor getTxtColor() {
        return (ChatColor.valueOf(getString(txt.getKey(), txt.getValue())));
    }

    public ChatColor getCmdExColor() {
        return (ChatColor.valueOf(getString(cmd.getKey(), cmd.getValue())));
    }

    public String getPlugMsg() {
        return getPathColored(plug.getKey(), plug.getValue()) + getTxtColor();
    }

    public String getErrorMsg() {
        return getPathColored(error.getKey(), error.getValue());
    }

    public String getNumFormatMsg() {
        return getErrorMsg() + getPathColored(num.getKey(), num.getValue());
    }

    public String getOnlyUserMsg() {
        return getErrorMsg() + getPathColored(onlyUser.getKey(), onlyUser.getKey());
    }

    public String getOfflineMsg() {
        return getErrorMsg() + getPathColored(offline.getKey(), offline.getValue());
    }

    public String getSyntaxMsg() {
        return getPlugMsg() + getPathColored(syntax.getKey(), syntax.getValue());
    }
}