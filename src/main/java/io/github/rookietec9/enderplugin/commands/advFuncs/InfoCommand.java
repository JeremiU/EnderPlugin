package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.configs.DataType;
import io.github.rookietec9.enderplugin.configs.associates.User;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.*;

/**
 * Gets Data from a player.
 *
 * @author Jeremi
 * @version 25.2.9
 * @since 9.0.2
 */
public class InfoCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int senderLvl = 10, targetLvl;

        if (sender instanceof Player) senderLvl = DataPlayer.getUser((Player) sender).rankLevel();
        if (!Java.isInRange(args.length, 1, 3)) return msg(sender, getSyntax(label));
        Player player = Bukkit.getPlayer(args[0]);
        if (player == null) return msg(sender, serverLang().getOfflineMsg());
        targetLvl = DataPlayer.getUser(player).rankLevel();
        DataPlayer target = DataPlayer.get(player);

        if (targetLvl == senderLvl && senderLvl < 3) return msg(sender, serverLang().getErrorMsg() + "Your rank is too low!");

        int section;

        String[][] info = {sendBukkit(player), sendBooty(target), sendCTF(target), sendSpleef(target), sendMurder(target), sendWizards(target), sendUser(DataPlayer.getUser(player)), sendHub(target)};

        if (args.length == 1) {
            for (String[] infos : info) sender.sendMessage(infos);
            return true;
        }
        section = switch (args[1].toLowerCase()) {
            case "bukkit" -> 0;
            case "booty" -> 1;
            case "ctf" -> 2;
            case "spleef" -> 3;
            case "murderer" -> 4;
            case "wizards" -> 5;
            case "user" -> 6;
            case "hub" -> 7;
            default -> -2;
        };

        int detail = -1;

        if (args.length == 3) switch (section) {
            case 1, 2, 5 -> {
                switch (args[2].toLowerCase()) {
                    case "kills" -> detail = 0;
                    case "deaths" -> detail = 1;
                    case "streak", "wins" -> detail = 2;
                    case "blade", "losses" -> detail = 3;
                    case "points" -> detail = 4;
                    case "captures" -> detail = 5;
                }
            }
            case 0 -> {
                switch (args[2].toLowerCase()) {
                    case "dispname" -> detail = 0;
                    case "listname" -> detail = 1;
                    case "health" -> detail = 2;
                    case "gm", "gamemode" -> detail = 3;
                    case "world" -> detail = 4;
                    case "location" -> detail = 5;
                    case "fly" -> detail = 6;
                    case "movement" -> detail = 7;
                    case "blocking" -> detail = 8;
                    case "serverperms" -> detail = 9;
                    case "speed" -> detail = 10;
                    case "hand" -> detail = 11;
                    case "xp" -> detail = 12;
                    case "saturation" -> detail = 13;
                    case "damage" -> detail = 14;
                }
            }
            case 3 -> {
                switch (args[2]) {
                    case "blocks" -> detail = 0;
                    case "wins" -> detail = 1;
                    case "losses" -> detail = 2;
                }
            }
            case 4 -> {
                switch (args[2].toLowerCase()) {
                    case "murderwins" -> detail = 0;
                    case "murderlosses" -> detail = 1;
                    case "hoodwins" -> detail = 2;
                    case "hoodlosses" -> detail = 3;
                }
            }
            case 6 -> {
                switch (args[2].toLowerCase()) {
                    case "rank" -> detail = 0;
                    case "ranklvl" -> detail = 1;
                    case "god" -> detail = 2;
                    case "mute" -> detail = 3;
                    case "skin" -> detail = 4;
                }
            }
            case 7 -> {
                switch (args[2].toLowerCase()) {
                    case "chat" -> detail = 0;
                    case "pvp" -> detail = 1;
                    case "players" -> detail = 2;
                }
            }
        }


        if (detail == -1 && args.length == 3 || section == -2) return msg(sender, getSyntax(label));

        if (detail == -1) sender.sendMessage(info[section]);
        else sender.sendMessage(info[section][detail]);
        return true;
    }

    private String[] sendBukkit(Player target) {
        return new String[]{
                serverLang().getTxtColor() + "Display Name: " + serverLang().getLightColor() + target.getDisplayName(),
                serverLang().getTxtColor() + "List Name: " + serverLang().getLightColor() + target.getPlayerListName(),
                serverLang().getTxtColor() + "Health: " + serverLang().getLightColor() + target.getHealth() + "/" + target.getHealthScale(),
                serverLang().getTxtColor() + "Gamemode: " + serverLang().getLightColor() + target.getGameMode().name(),
                serverLang().getTxtColor() + "World: " + serverLang().getLightColor() + target.getWorld().getName(),
                serverLang().getTxtColor() + "Location: " + serverLang().getLightColor() + target.getLocation().getBlockX() + " " + target.getLocation().getBlockY() + " " + target.getLocation().getBlockZ(),
                serverLang().getTxtColor() + "Can Fly: " + serverLang().getLightColor() + target.getAllowFlight() + serverLang().getTxtColor() + " Is Flying: " + serverLang().getLightColor() + target.isFlying(),
                serverLang().getTxtColor() + "Is Sneaking: " + serverLang().getLightColor() + target.isSneaking() + serverLang().getTxtColor() + " Is Sprinting: " + serverLang().getLightColor() + target.isSprinting(),
                serverLang().getTxtColor() + "Is Blocking: " + serverLang().getLightColor() + target.isBlocking(),
                serverLang().getTxtColor() + "Is OP: " + serverLang().getLightColor() + target.isOp() + serverLang().getTxtColor() + " Is whitelisted: " + serverLang().getLightColor() + target.isWhitelisted(),
                serverLang().getTxtColor() + "Flying Speed: " + serverLang().getLightColor() + target.getFlySpeed() + serverLang().getTxtColor() + " Walking Speed: " + serverLang().getLightColor() + target.getWalkSpeed(),
                serverLang().getTxtColor() + "Item in hand: " + serverLang().getLightColor() + target.getItemInHand().getType(),
                serverLang().getTxtColor() + "XP: " + serverLang().getLightColor() + target.getExp(),
                serverLang().getTxtColor() + "Exhaustion: " + serverLang().getLightColor() + target.getExhaustion() + serverLang().getTxtColor() + " Food Level: " + serverLang().getLightColor() + target.getFoodLevel(),
                serverLang().getTxtColor() + "Last Damage: " + serverLang().getLightColor() + target.getLastDamage() + serverLang().getTxtColor() + " Cause: " + serverLang().getLightColor() + (target.getLastDamageCause() != null ? target.getLastDamageCause().getCause() : "Unknown/Null"),
        };
    }

    private String[] sendBooty(DataPlayer target) { //v1
        return new String[]{
                PREFIX_BOOTY(target.player) + serverLang().getTxtColor() + "Kills: " + serverLang().getLightColor() + target.getInt(DataType.BOOTYKILLS),
                serverLang().getTxtColor() + "Deaths: " + serverLang().getLightColor() + target.getInt(DataType.BOOTYDEATHS),
                serverLang().getTxtColor() + "Streak: " + serverLang().getLightColor() + target.tempBootyStreak
        };
    }

    private String[] sendCTF(DataPlayer target) { //v1
        return new String[]{
                PREFIX_CTF + serverLang().getTxtColor() + "Kills: " + serverLang().getLightColor() + target.getInt(DataType.CTFKILLS),
                serverLang().getTxtColor() + "Deaths: " + serverLang().getLightColor() + target.getInt(DataType.CTFDEATHS),
                serverLang().getTxtColor() + "Wins: " + serverLang().getLightColor() + target.getInt(DataType.CTFWINS),
                serverLang().getTxtColor() + "Losses: " + serverLang().getLightColor() + target.getInt(DataType.CTFLOSSES),
                serverLang().getTxtColor() + "Points: " + serverLang().getLightColor() + target.getInt(DataType.CTFPOINTS),
                serverLang().getTxtColor() + "Captures: " + serverLang().getLightColor() + target.getInt(DataType.CTFCAPTURES),
        };
    }

    private String[] sendSpleef(DataPlayer target) {
        return new String[]{
                PREFIX_SPLEEF + serverLang().getTxtColor() + "Blocks Mined: " + serverLang().getLightColor() + target.getInt(DataType.SPLEEFBLOCKS),
                serverLang().getTxtColor() + "Wins: " + serverLang().getLightColor() + target.getInt(DataType.SPLEEFWINS),
                serverLang().getTxtColor() + "Losses: " + serverLang().getLightColor() + target.getInt(DataType.SPLEEFLOSSES),
        };
    }

    private String[] sendHub(DataPlayer target) {
        return new String[]{
                PREFIX_HUB + serverLang().getTxtColor() + "Chat Enabled: " + serverLang().getLightColor() + target.chatEnabled,
                serverLang().getTxtColor() + "PVP Enabled: " + serverLang().getLightColor() + target.pvpEnabled,
                serverLang().getTxtColor() + "Players Enabled: " + serverLang().getLightColor() + target.playersEnabled
        };
    }

    private String[] sendWizards(DataPlayer target) { //v1
        return new String[]{
                PREFIX_WZRDS + serverLang().getTxtColor() + "Kills: " + serverLang().getLightColor() + target.getInt(DataType.WIZARDKILLS),
                serverLang().getTxtColor() + "Deaths: " + serverLang().getLightColor() + target.getInt(DataType.WIZARDDEATHS),
                serverLang().getTxtColor() + "Streak: " + serverLang().getLightColor() + target.tempWizardStreak,
                serverLang().getTxtColor() + "Blade: " + serverLang().getLightColor() + target.tempWizardBlade,
        };
    }

    private String[] sendMurder(DataPlayer target) {
        return new String[]{
                PREFIX_MMG_LOBBY + "Jail Wins: " + serverLang().getLightColor() + target.getInt(DataType.MURDERWINS),
                serverLang().getTxtColor() + "Jail Losses: " + serverLang().getLightColor() + target.getInt(DataType.MURDERLOSSES),
                serverLang().getTxtColor() + "Jail Wins: " + serverLang().getLightColor() + target.getInt(DataType.HOODWINS),
                serverLang().getTxtColor() + "Jail Losses: " + serverLang().getLightColor() + target.getInt(DataType.HOODLOSSES)
        };
    }

    private String[] sendUser(User target) {
        return new String[]{
                serverLang().getPlugMsg() + "Rank: " + serverLang().getLightColor() + target.rank(),
                serverLang().getTxtColor() + "Rank Level: " + serverLang().getLightColor() + target.rankLevel(),
                serverLang().getTxtColor() + "God Mode: " + serverLang().getLightColor() + target.getGod(),
                serverLang().getTxtColor() + "Muted: " + serverLang().getLightColor() + target.isMuted(),
                serverLang().getTxtColor() + "Skin Owner: " + serverLang().getLightColor() + Bukkit.getOfflinePlayer(target.getSkinUUID()).getName(),
        };
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) return tabOption(args[1], "bukkit", "booty", "ctf", "spleef", "murderer", "wizards", "user", "hub");
        if (args.length == 3) {
            return switch (args[1].toLowerCase()) {
                case "bukkit" ->  tabOption(args[2],"dispName","listName","health","gamemode","world","location","fly","movement","blocking","serverPerms", "speed","hand","xp","saturation","damage");
                case "booty" -> tabOption(args[2],"kills","deaths","streak");
                case "ctf" -> tabOption(args[2],"kills","deaths","wins","losses","points","captures");
                case "spleef" -> tabOption(args[2],"blocks","wins","losses");
                case "murderer" -> tabOption(args[2],"murderwins","murderlosses","hoodwins","hoodlosses");
                case "wizards" -> tabOption(args[2],"kills","deaths","streak","blade");
                case "user" -> tabOption(args[2],"rank","rankLvl","god","mute","skin");
                case "hub" -> tabOption(args[2],"chat","pvp","players");
                default -> null;
            };
        }
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(USER, true) + " " + helpBr(MODE, true) + helpBr(MODE, true);
    }

    public List<String> commandNames() {
        return List.of("info");
    }
}