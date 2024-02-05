package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.configs.associates.Spawn;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.PartySystem;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
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
import java.util.UUID;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.PREFIX_PARTY;
import static io.github.rookietec9.enderplugin.Reference.USER;

/**
 * @author Jeremi
 * @version 25.6.1
 * @since 22.7.6
 */
public class PartyCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());

        Player player = (Player) sender;
        if (args.length < 1 || args.length > 2) return msg(sender, getSyntax(label));
        switch (args[0].toLowerCase()) {
            case "create" -> {
                if (!PartySystem.create(player)) return msg(sender, serverLang().getErrorMsg() + "You're already in a party.");
                return msg(sender, PREFIX_PARTY + "Created a party.");
            }
            case "menu" -> {
                if (PartySystem.getFromPlayer(player) == null) return msg(sender, serverLang().getErrorMsg() + "You're not in a party.");
                if (PartySystem.getFromPlayer(player).getLeader().equals(player.getUniqueId())) player.openInventory(PartySystem.getFromPlayer(player).forLeader());
                else player.openInventory(PartySystem.getFromPlayer(player).forPlayer(player));
                return true;
            }
            case "invite" -> {
                if (args.length != 2) return msg(sender, serverLang().getErrorMsg() + "You need to specify who to invite.");
                return invite(Bukkit.getPlayer(args[1]), player);
            }
            case "accept", "decline" -> {
                if (args.length != 2) return msg(sender, serverLang().getErrorMsg() + "You need to specify who sent you an invite.");
                if (Bukkit.getPlayer(args[1]) == null) return msg(sender, serverLang().getOfflineMsg());
                Player owner = Bukkit.getPlayer(args[1]);
                if (owner.equals(player)) return msg(sender, serverLang().getErrorMsg() + "Cannot invite yourself.");

                if (PartySystem.getFromPlayer(owner) == null) return msg(sender, serverLang().getErrorMsg() + "That player is not in a party.");
                if (!PartySystem.getFromPlayer(owner).invites.containsKey(player.getUniqueId())) return msg(sender, serverLang().getErrorMsg() + "That invite doesn't exist.");
                if (PartySystem.getFromPlayer(player) != null) return msg(sender,"");
                if (!PartySystem.getFromPlayer(owner).invites.getOrDefault(player.getUniqueId(), true)) return msg(sender, serverLang().getErrorMsg() + "You have declined the invite!");

                PartySystem.getFromPlayer(owner).notifyPlayers(DataPlayer.getUser(player).getNickName() + " " + (args[0].equalsIgnoreCase("accept") ? "accepted" : "declined") + " an invite.", true);

                if (args[0].equalsIgnoreCase("accept")) PartySystem.getFromPlayer(owner).addPlayers(player);
                else PartySystem.getFromPlayer(owner).decline(player);

                return msg(sender, serverLang().getPlugMsg() + (args[0].equalsIgnoreCase("accept") ? "Accepted" : "Declined") + " the invite from " + DataPlayer.getUser(owner).getNickName() + ".");
            }
            case "kick" -> {
                if (args.length != 2) return msg(sender, serverLang().getErrorMsg() + "You need to specify a player.");
                Player target = Bukkit.getPlayer(args[1]);
                if (target == null) return msg(sender, serverLang().getOfflineMsg());
                if (target.equals(player)) return msg(sender, serverLang().getErrorMsg() + "Cannot kick yourself.");

                if (PartySystem.getFromPlayer(player) == null) return msg(sender, serverLang().getErrorMsg() + "You're not in a party.");
                if (!PartySystem.getFromPlayer(player).getLeader().equals(player.getUniqueId())) return msg(sender, serverLang().getErrorMsg() + "You're not the party leader.");
                if (PartySystem.getFromPlayer(target) != null && !PartySystem.getFromPlayer(player).equals(PartySystem.getFromPlayer(target))) return msg(sender, serverLang().getErrorMsg() + "That player is not in your party.");
                PartySystem.getFromPlayer(player).changeTeam(target, PartySystem.PartyTeam.NONE);
                PartySystem.getFromPlayer(player).removePlayers(target);

                PartySystem.getFromPlayer(player).notifyPlayers(DataPlayer.getUser(target).getNickName() + " was kicked from your party.", false);
                return msg(sender, PREFIX_PARTY + "Removed " + DataPlayer.getUser(target).getNickName() + " from your party.");
            }
            case "leave" -> {
                if (PartySystem.getFromPlayer(player) == null) return msg(sender, serverLang().getErrorMsg() + "You're not in a party.");
                if (PartySystem.getFromPlayer(player).getLeader().equals(player.getUniqueId())) return msg(sender, serverLang().getErrorMsg() + "You must disband the party.");
                PartySystem ps = PartySystem.getFromPlayer(player);
                PartySystem.getFromPlayer(player).removePlayers(player);
                ps.notifyPlayers(DataPlayer.getUser(player).getNickName() + " left your party.", true);
                return msg(sender, PREFIX_PARTY + "left the party.");
            }
            case "list" -> {
                if (PartySystem.getFromPlayer(player) == null) return msg(sender, serverLang().getErrorMsg() + "You're not in a party.");
                StringBuilder stringBuilder = new StringBuilder(PREFIX_PARTY);
                stringBuilder.append(PartySystem.getFromPlayer(player).partyCount());
                stringBuilder.append(" Player");
                if (PartySystem.getFromPlayer(player).partyCount() != 1) stringBuilder.append("s");
                stringBuilder.append(" in the party: ");

                for (int i = 0; i < PartySystem.getFromPlayer(player).partyCount(); i++) {
                    UUID[] uuids = new UUID[PartySystem.getFromPlayer(player).partyCount()];
                    Player partyPlayer = Bukkit.getPlayer(PartySystem.getFromPlayer(player).getPlayers().toArray(uuids)[i]);
                    if (partyPlayer == null) continue;
                    stringBuilder.append(Minecraft.teamColor(PartySystem.getFromPlayer(partyPlayer).getTeam(partyPlayer).toString(), true));
                    if (uuids[i].equals(PartySystem.getFromPlayer(player).getLeader())) stringBuilder.append(ChatColor.UNDERLINE);
                    stringBuilder.append(DataPlayer.getUser(partyPlayer).getNickName());
                    stringBuilder.append(ChatColor.GRAY);
                    if (i + 1 < PartySystem.getFromPlayer(player).getPlayers().size()) stringBuilder.append(", ");
                }
                return msg(sender, stringBuilder.toString());
            }
            case "team" -> {
                if (args.length != 2) return msg(sender, serverLang().getErrorMsg() + "You need to specify a team.");
                if (!Java.argWorks(args[1], "red", "green", "blue", "yellow", "good", "bad", "none")) return msg(sender, serverLang().getErrorMsg() + "You need to specify a correct team.");
                if (PartySystem.getFromPlayer(player) == null) return msg(sender, serverLang().getErrorMsg() + "You're not in a party.");
                PartySystem.PartyTeam pT = PartySystem.PartyTeam.valueOf(args[1].toUpperCase());
                PartySystem.getFromPlayer(player).changeTeam(player, pT);
                return msg(sender, PREFIX_PARTY + (pT == PartySystem.PartyTeam.NONE ? "Took you off the team." : "Put you on the " + Java.capFirst(pT.name()) + " team."));
            }
            case "disband" -> {
                if (!PartySystem.getFromPlayer(player).getLeader().equals(player.getUniqueId())) return msg(sender, serverLang().getErrorMsg() + "You are not the party leader.");
                PartySystem.getFromPlayer(player).notifyPlayers(DataPlayer.getUser(player).getNickName() + " disbanded the party.", false);
                PartySystem.getFromPlayer(player).clearPlayers();
                return msg(sender, PREFIX_PARTY + "Disbanded the party.");
            }
            case "warp" -> {
                if (PartySystem.getFromPlayer(player) == null) return msg(sender, serverLang().getErrorMsg() + "You're not in a party.");
                if (!PartySystem.getFromPlayer(player).getLeader().equals(player.getUniqueId())) return msg(sender, serverLang().getErrorMsg() + "You are not the party leader.");
                for (UUID uuid : PartySystem.getFromPlayer(player).getPlayers()) if (Bukkit.getPlayer(uuid) != null && !uuid.equals(PartySystem.getFromPlayer(player).getLeader())) Bukkit.getPlayer(uuid).teleport(new Spawn(player.getWorld().getName()).location());
                PartySystem.getFromPlayer(player).notifyPlayers("The party leader teleported you to their world.", false);
                return msg(sender, PREFIX_PARTY + "Teleported the party to your current world.");
            }
        }
        if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) return msg(sender, serverLang().getOfflineMsg());
            if (PartySystem.getFromPlayer(player) == null) PartySystem.create(player);
            return invite(target, player);
        }
        return msg(sender, getSyntax(label));
    }

    private boolean invite(Player toInvite, Player sender) {
        if (toInvite == null) return msg(sender, serverLang().getOfflineMsg());
        if (toInvite.equals(sender)) return msg(sender, serverLang().getErrorMsg() + "Cannot invite yourself.");
        if (PartySystem.getFromPlayer(sender) == null) PartySystem.create(sender);
        if (!PartySystem.getFromPlayer(sender).invites.getOrDefault(toInvite.getUniqueId(), true)) return msg(sender, serverLang().getErrorMsg() + "That player has recently declined your invite.");
        toInvite.sendMessage(serverLang().getPlugMsg() + DataPlayer.getUser(sender).getNickName() + " has sent you a party invite.");

        PartySystem.getFromPlayer(sender).invites.put(toInvite.getUniqueId(), true);

        TextComponent accept = new TextComponent("Accept");
        TextComponent acceptHover = new TextComponent("Click to join " + DataPlayer.getUser(sender).getNickName() + "'s party.");
        accept.setColor(ChatColor.GREEN);
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + sender.getName()));
        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{acceptHover}));

        TextComponent decline = new TextComponent("Decline");
        TextComponent declineHover = new TextComponent("Click to decline " + DataPlayer.getUser(sender).getNickName() + "'s party invite.");
        decline.setColor(ChatColor.RED);
        decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party decline " + sender.getName()));
        decline.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[]{declineHover}));

        if (!PartySystem.getFromPlayer(sender).invites.getOrDefault(toInvite.getUniqueId(), true)) return msg(sender, serverLang().getErrorMsg() + "that player has declined your invite.");

        toInvite.spigot().sendMessage(accept, new TextComponent(" "), decline);
        return msg(sender, PREFIX_PARTY + "Sent " + DataPlayer.getUser(toInvite).getNickName() + " an invite.");
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player)) return null;
        Player player = (Player) sender;
        if (args.length == 1 && PartySystem.getFromPlayer(player) == null) return null;
        if (args.length == 1 && PartySystem.getFromPlayer(player) != null) {
            if (PartySystem.getFromPlayer((Player) sender).getLeader().equals(player.getUniqueId())) return tabOption(args[0], "disband", "list", "team", "warp", "menu");
            else return tabOption(args[0], "menu", "invite", "leave");
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("team")) return tabOption(args[0], PartySystem.PartyTeam.class);
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + "create" + "\n" +
                helpLabel(label) + "team " + helpBr("team", true) + "\n" +
                helpLabel(label) + "menu" + "\n" +
                helpLabel(label) + "list" + "\n" +
                helpLabel(label) + helpBr(USER, true);
    }

    public List<String> commandNames() {
        return List.of("party");
    }
}