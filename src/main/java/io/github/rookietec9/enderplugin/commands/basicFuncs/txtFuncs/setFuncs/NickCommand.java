package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs.setFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import io.github.rookietec9.enderplugin.API.configs.associates.User;
import io.github.rookietec9.enderplugin.xboards.HubBoard;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Sets a player's nickname. Used for chat and the player list.
 *
 * @author Jeremi
 * @version 14.8.4
 * @since 0.7.0
 */
public class NickCommand implements EndExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (!(sender instanceof Player)) {
            sender.sendMessage(l.getOnlyUserMsg());
            return true;
        }
        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(this.getSyntax(command, l));
            return true;
        }
        new User(player).setTabName(StringUtils.join(args, " ").replace("{PLAYERNAME}", player.getName()));
        player.sendMessage(l.getPlugMsg() + "changed nickname.");
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.setPlayerListName(new User(p).getTabName());
        }
        if (player.getWorld().getName().equalsIgnoreCase(Utils.Reference.Worlds.HUB)) new HubBoard(player).updateRank();
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return new String[]{
                l.getSyntaxMsg() + l.getCmdExColor() + "/" + l.getLightColor() + command.getName().toLowerCase().replace("enderd","endern") + " " + l.getCmdExColor() +
                        Utils.Reference.OPEN_MANDATORY_CHAR + l.getLightColor() + "nickname" + l.getCmdExColor() + Utils.Reference.CLOSE_MANDATORY_CHAR
        };
    }

    public String commandName() {
        return "nick";
    }
}