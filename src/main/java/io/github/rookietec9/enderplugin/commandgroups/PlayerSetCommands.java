package io.github.rookietec9.enderplugin.commandgroups;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.User;
import io.github.rookietec9.enderplugin.scoreboards.HubBoard;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.MODE;
import static io.github.rookietec9.enderplugin.Reference.USER;

/**
 * @author Jeremi
 * @version 25.2.6
 * @since 21.4.5
 */
public class PlayerSetCommands implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        label = label != null ? label : command.getName();

        if (!(sender instanceof Player) && args.length < 2) return msg(sender, serverLang().getOnlyUserMsg());
        Player target = (args.length > 1) ? Bukkit.getPlayer(args[1]) : (Player) sender;
        if (target == null && !Java.argWorks(label, "speed")) return msg(sender, serverLang().getOfflineMsg());
        User user = target != null ? DataPlayer.getUser(target) : null;
        String value = "";

        switch (label) {
            case "skin" -> {
                if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
                if (args.length == 0) return msg(sender, getSyntax(label));
                if (Bukkit.getOfflinePlayer(args[0]) == null) return msg(sender, serverLang().getErrorMsg() + "Wrong name.");
                boolean b = DataPlayer.get((Player) sender).setSkin(Bukkit.getOfflinePlayer(args[0]).getUniqueId());

                if (!b) return msg(sender, serverLang().getErrorMsg() + "Skin change failed.");

                if (!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) value = Bukkit.getOfflinePlayer(args[0]).getName() + "'s skin";
                else value = DataPlayer.getUser(Bukkit.getOfflinePlayer(args[0])).getNickName() + "'s skin";

                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.hidePlayer((Player) sender);
                    EnderPlugin.scheduler().runSingleTask(() -> player.showPlayer((Player) sender), "SKIN_FLIP_" + sender.getName().toUpperCase(), 0.25, serverLang().getPlugMsg());
                }
            }
            case "nick" -> {
                if (args.length == 0) return msg(sender, getSyntax(label));
                if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
                value = StringUtils.join(args, " ");
                if (DataPlayer.getUser((Player) sender).getNickName().equalsIgnoreCase(value)) return msg(sender, serverLang().getErrorMsg() + "That's already your nickname.");

                for (User user1 : DataPlayer.users.values()) {
                    if (user1.getNickName().equalsIgnoreCase(value) && user1.wasOnline() == DataPlayer.getUser((Player) sender).wasOnline() && user1.equals(DataPlayer.getUser((Player) sender))) return msg(sender, serverLang().getErrorMsg() + "Someone else already has that nickname.");
                }

                DataPlayer.getUser((Player) sender).setTabName(value);
                DataPlayer.get((Player) sender).setName(value);
            }
            case "god" -> {
                if (args.length > 0 && !Java.argWorks(args[0], "on", "true", "off", "false")) return msg(sender, getSyntax(label));
                value = String.valueOf(args.length == 0 ? !user.getGod() : Java.argWorks(args[0], "on", "true"));
                Objects.requireNonNull(user).setGod(Boolean.parseBoolean(value));
            }
            case "fly" -> {
                if (args.length > 0 && !Java.argWorks(args[0], "on", "true", "off", "false")) return msg(sender, getSyntax(label));
                value = String.valueOf(args.length == 0 ? !target.getAllowFlight() : Java.argWorks(args[0], "on", "true"));
                Objects.requireNonNull(target).setAllowFlight(Boolean.parseBoolean(value));
            }
            case "rank" -> {
                if (!Java.isInRange(args.length, 1, 2) || !Java.argWorks(args[0], "owner", "coowner", "co-owner", "co", "civ", "civilian", "bot", "admin")) return msg(sender, getSyntax(label));
                value = switch (args[0].toLowerCase()) {
                    case "owner" -> "§7[§3§lOwner§7]§f";
                    case "coowner", "co-owner", "co" -> "§7[§b§lCo§7-§b§lOwner§7]§f";
                    case "admin" -> "§7[§c§lAdmin§7]§f";
                    case "bot" -> "§7[§e§lBot§7]§f";
                    default -> "§7[§2§lCivilian§7]§f";
                };
                Objects.requireNonNull(user).setRank(value + " ");
                DataPlayer.get(target).getBoard(HubBoard.class).updateRank();
            }
            case "gamemode", "gm" -> {
                if (!Java.isInRange(args.length, 1, 2)) return msg(sender, getSyntax(label));
                GameMode gm = (switch (args[0].toLowerCase()) {
                    case "a", "adventure", "2" -> GameMode.ADVENTURE;
                    case "sp", "spectator", "3" -> GameMode.SPECTATOR;
                    case "s", "survival", "0" -> GameMode.SURVIVAL;
                    case "c", "creative", "1" -> GameMode.CREATIVE;
                    default -> null;
                });
                if (gm == null) return msg(sender, serverLang().getErrorMsg() + "That's not a valid gamemode!");
                value = Java.capFirst(gm.name());
                Objects.requireNonNull(target).setGameMode(gm);
            }
            case "mute", "unmute" -> {
                target = (args.length > 0) ? Bukkit.getPlayer(args[0]) : (Player) sender;
                boolean setMute = label.equalsIgnoreCase("mute");
                user = DataPlayer.getUser(target);
                user.setMute(setMute);

                String reason = (args.length > 1) ? " for " + Minecraft.tacc(StringUtils.join(args, " ", 2, args.length)) : "";
                String by = (sender instanceof Player) ? " by " + DataPlayer.getUser((Player) sender).getTabName() : "";
                return msg(target, serverLang().getPlugMsg() + "You have been" + (setMute ? "" : "un") + "muted" + by + reason);
            }
            case "speed" -> { //SPEED speed[0] mode[1] player[2]
                if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());
                target = (Player) sender;
                if (!Java.isInRange(args.length, 1, 2) || args.length > 1 && Java.argWorks(args[0], "fly", "walk")) return msg(sender, getSyntax(label));
                float speed;
                boolean isFlying = (args.length < 2 ? target.isFlying() : Java.argWorks(args[1], "fly"));
                try {
                    speed = Float.parseFloat(args[0]);
                } catch (NumberFormatException ex) {
                    return msg(sender, serverLang().getNumFormatMsg());
                }
                if (!Java.isInRange(speed, 0, 10)) return msg(sender, serverLang().getErrorMsg() + "Your speed value must be between 0 and 10!");
                value = String.valueOf(speed);
                speed *= isFlying ? 0.1f : 0.2f;
                speed = speed > 1 ? 1 : speed;
                if (isFlying) target.setFlySpeed(speed);
                else target.setWalkSpeed(speed);
            }
        }
        return msg(sender, serverLang().getPlugMsg() + "Updated " + ((sender.equals(target)) ? "your" : DataPlayer.getUser(target).getNickName() + "'s") + " " + (Java.argWorks(label, "fly", "god") ? label + " mode" : label).replace("gm", "gamemode") + " to " + serverLang().getDarkColor() + value + serverLang().getTxtColor() + ".");
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) return switch (alias.toLowerCase()) {
            case "god", "fly" -> tabOption(args[0], "on", "off");
            case "rank" -> tabOption(args[0], "owner", "co", "admin", "civilian", "bot");
            case "speed" -> tabOption(args[0],"1","2","3","5","6","7","8","9","10");
            case "gamemode", "gm" -> tabOption(args[0], GameMode.class);
            default -> null;
        };
        if (args.length == 2 && alias.equalsIgnoreCase("speed")) return tabOption(args[1], "fly", "walk");
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + switch (label.toLowerCase()) {
            case "skin" -> helpBr(USER, true);
            case "nick" -> helpBr("nick", true);
            case "god", "fly" -> helpBr(MODE, false) + " " + helpBr(USER, false);
            case "rank" -> helpBr("rank", true) + " " + helpBr(USER, false);
            case "gamemode", "gm" -> helpBr("gamemode", true) + " " + helpBr(USER, false);
            case "mute", "unmute" -> helpBr(USER, true) + " " + helpBr("reason", false);
            default -> helpBr("speed", true) + " " + helpBr(MODE, false);
        };
    }

    public List<String> commandNames() {
        return List.of("fly", "gamemode", "god", "mute", "nick", "rank", "skin", "speed", "unmute");
    }
}