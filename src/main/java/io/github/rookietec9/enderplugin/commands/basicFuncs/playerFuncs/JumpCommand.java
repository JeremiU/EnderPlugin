package io.github.rookietec9.enderplugin.commands.basicFuncs.playerFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.Utils;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Teleports the player to the block it was looking at.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 5.7.5
 */
public class JumpCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Lang l = new Lang(Langs.fromSender(sender));
        if (sender instanceof Player) {
            Player p = (Player) sender;

            Location la = p.getTargetBlock(Utils.HOLLOW_MATERIALS(), 300).getLocation();
            la.setPitch(p.getLocation().getPitch());
            la.setYaw(p.getLocation().getYaw());
            p.teleport(la);
            p.sendMessage(l.getPlugMsg() + "Jumped.");
        } else sender.sendMessage(l.getOnlyUserMsg());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "jump";
    }
}