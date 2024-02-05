package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.endcommands.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.Reference.USER;

/**
 * Lists all effects on a certain player including the duration, ID, and Amplifier (level).
 *
 * @author Jeremi
 * @version 25.2.0
 * @since 2.1.3
 */
public class EffectsListCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) return msg(sender, getSyntax(label));
        if (args.length == 0 && !(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());

        Player player = (args.length == 0) ? (Player) sender : Bukkit.getPlayer(args[0]);
        if (player == null) return msg(sender, serverLang().getOfflineMsg());
        if (player.getActivePotionEffects().isEmpty()) return msg(sender, serverLang().getErrorMsg() + "There are no active effects on the player!");

        sender.sendMessage(serverLang().getPlugMsg() + "Getting active effects for " + DataPlayer.getUser(player).getTabName() + "...");
        for (PotionEffect pe : player.getActivePotionEffects()) {
            int ticks = pe.getDuration();
            double minute = ticks / 1200d;
            double second = ticks / 20d - minute * 60;
            String time = Math.round(minute) + ":" + Math.round(second);
            sender.sendMessage(serverLang().getDarkColor() + "TYPE: " + serverLang().getLightColor() + pe.getType().getName() + " ," + serverLang().getDarkColor() + " ID: " + serverLang().getLightColor() + pe.getType().getId() + serverLang().getDarkColor() +
                    " AMP: " + serverLang().getLightColor() + pe.getAmplifier() + " ," + serverLang().getDarkColor() + " DUR: " + serverLang().getLightColor() + time);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(USER, false);
    }

    public List<String> commandNames() {
        return List.of("effectslist");
    }
}