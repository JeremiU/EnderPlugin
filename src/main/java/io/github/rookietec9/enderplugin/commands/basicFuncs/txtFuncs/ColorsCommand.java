package io.github.rookietec9.enderplugin.commands.basicFuncs.txtFuncs;

import io.github.rookietec9.enderplugin.API.EndExecutor;
import io.github.rookietec9.enderplugin.API.configs.Langs;
import io.github.rookietec9.enderplugin.API.configs.associates.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Lists all the possible colors and formatting characters used in the game.
 *
 * @author Jeremi
 * @version 13.4.4
 * @since 0.8.8
 */
public class ColorsCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(new Lang(Langs.fromSender(sender)).getPlugMsg() + "§00 §11 §22 §33 §44 §55 §66 §77 §88 §99 §f/ §AA §BB §CC §DD §EE §FF / §RR §r§LBOLD §r§mm§r §r§nn§r §r§oo§r §r§kk§r (k)");
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String[] getSyntax(Command command, Lang l) {
        return null;
    }

    public String commandName() {
        return "colormap";
    }
}