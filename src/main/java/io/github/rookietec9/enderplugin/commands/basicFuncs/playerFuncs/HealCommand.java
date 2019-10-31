package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Heals the sender or a targeted player, removes all evil effects <b>COUGH COUGH ESSENTIALS</b>, and restores hunger.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 0.0.4
 */
public class HealCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        PotionEffectType[] bad = {PotionEffectType.BLINDNESS, PotionEffectType.CONFUSION, PotionEffectType.HARM,
                PotionEffectType.HUNGER, PotionEffectType.POISON, PotionEffectType.SATURATION, PotionEffectType.SLOW,
                PotionEffectType.SLOW_DIGGING, PotionEffectType.WEAKNESS, PotionEffectType.WITHER,};

        Lang l = new Lang(Langs.fromSender(sender));
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(l.getOnlyUserMsg());
                return true;
            }
            Player player = (Player) sender;
            player.setHealth(player.getHealthScale());
            player.setFoodLevel(20);
            for (PotionEffectType e : bad) {
                player.removePotionEffect(e);
            }
            player.sendMessage(l.getPlugMsg() + "Healed.");
            return true;
        }
        if (args.length == 1) {
            Player target = sender.getServer().getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(l.getOfflineMsg());
                return true;
            }
            target.setHealth(target.getHealthScale());
            target.setFoodLevel(20);
            for (PotionEffectType e : bad) {
                target.removePotionEffect(e);
            }
            sender.sendMessage(l.getPlugMsg() + "Healed " + new User(target).getTabName() + ".");
            if (sender instanceof Player)
                target.sendMessage(l.getPlugMsg() + "Healed by " + new User(target).getTabName() + ".");
        }
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase() + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_OPTIONAL_CHAR + l.getLightColor() + Utils.Reference.MODE + l.getCmdExColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR + " " + Utils.Reference.OPEN_OPTIONAL_CHAR +
                        l.getLightColor() + Utils.Reference.USER + l.getTxtColor() + Utils.Reference.CLOSE_OPTIONAL_CHAR
        };
    }

    public String commandName() {
        return "heal";
    }
}