package io.github.rookietec9.enderplugin.API.configs.associates;

import io.github.rookietec9.enderplugin.API.configs.Config;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.EnderPlugin;
import javafx.util.Pair;
import org.bukkit.ChatColor;

/**
 * @author Jeremi
 * @version 16.2.6
 * @since ?.?.?
 */
public class Lang extends Associate {
    private final Pair<String, String> plug = new Pair<>("Main.plugMsg", "&7[&3&lE&b&lC&f]&7 ");
    private final Pair<String, String> error = new Pair<>("Main.error", "&4Error: &c ");
    private final Pair<String, String> onlyUser = new Pair<>("Main.onlyUser", "Only users can run this command.");
    private final Pair<String, String> syntax = new Pair<>("Main.syntax", "&3SYNTAX: &B");
    private final Pair<String, String> offline = new Pair<>("Main.offline", "The request player is offline.");
    private final Pair<String, String> light = new Pair<>("Main.color.light", "AQUA");
    private final Pair<String, String> dark = new Pair<>("Main.color.dark", "DARK_AQUA");
    private final Pair<String, String> txt = new Pair<>("Main.color.text", "GRAY");
    private final Pair<String, String> cmd = new Pair<>("Main.color.cmd", "WHITE");
    private final Pair<String, String> num = new Pair<>("Main.numberFormatException", "That's not a number!");
    private final Pair<String, String> noBlock = new Pair<>("Main.blockNotAllowed", "Command blocks cannot use this command.");
    private final Pair<String, String> noConsole = new Pair<>("Main.blockNotAllowed", "Consoles cannot use this command.");

    Pair<String, String>[] pairs = new Pair[]{plug, error, onlyUser, syntax, offline, light, dark, txt, cmd, num, noBlock, noConsole};

    public Lang(Langs l) {
        super(new Config(false, "Texts", l.toString() + ".yml", EnderPlugin.getInstance()));

        for (Pair<String, String> pair : pairs) {
            if (config.getYaml().get(pair.getKey()) == null) {
                config.getYaml().createSection(pair.getKey());
                config.getYaml().set(pair.getKey(), pair.getValue());
            }
        }
        config.modifyYaml();
    }

    public ChatColor getDarkColor() {
        return (ChatColor.valueOf((String) getPath(dark.getKey(), dark.getValue())));
    }

    public ChatColor getLightColor() {
        return (ChatColor.valueOf((String) getPath(light.getKey(), light.getValue())));
    }

    public ChatColor getTxtColor() {
        return (ChatColor.valueOf((String) getPath(txt.getKey(), txt.getValue())));
    }

    public ChatColor getCmdExColor() {
        return (ChatColor.valueOf((String) getPath(cmd.getKey(), cmd.getValue())));
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
        return getPlugMsg() + getPathColored(syntax.getKey(), syntax.getKey());
    }
}