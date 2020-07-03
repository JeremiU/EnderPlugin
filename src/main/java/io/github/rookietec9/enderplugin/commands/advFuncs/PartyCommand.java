package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.USER;

/**
 * @author Jeremi
 * @version 22.8.8
 * @since 22.7.6
 */
public class PartyCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());

        Player player = (Player) sender;
        if (args.length < 1 || args.length > 2) return msg(sender, getSyntax(label));
        switch (args[0].toLowerCase()) {
            case "create" -> {
                PartySystem.create(player);
                return msg(sender, serverLang().getPlugMsg() + "Created a party.");
            }
            case "invite" -> {
                if (args.length != 2) return msg(sender, serverLang().getErrorMsg() + "You need to specify who to invite.");
                if (Bukkit.getPlayer(args[1]) == null) return msg(sender, serverLang().getOfflineMsg());
                Player target = Bukkit.getPlayer(args[1]);
                if (target.equals(player)) return msg(sender, serverLang().getErrorMsg() + "Cannot invite yourself.");
                target.sendMessage(serverLang().getPlugMsg() + DataPlayer.getUser(player).getNickName() + " has sent you a party invite.");

                TextComponent accept = new TextComponent("Accept");
                TextComponent acceptHover = new TextComponent("Click to join " + DataPlayer.getUser(player).getNickName() + "'s party.");
                accept.setColor(ChatColor.GREEN);
                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + sender.getName()));
                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{acceptHover}));

                TextComponent decline = new TextComponent("Decline");
                TextComponent declineHover = new TextComponent("Click to decline " + DataPlayer.getUser(player).getNickName() + "'s party invite.");
                decline.setColor(ChatColor.RED);
                decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party decline " + sender.getName()));
                decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{declineHover}));

                target.spigot().sendMessage(accept);
                target.spigot().sendMessage(decline);
            }
            case "accept" -> {
                if (args.length != 2) return msg(sender, serverLang().getErrorMsg() + "You need to specify who sent you an invite.");
                if (Bukkit.getPlayer(args[1]) == null) return msg(sender, serverLang().getOfflineMsg());
                Player owner = Bukkit.getPlayer(args[1]);
                if (owner.equals(player)) return msg(sender, serverLang().getErrorMsg() + "Cannot invite yourself.");

                if (PartySystem.getFromPlayer(owner) == null) return msg(sender, serverLang().getErrorMsg() + "That player is not in a party.");
                PartySystem.getFromPlayer(owner).notifyPlayers(DataPlayer.getUser(player).getNickName() + " accepted an invite.");
                PartySystem.getFromPlayer(owner).addPlayers(player);
                return msg(sender, serverLang().getPlugMsg() + "Accepted the invite from " + DataPlayer.getUser(owner).getNickName() + ".");
            }
            case "decline" -> {
                if (args.length != 2) return msg(sender, serverLang().getErrorMsg() + "You need to specify who sent you an invite.");
                if (Bukkit.getPlayer(args[1]) == null) return msg(sender, serverLang().getOfflineMsg());
                Player owner = Bukkit.getPlayer(args[1]);
                if (owner.equals(player)) return msg(sender, serverLang().getErrorMsg() + "Cannot invite yourself.");

                if (PartySystem.getFromPlayer(owner) == null) return msg(sender, serverLang().getErrorMsg() + "That player is not in a party.");
                PartySystem.getFromPlayer(owner).notifyPlayers(DataPlayer.getUser(player).getNickName() + " declined an invite.");
                return msg(sender, serverLang().getPlugMsg() + "Declined the invite from " + DataPlayer.getUser(owner).getNickName() + ".");
            }
            case "kick" -> {
                if (args.length != 2) return msg(sender, serverLang().getErrorMsg() + "You need to specify a player.");
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) return msg(sender, serverLang().getOfflineMsg());
                if (target.equals(player)) return msg(sender, serverLang().getErrorMsg() + "Cannot kick yourself.");

                if (PartySystem.getFromPlayer(player) == null) return msg(sender, serverLang().getErrorMsg() + "You're not in a party.");
                if (!PartySystem.getFromPlayer(player).getLeader().equals(player.getUniqueId())) return msg(sender, serverLang().getErrorMsg() + "You're not the party leader.");
                if (PartySystem.getFromPlayer(target) != null && !PartySystem.getFromPlayer(player).equals(PartySystem.getFromPlayer(target))) return msg(sender, serverLang().getErrorMsg() + "That player is not in your party.");
                PartySystem.getFromPlayer(player).removePlayers(target);

                PartySystem.getFromPlayer(player).notifyPlayers(DataPlayer.getUser(target).getNickName() + " was kicked from your party.");
                return msg(sender, serverLang().getPlugMsg() + "Removed " + DataPlayer.getUser(target).getNickName() + " from your party.");
            }
            case "leave" -> {
                if (PartySystem.getFromPlayer(player) == null) return msg(sender, serverLang().getErrorMsg() + "You're not in a party.");
                PartySystem.getFromPlayer(player).removePlayers(player);
                PartySystem.getFromPlayer(player).notifyPlayers(DataPlayer.getUser(player).getNickName() + " left your party.");
                return msg(sender, serverLang().getPlugMsg() + "Left the party.");
            }
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return List.of("create", "accept", "decline", "kick", "leave");
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + "create" + "\n" +
                helpLabel(label) + "invite " + helpBr(USER, true) + "\n" +
                helpLabel(label) + "accept " + helpBr(USER, true) + "\n" +
                helpLabel(label) + "decline " + helpBr(USER, true) + "\n" +
                helpLabel(label) + "kick " + helpBr(USER, true) + "\n" +
                helpLabel(label) + "leave" + "\n";
    }

    public List<String> commandNames() {
        return List.of("party");
    }
}