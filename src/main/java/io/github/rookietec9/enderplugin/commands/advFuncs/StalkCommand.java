package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.EnderPlugin;
import io.github.rookietec9.enderplugin.configs.associates.User;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.reference.DataType;
import io.github.rookietec9.enderplugin.utils.reference.Prefixes;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.MODE;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.USER;

/**
 * Gets Data from a player.
 *
 * @author Jeremi
 * @version 22.8.0
 * @since 9.0.2
 */
public class StalkCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        int senderLvl = 10, targetLvl;

        if (sender instanceof Player) senderLvl = DataPlayer.getUser((Player) sender).rankLevel();
        if (args.length > 2 || !(sender instanceof Player) && args.length == 0) return msg(sender, getSyntax(label));
        Player player = args.length == 0 ? (Player) sender : Bukkit.getPlayer(args[0]);
        if (player == null) return msg(sender, serverLang().getOfflineMsg());
        targetLvl = DataPlayer.getUser(player).rankLevel();

        if (targetLvl == senderLvl && senderLvl < 3) return msg(sender, serverLang().getErrorMsg() + "Your rank is too low!");

        byte mode = 0;

        if (args.length == 2) {
            if (!Java.argWorks(args[1], "bukkit", "gamedata", "userdata", "all")) return msg(sender, getSyntax(label));
            mode = (byte) switch (args[1]) {
                case "bukkit" -> 1;
                case "gamedata" -> 2;
                case "userdata" -> 3;
                default -> 0;
            };
        }

        switch (mode) {
            case 0 -> {
                sendBukkit(sender, player);
                EnderPlugin.scheduler().runSingleTask(() -> sendGame(sender, DataPlayer.get(player)), "INFO_COMMAND_" + sender.getName().toUpperCase(), 5);
                EnderPlugin.scheduler().runSingleTask(() -> sendUser(sender, DataPlayer.getUser(player)), "INFO_COMMAND_" + sender.getName().toUpperCase(), 10);
            }
            case 1 -> sendBukkit(sender, player);
            case 2 -> sendGame(sender, DataPlayer.get(player));
            case 3 -> sendUser(sender, DataPlayer.getUser(player));
        }
        return true;
    }

    private void sendBukkit(CommandSender sender, Player target) {
        sender.sendMessage(serverLang().getPlugMsg() + "Getting Bukkit Info for" + DataPlayer.getUser(target).getNickName());
        sender.sendMessage(serverLang().getTxtColor() + "Display Name: " + serverLang().getLightColor() + target.getDisplayName());
        sender.sendMessage(serverLang().getTxtColor() + "List Name: " + serverLang().getLightColor() + target.getPlayerListName());
        sender.sendMessage(serverLang().getTxtColor() + "Health: " + serverLang().getLightColor() + target.getHealth() + "/" + target.getHealthScale());
        sender.sendMessage(serverLang().getTxtColor() + "Gamemode: " + serverLang().getLightColor() + target.getGameMode().name());
        sender.sendMessage(serverLang().getTxtColor() + "World: " + serverLang().getLightColor() + target.getWorld().getName());
        sender.sendMessage(serverLang().getTxtColor() + "Location: " + serverLang().getLightColor() + target.getLocation().getBlockX() + " " + target.getLocation().getBlockY() + " " + target.getLocation().getBlockZ());
        sender.sendMessage(serverLang().getTxtColor() + "Can Fly: " + serverLang().getLightColor() + target.getAllowFlight() + serverLang().getTxtColor() + " Is Flying: " + serverLang().getLightColor() + target.isFlying());
        sender.sendMessage(serverLang().getTxtColor() + "Sleeping: " + serverLang().getLightColor() + target.isSleeping() + serverLang().getTxtColor() + " Is Sneaking: " + serverLang().getLightColor() + target.isSneaking() + serverLang().getTxtColor() + " Is Sprinting: " + serverLang().getLightColor() + target.isSprinting());
        sender.sendMessage(serverLang().getTxtColor() + "Is Blocking: " + serverLang().getLightColor() + target.isBlocking());
        sender.sendMessage(serverLang().getTxtColor() + "Is OP: " + serverLang().getLightColor() + target.isOp() + serverLang().getTxtColor() + " Is whitelisted: " + serverLang().getLightColor() + target.isWhitelisted());
        sender.sendMessage(serverLang().getTxtColor() + "Flying Speed: " + serverLang().getLightColor() + target.getFlySpeed() + serverLang().getTxtColor() + " Walking Speed: " + serverLang().getLightColor() + target.getWalkSpeed());
        sender.sendMessage(serverLang().getTxtColor() + "Item in hand: " + serverLang().getLightColor() + target.getItemInHand().getType());
        sender.sendMessage(serverLang().getTxtColor() + "XP: " + serverLang().getLightColor() + target.getExp());
        sender.sendMessage(serverLang().getTxtColor() + "Exhaustion: " + serverLang().getLightColor() + target.getExhaustion() + serverLang().getTxtColor() + " Food Level: " + serverLang().getLightColor() + target.getFoodLevel());
        sender.sendMessage(serverLang().getTxtColor() + "Last Damage:" + serverLang().getLightColor() + target.getLastDamage() + serverLang().getTxtColor() + " Cause: " + serverLang().getLightColor() + (target.getLastDamageCause() != null ? target.getLastDamageCause().getCause() : "Unknown"));
    }

    private void sendGame(CommandSender sender, DataPlayer target) {
        sender.sendMessage(serverLang().getPlugMsg() + "Getting " + Prefixes.BOOTY.replace(">", "") + " Info for " + DataPlayer.getUser(target.player).getNickName());
        sender.sendMessage(serverLang().getTxtColor() + "Temp Kills: " + serverLang().getLightColor() + target.tempBootyKills);
        sender.sendMessage(serverLang().getTxtColor() + "Temp Deaths: " + serverLang().getLightColor() + target.tempBootyDeaths);
        sender.sendMessage(serverLang().getTxtColor() + "Total Kills: " + serverLang().getLightColor() + target.getInt(DataType.BOOTYKILLS));
        sender.sendMessage(serverLang().getTxtColor() + "Total Deaths: " + serverLang().getLightColor() + target.getInt(DataType.BOOTYDEATHS));

        sender.sendMessage(serverLang().getPlugMsg() + "Getting " + Prefixes.CTF.replace(">", "") + " Info for " + DataPlayer.getUser(target.player).getNickName());
        sender.sendMessage(serverLang().getTxtColor() + "Temp Kit: " + serverLang().getLightColor() + target.tempCTFKit);
        sender.sendMessage(serverLang().getTxtColor() + "Temp Kills: " + serverLang().getLightColor() + target.tempCTFKills);
        sender.sendMessage(serverLang().getTxtColor() + "Temp Deaths: " + serverLang().getLightColor() + target.tempCTFDeaths);
        sender.sendMessage(serverLang().getTxtColor() + "Total Wins: " + serverLang().getLightColor() + target.getInt(DataType.CTFWINS));
        sender.sendMessage(serverLang().getTxtColor() + "Total Losses: " + serverLang().getLightColor() + target.getInt(DataType.CTFLOSSES));
        sender.sendMessage(serverLang().getTxtColor() + "Total Points: " + serverLang().getLightColor() + target.getInt(DataType.CTFPOINTS));
        sender.sendMessage(serverLang().getTxtColor() + "Total Captures: " + serverLang().getLightColor() + target.getInt(DataType.CTFCAPTURES));
        sender.sendMessage(serverLang().getTxtColor() + "Total Kills: " + serverLang().getLightColor() + target.getInt(DataType.CTFKILLS));
        sender.sendMessage(serverLang().getTxtColor() + "Total Deaths: " + serverLang().getLightColor() + target.getInt(DataType.CTFDEATHS));

        sender.sendMessage(serverLang().getPlugMsg() + "Getting " + Prefixes.SPLEEF.replace(">", "") + " Info for " + DataPlayer.getUser(target.player).getNickName());
        sender.sendMessage(serverLang().getTxtColor() + "Temp Blocks: " + serverLang().getLightColor() + target.tempSpleefBlocks);
        sender.sendMessage(serverLang().getTxtColor() + "Total Wins: " + serverLang().getLightColor() + target.getInt(DataType.SPLEEFWINS));
        sender.sendMessage(serverLang().getTxtColor() + "Total Losses: " + serverLang().getLightColor() + target.getInt(DataType.SPLEEFLOSSES));
        sender.sendMessage(serverLang().getTxtColor() + "Total Blocks: " + serverLang().getLightColor() + target.getInt(DataType.SPLEEFBLOCKS));

        sender.sendMessage(serverLang().getPlugMsg() + "Getting " + Prefixes.HUB.replace(">", "") + " Info for " + DataPlayer.getUser(target.player).getNickName());
        sender.sendMessage(serverLang().getTxtColor() + "Chat Enabled: " + serverLang().getLightColor() + target.chatEnabled);
        sender.sendMessage(serverLang().getTxtColor() + "PVP Enabled: " + serverLang().getLightColor() + target.pvpEnabled);
        sender.sendMessage(serverLang().getTxtColor() + "Players Enabled: " + serverLang().getLightColor() + target.playersEnabled);

        sender.sendMessage(serverLang().getPlugMsg() + "Getting " + Prefixes.WZRDS.replace(">", "") + " Info for " + DataPlayer.getUser(target.player).getNickName());
        sender.sendMessage(serverLang().getTxtColor() + "Temp Streak: " + serverLang().getLightColor() + target.tempWizardStreak);
        sender.sendMessage(serverLang().getTxtColor() + "Temp Kills: " + serverLang().getLightColor() + target.tempWizardKills);
        sender.sendMessage(serverLang().getTxtColor() + "Temp Deaths: " + serverLang().getLightColor() + target.tempWizardDeaths);
        sender.sendMessage(serverLang().getTxtColor() + "Temp Blade: " + serverLang().getLightColor() + target.tempWizardBlade);
        sender.sendMessage(serverLang().getTxtColor() + "Total Kills: " + serverLang().getLightColor() + target.getInt(DataType.WIZARDKILLS));
        sender.sendMessage(serverLang().getTxtColor() + "Total Deaths: " + serverLang().getLightColor() + target.getInt(DataType.WIZARDDEATHS));

        sender.sendMessage(serverLang().getTxtColor() + "Murder Wins: " + serverLang().getLightColor() + target.getInt(DataType.MURDERWINS));
        sender.sendMessage(serverLang().getTxtColor() + "Murder Name: " + serverLang().getLightColor() + target.getInt(DataType.MURDERLOSSES));

        sender.sendMessage(serverLang().getTxtColor() + "Hide & Seek Wins: " + serverLang().getLightColor() + target.getInt(DataType.HOODWINS));
        sender.sendMessage(serverLang().getTxtColor() + "Hide & Seek Losses: " + serverLang().getLightColor() + target.getInt(DataType.HOODLOSSES));
    }

    private void sendUser(CommandSender sender, User target) {
        sender.sendMessage(serverLang().getPlugMsg() + "Getting " + Prefixes.BOOTY.replace(">", "") + " Info for " + target.getTabName());
        sender.sendMessage(serverLang().getTxtColor() + "Rank: " + serverLang().getLightColor() + target.rank());
        sender.sendMessage(serverLang().getTxtColor() + "Rank Level: " + serverLang().getLightColor() + target.rankLevel());
        sender.sendMessage(serverLang().getTxtColor() + "God Mode: " + serverLang().getLightColor() + target.getGod());
        sender.sendMessage(serverLang().getTxtColor() + "Muted: " + serverLang().getLightColor() + target.isMuted());
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 2) return tabOption(args[0], "bukkit", "gamedata", "userdata", "all");
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(USER, true) + " " + helpBr(MODE, false);
    }

    public List<String> commandNames() {
        return List.of("info");
    }
}