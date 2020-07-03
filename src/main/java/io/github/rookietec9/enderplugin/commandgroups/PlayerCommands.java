package io.github.rookietec9.enderplugin.commandgroups;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.events.main.MainDeathEvent;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Java;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.USER;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 21.4.5
 */
public class PlayerCommands implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        label = label != null ? label : command.getName();

        if (args.length > 1) return msg(sender, getSyntax(label));
        if (args.length == 0 && !(sender instanceof Player)) return msg(sender, getSyntax(label));

        Player player = (args.length == 1) ? Bukkit.getPlayer(args[0]) : (Player) sender;

        if (player == null) return msg(sender, serverLang().getOfflineMsg());

        switch (label.toLowerCase()) {
            case "ext", "extinguish", "enderextinguish" -> player.setFireTicks(-20);
            case "kill", "enderkill" -> MainDeathEvent.fullCheck(player, ((sender instanceof Player) ? (Player) sender : player.getKiller()));
            case "finish", "enderfinish" -> player.setHealth(1);
            case "hug", "enderhug" -> {
                if (args.length == 0) return msg(sender, getSyntax(label));
            }
            case "heal", "enderheal" -> {
                player.setHealth(player.getHealthScale());
                player.setFoodLevel(20);
                for (PotionEffectType e : Minecraft.BAD_EFFECTS()) player.removePotionEffect(e);
            }
            case "clear" -> {
                int i = DataPlayer.getUser(player).clearCount();
                if (i == 0) return msg(sender, serverLang().getPlugMsg() + "Clear nothing.");
                sender.sendMessage(serverLang().getPlugMsg() + "Removed " + i + " item" + (i == 1 ? "" : "s") + ".");
            }
        }

        String prefix = serverLang().getPlugMsg() + Java.capFirst(label) + (label.toLowerCase().endsWith("g") ? "g" : "") + "ed ";
        msg(sender, prefix + DataPlayer.getUser(player).getTabName() + serverLang().getTxtColor() + ".");
        return sender.equals(player) || msg(player, prefix + (sender.equals(player) ? "yourself" : "by " + (sender instanceof Player ? DataPlayer.getUser(sender).getTabName() : "a non-player")) + ".");
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr(USER, false);
    }

    public List<String> commandNames() {
        return List.of("ext","kill","finish","hug","heal","clear");
    }
}