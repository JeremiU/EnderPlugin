package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Config;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.EnderPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Gets Data from a player.
 *
 * @author Jeremi
 * @version 13.4.4 // 11.6.0
 * @since 9.0.2
 */
public class StalkCommand implements EndExecutor {

    private final Config data = new Config(true, "", "data.yml", EnderPlugin.getInstance());

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (sender instanceof ConsoleCommandSender) {
            if (args.length != 2) {
                if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                    sender.sendMessage(getSyntax(command, l));
                    return true;
                }
                sender.sendMessage(getSyntax(command, l));
                return true;
            }
            if (Bukkit.getPlayer(args[0]) == null) {
                sender.sendMessage(l.getOfflineMsg());
                return true;
            }
            Player p = Bukkit.getPlayer(args[0]);
            if (!args[1].equalsIgnoreCase("1") && !args[1].equalsIgnoreCase("2")
                    && !args[1].equalsIgnoreCase("3") && !args[1].equalsIgnoreCase("help")) {
                sender.sendMessage(l.getErrorMsg() + "valid pages are from 1 - 3.");
                return true;
            }

            if (args[1].equalsIgnoreCase("1"))
                for (int i = 0; i < page1(p, sender).length; i++)
                    sender.sendMessage(page1(p, sender)[i].replace("null", "N/A"));

            if (args[1].equalsIgnoreCase("2"))
                for (int j = 0; j < page2(p, sender).length; j++)
                    sender.sendMessage(page2(p, sender)[j].replace("null", "N/A"));

