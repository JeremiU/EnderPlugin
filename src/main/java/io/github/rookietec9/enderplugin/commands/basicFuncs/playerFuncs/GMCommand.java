package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sets a player's gamemode.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 1.0.1
 */
public class GMCommand implements EndExecutor {
    private final String[] valids = {"a", "s", "c", "sp"};

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if ((!(sender instanceof Player)) && (args.length == 1)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        if (args.length == 1 || args.length == 2) {
            Player player;
            if (args.length == 1) player = (Player) sender;
            else player = Bukkit.getPlayer(args[1]);

            if (player == null) {
                sender.sendMessage(l.getOfflineMsg());
                return true;
            }
            User user = new User(player);
            String name = user.getTabName() + l.getTxtColor() + "'s ";
            if (player == sender) {
                name = "your ";
            }
            switch (args[0].toLowerCase()) {
                case "a":
                case "adventure":
                case "2":
                    player.setGameMode(GameMode.ADVENTURE);
                    sender.sendMessage(l.getPlugMsg() + "Set " + name + "gamemode to " + l.getDarkColor() + Utils.upSlash(player.getGameMode().toString()));
                    break;
                case "sp":
                case "spectator":
                case "3":
                    player.setGameMode(GameMode.SPECTATOR);
                    sender.sendMessage(l.getPlugMsg() + "Set " + name + "gamemode to " + l.getDarkColor() + Utils.upSlash(player.getGameMode().toString()));
                    break;
                case "s":
                case "survival":
                case "0":
                    player.setGameMode(GameMode.SURVIVAL);
                    sender.sendMessage(l.getPlugMsg() + "Set " + name + "gamemode to " + l.getDarkColor() + Utils.upSlash(player.getGameMode().toString()));
                    break;
                case "c":
                case "creative":
                case "1":
                    player.setGameMode(GameMode.CREATIVE);
                    sender.sendMessage(l.getPlugMsg() + "Set " + name + "gamemode to " + l.getDarkColor() + Utils.upSlash(player.getGameMode().toString()));
                    break;
                default:
                    player.sendMessage(l.getErrorMsg() + "That's not a valid gamemode!");
            }
        } else {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> l = new ArrayList<>();
        if (args.length == 1) {
            Collections.addAll(l, valids);
            return l;
        }
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "gamemode" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " + Utils.Reference.OPEN_OPTIONAL_CHAR +
                        l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR
        };
    }

    public String commandName() {
        return "gamemode";
    }
}