package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.USER;

/**
 * Get's a player's UUID.
 *
 * @author Jeremi
 * @version 25.5.4
 * @since 1.9.6
 */
public class UUIDCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1) return msg(sender, getSyntax(label));
        if (Bukkit.getOfflinePlayer(args[0]) == null) return msg(sender, serverLang().getErrorMsg() + "That player is not registered!");

        sender.sendMessage(ChatColor.WHITE + Bukkit.getOfflinePlayer(args[0]).getName() + "'s uuid: ");
        for (OfflinePlayer op : Bukkit.getOfflinePlayers()) {
            if (op.getName().equalsIgnoreCase(args[0])) {
                if (!op.hasPlayedBefore()) return msg(sender, serverLang().getErrorMsg() + "That player has no joined before!");
                if (op.isBanned()) return msg(sender, serverLang().getPlugMsg() + op.getName() + " has been" + serverLang().getLightColor() + " banned" + serverLang().getTxtColor() + "!");

                net.md_5.bungee.api.ChatColor color = DataPlayer.getUser(op).wasOnline() ? net.md_5.bungee.api.ChatColor.AQUA : net.md_5.bungee.api.ChatColor.RED;
                String uuid = op.getUniqueId().toString();

                if (sender instanceof Player) {
                    Player player = (Player) sender;

                    TextComponent uuidText = new TextComponent(uuid);
                    TextComponent uuidHover = new TextComponent("Copy to clipboard");
                    uuidText.setColor(color);
                    uuidText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, uuid));
                    uuidText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{uuidHover}));
                    player.spigot().sendMessage(uuidText);
                    return true;
                }
                return msg(sender, color + uuid);
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> tab = new ArrayList<>();
            Arrays.stream(Bukkit.getOfflinePlayers()).filter(x -> x.hasPlayedBefore() && !tab.contains(x.getName())).forEach(x -> tab.add(x.getName()));
            return tabOption(args[0], tab);
        }
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(USER, true);
    }

    public List<String> commandNames() {
        return List.of("uuid");
    }
}