package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.dataFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.List;

/**
 * Lists all effects on a certain player including the duration, ID, and Amplifier (level).
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 2.1.3
 */
public class EffectsListCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length != 0 && args.length != 1) {
            sender.sendMessage(this.getSyntax(command, l));
            return true;
        }
        if (args.length == 0 && !(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        Player player;
        if (args.length == 0) player = (Player) sender;
        else player = Bukkit.getPlayer(args[0]);
        if (player == null) {
            sender.sendMessage(l.getOfflineMsg());
            return true;
        }
        if (player.getActivePotionEffects().isEmpty()) {
            sender.sendMessage(l.getErrorMsg() + "There are no active effects on " + new User(player).getTabName());
            return true;
        }
        sender.sendMessage(l.getPlugMsg() + "Getting active effects for " + new User(player).getTabName() + "...");
        for (PotionEffect pe : player.getActivePotionEffects()) {
            int ticks = pe.getDuration();
            long minute = ticks / 1200;
            long second = ticks / 20 - minute * 60;
            String time = Math.round((float) minute) + ":" + Math.round((float) second);
            sender.sendMessage(l.getDarkColor() + "TYPE: " + l.getLightColor() + pe.getType().getName() + " ," + l.getDarkColor() + " ID: " + l.getLightColor() + pe.getType().getId() + l.getDarkColor() +
                    " AMP: " + l.getLightColor() + pe.getAmplifier() + " ," + l.getDarkColor() + " DUR: " + l.getLightColor() + time);
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "effectslist";
    }
}