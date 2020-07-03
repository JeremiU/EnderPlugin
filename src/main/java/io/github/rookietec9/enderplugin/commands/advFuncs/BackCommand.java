package io.github.rookietec9.enderplugin.commands.advFuncs;

import io.github.rookietec9.enderplugin.utils.datamanagers.EndExecutor;
import io.github.rookietec9.enderplugin.utils.datamanagers.DataPlayer;
import io.github.rookietec9.enderplugin.utils.methods.Minecraft;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static io.github.rookietec9.enderplugin.EnderPlugin.serverLang;

/**
 * @author Jeremi
 * @version 22.8.0
 * @since 21.0.7
 */
public class BackCommand implements EndExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) return msg(sender, serverLang().getOnlyUserMsg());

        DataPlayer.get((Player) sender).backloc();
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }

    public String getSyntax(String label) {
        return null;
    }

    public List<String> commandNames() {
        return List.of("back");
    }
}