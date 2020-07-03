package io.github.rookietec9.enderplugin.commands.games;

import io.github.rookietec9.enderplugin.configs.associates.Blades;
import io.github.rookietec9.enderplugin.events.inventoryclickers.WizardClickEvent;
import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;
import static io.github.rookietec9.enderplugin.utils.reference.Syntax.USER;


/**
 * @author Jeremi
 * @version 22.8.0
 * @since 11.6.6
 */
public class SelectBladeCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ((!(sender instanceof Player)) && (args.length == 1)) return msg(sender, serverLang().getOnlyUserMsg());

        if (args.length == 1 || args.length == 2) {
            Player player = (args.length == 1) ? (Player) sender : Bukkit.getPlayer(args[1]);
            if (player == null) return msg(sender, serverLang().getOfflineMsg());
            return WizardClickEvent.giveBlade(args[0], player) || msg(sender, serverLang().getErrorMsg() + "That's not a valid blade!");
        }
        return msg(sender, getSyntax(label));
    }


    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> list = new ArrayList<>();
        for (Blades blade : Blades.blades) {
            list.add(blade.getName());
        }
        if (args.length == 1) return tabOption(args[0], list);
        return null;
    }

    public String getSyntax(String label) {
        return helpLabel(label) + helpBr("blade type", true) + helpBr(USER, true);
    }

    public List<String> commandNames() {
        return List.of("selectblade");
    }
}