            if (args[1].equalsIgnoreCase("3"))
                for (int k = 0; k < page3(p, sender).length; k++)
                    sender.sendMessage(page3(p, sender)[k].replace("null", "N/A"));
            return true;
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(l.getOnlyUserMsg());
                return true;
            }
            if (!sender.isOp() && data.getYaml().getBoolean("config.mustOp")) {
                sender.sendMessage(l.getErrorMsg() + "You must be op!");
                return true;
            }
        }
        if (args.length != 2) {
            sender.sendMessage(getSyntax(command, l));
            return true;
        }

        if (!data.getYaml().getBoolean("config.consoleOnly")) {
            if (Bukkit.getPlayer(args[0]) == null) {
                //sender.sendMessage(l.getNoPlayerMsg());
                return true;
            }
            if (!canStalk(((Player) sender).getUniqueId(), Bukkit.getPlayer(args[0]).getUniqueId())) {
                sender.sendMessage(l.getErrorMsg() + "You cannot stalk that player!");
                return true;
            }
            Player p = Bukkit.getPlayer(args[0]);
            if (!args[1].equalsIgnoreCase("1") && !args[1].equalsIgnoreCase("2") && !args[1].equalsIgnoreCase("3")
                    && !args[1].equalsIgnoreCase("help")) {
                sender.sendMessage(l.getErrorMsg() + "Valid pages are from 1 - 3.");
            }
            if (args[1].equalsIgnoreCase("help")) {
                sender.sendMessage("Valid pages are from 1 - 3.");
            }
            if (args[1].equalsIgnoreCase("1"))
                for (int i = 0; i < page1(p, sender).length; i++)
                    sender.sendMessage(page1(p, sender)[i].replace("null", "N/A"));
            if (args[1].equalsIgnoreCase("2"))
                for (int j = 0; j < page2(p, sender).length; j++)
                    sender.sendMessage(page2(p, sender)[j].replace("null", "N/A"));
            if (args[1].equalsIgnoreCase("3"))
                for (int k = 0; k < page3(p, sender).length; k++)
                    sender.sendMessage(page3(p, sender)[k].replace("null", "N/A"));
        }
        return true;
    }

    /**
     * Checks if the sender level is capable of stalking the target level.
     *
     * @param senderUUID sender's uuid
     * @param targetUUID target's uuid
     *                   <p>
     *                   CHECKS FOR: 1: TARGET LEVEL IS HIGHER THEN SENDER 2: TARGET
     *                   LEVEL IS EQUAL TO SENDER LEVEL 3: SENDER LEVEL IS A 0 (A BAN
     *                   FROM GETTING LEVELS) 4: TARGET LEVEL IS A 6 (PREVENTS FROM
     *                   OTHERS STALKING) returns false;
     *                   <p>
     *                   1: TARGET LEVEL IS 0 AND SENDER IS HIGHER THEN 0 2: TARGET
     *                   LEVEL IS LOWER THEN SENDER LEVEL 3: TARGET UUID IS SENDER UUID
     *                   AND TARGET LEVEL IS NOT 0 returns true;
     * @return true if sender is capable of stalking.
     * @author TheEnderCrafter9
     */
    private boolean canStalk(UUID senderUUID, UUID targetUUID) {
        data.modifyYaml();
        if (data.getYaml().getString("Players." + senderUUID.toString()) == null) {
            data.getYaml().createSection("Players." + senderUUID.toString());
            data.getYaml().set("Players." + senderUUID.toString(), data.getYaml().getInt("config.default"));
            data.modifyYaml();
        }
        if (data.getYaml().getString("Players." + targetUUID.toString()) == null) {
            data.getYaml().createSection("Players." + targetUUID.toString());
            data.getYaml().set("Players." + targetUUID.toString(), data.getYaml().getInt("config.default"));
            data.modifyYaml();
        }
        int sender = data.getYaml().getInt("Players." + senderUUID.toString());
        int target = data.getYaml().getInt("Players." + targetUUID.toString());

        if ((sender > target) && sender > data.getYaml().getInt("config.lowLVL") && target < data.getYaml().getInt("config.highLVL")
                || (senderUUID == targetUUID && sender != data.getYaml().getInt("config.lowLVL")) || sender == target) {
            EnderPlugin.getInstance().debug("[EC-ST] " + "Sender UUID : " + ChatColor.WHITE + senderUUID.toString());
            EnderPlugin.getInstance().debug("[EC-ST] " + "Target UUID : " + ChatColor.WHITE + targetUUID.toString());
            EnderPlugin.getInstance().debug("[EC-ST] " + "Sender lvl  : " + ChatColor.WHITE + sender);
            EnderPlugin.getInstance().debug("[EC-ST] " + "Target lvl  : " + ChatColor.WHITE + target);
            EnderPlugin.getInstance().debug("[EC-ST] " + "TRUE");
            return true;
        }
        if ((sender < target && senderUUID != targetUUID) || sender == 0 || target == 6) {
            EnderPlugin.getInstance().debug("[EC-ST] " + "Sender UUID : " + ChatColor.WHITE + senderUUID.toString());
            EnderPlugin.getInstance().debug("[EC-ST] " + "Target UUID : " + ChatColor.WHITE + targetUUID.toString());
            EnderPlugin.getInstance().debug("[EC-ST] " + "Sender lvl  : " + ChatColor.WHITE + sender);
            EnderPlugin.getInstance().debug("[EC-ST] " + "Target lvl  : " + ChatColor.WHITE + target);
            EnderPlugin.getInstance().debug("[EC-ST] " + "FALSE");
            return false;
        }
        return false;
    }

    private String[] page1(Player player, CommandSender s) {
        Lang l = new Lang(Langs.fromSender(s));
        return new String[]{l.getDarkColor() + "Page: " + l.getLightColor() + "1 / 3",
                l.getDarkColor() + "Is op: " + l.getLightColor() + player.isOp(),
                l.getDarkColor() + "Is flying: " + l.getLightColor() + player.isFlying(),
                l.getDarkColor() + "Is blocking: " + l.getLightColor() + player.isBlocking(),
                l.getDarkColor() + "Is dead: " + l.getLightColor() + player.isDead(),
                l.getDarkColor() + "Is riding: " + l.getLightColor() + player.isInsideVehicle(),
                l.getDarkColor() + "Is sleeping: " + l.getLightColor() + player.isSleeping(),
                l.getDarkColor() + "Is sneaking: " + l.getLightColor() + player.isSneaking(),
                l.getDarkColor() + "Is sprinting: " + l.getLightColor() + player.isSprinting(),
                l.getDarkColor() + "Can pickup items: " + l.getLightColor() + player.getCanPickupItems(),
                "Type /stalk " + "{USERNAME}" + " 2 for more."};
    }

    private String[] page2(Player player, CommandSender s) {
        Lang l = new Lang(Langs.fromSender(s));
        return new String[]{l.getDarkColor() + "Page: " + l.getLightColor() + "2 / 3",
                l.getDarkColor() + "Is holding: " + l.getLightColor() + player.getItemOnCursor().getType().toString(),
                l.getDarkColor() + "Ip: " + l.getLightColor() + player.getAddress(),
                l.getDarkColor() + "Nick: " + l.getLightColor() + player.getCustomName(),
                l.getDarkColor() + "Total XP: " + l.getLightColor() + l.getLightColor() + player.getExp(),
                l.getDarkColor() + "XP to level up:" + l.getLightColor() + player.getExpToLevel(),
                l.getDarkColor() + "Xp level: " + l.getLightColor() + player.getLevel(),
                l.getDarkColor() + "Eye height: " + l.getLightColor() + player.getEyeHeight(),
                l.getDarkColor() + "Fallen: " + l.getLightColor() + player.getFallDistance(),
                l.getDarkColor() + "Fly speed: " + l.getLightColor() + String.valueOf(player.getFlySpeed()).substring(0, 3),
                l.getDarkColor() + "Hunger: " + l.getLightColor() + player.getFoodLevel() + "/20.0",
                l.getDarkColor() + "Health: " + l.getLightColor() + (int) Math.round(player.getHealth()) + "/" + player.getHealthScale(),
                l.getDarkColor() + "Walk speed: " + l.getLightColor() + String.valueOf(player.getWalkSpeed()).substring(0, 3),
                "Type /stalk " + "{USERNAME}" + " 3 for more"};
    }

    private String[] page3(Player player, CommandSender s) {
        Lang l = new Lang(Langs.fromSender(s));
        return new String[]{l.getDarkColor() + "Page: " + l.getLightColor() + "3 / 3",
                l.getDarkColor() + "World: " + l.getLightColor() + player.getWorld().getName(),
                l.getDarkColor() + "Game Mode: " + l.getLightColor() + player.getGameMode(),
                l.getDarkColor() + "UUID: " + l.getLightColor() + player.getUniqueId().toString(),
                l.getDarkColor() + "Is whitelisted: " + l.getLightColor() + player.isWhitelisted(),
                l.getDarkColor() + "Last killed by: " + l.getLightColor() + player.getKiller(),
                l.getDarkColor() + "Is on ground: " + l.getLightColor() + player.isOnGround(),
                l.getDarkColor() + "Location: " + l.getLightColor() + player.getLocation().getBlockX() + ", " + player.getLocation().getBlockY() + ", " + player.getLocation().getBlockZ(),
                l.getDarkColor() + "World: " + l.getLightColor() + player.getLocation().getWorld().getName(),
        };
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + Utils.Reference.USER + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR + " " +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "page" + Utils.Reference.CLOSE_MANDATORY_CHAR

        };
    }

    public String commandName() {
        return "stalk";
    }
}