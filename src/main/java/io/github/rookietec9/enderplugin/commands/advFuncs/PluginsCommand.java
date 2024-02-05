package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * Lists all the plugins on the server.
 *
 * @author Jeremi
 * @version 22.8.0
 * @since 2.9.3
 */
public class PluginsCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return msg(sender, "Plugins " + getPluginList());
    }

    private String getPluginList() {
        StringBuilder pluginList = new StringBuilder();
        Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
        for (int i = 0; i < plugins.length; i++) {
            if (pluginList.length() > 0) pluginList.append(ChatColor.WHITE);
            if (!plugins[i].isEnabled()) pluginList.append(ChatColor.STRIKETHROUGH).append(serverLang().getDarkColor()).append(plugins[i].getDescription().getName());
            else pluginList.append(plugins[i].getDescription().getName());

            if (i + 1 < plugins.length) pluginList.append(ChatColor.WHITE).append(", ").append(ChatColor.WHITE).append(serverLang().getDarkColor());
        }
        ChatColor color = ChatColor.RESET;
        if (plugins.length >= 21) color = ChatColor.DARK_RED;
        if (plugins.length <= 21) color = ChatColor.RED;
        if (plugins.length <= 16) color = ChatColor.AQUA;
        if (plugins.length <= 11) color = ChatColor.GREEN;
        if (plugins.length <= 6) color = ChatColor.YELLOW;
        if (plugins.length <= 3) color = ChatColor.GOLD;

        return serverLang().getTxtColor() + "(" + color + (plugins.length) + serverLang().getTxtColor() + "): " + ChatColor.WHITE + pluginList.toString();
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return null;
    }

    public List<String> commandNames() {
        return List.of("plugins");
    }
}