package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * Lists all the plugins on the server.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 2.9.3
 */
public class PluginsCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("Plugins " + getPluginList(sender));
        return true;
    }

    private String getPluginList(CommandSender s) {
        Lang l = new Lang(Langs.fromSender(s));
        StringBuilder pluginList = new StringBuilder();
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        for (Plugin plugin : plugins) {
            if (pluginList.length() > 0) {
                pluginList.append(ChatColor.WHITE);
            }
            if (!plugin.isEnabled()) {
                pluginList.append(ChatColor.STRIKETHROUGH + "").append(l.getDarkColor()).append(plugin.getDescription().getName());
            } else {
                pluginList.append(plugin.getDescription().getName());
            }
            pluginList.append(ChatColor.WHITE + ", " + ChatColor.WHITE).append(l.getDarkColor());
        }
        ChatColor color = ChatColor.RESET;
        if (plugins.length == 1) {
            color = ChatColor.GOLD;
        }

        if (plugins.length <= 21) {
            color = ChatColor.RED;
        }

        if (plugins.length <= 16) {
            color = ChatColor.AQUA;
        }

        if (plugins.length <= 11) {
            color = ChatColor.GREEN;
        }

        if (plugins.length <= 6) {
            color = ChatColor.YELLOW;
        }

        if (plugins.length >= 21) {
            color = ChatColor.DARK_RED;
        }
        return l.getTxtColor() + "(" + color + (plugins.length) + l.getTxtColor()+ "): " + ChatColor.WHITE + pluginList.toString().substring(0, pluginList.toString().lastIndexOf(",") - 2);
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "plugins";
    }
}